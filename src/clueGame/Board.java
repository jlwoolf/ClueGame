package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {
	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;

	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;

	private Set<BoardCell> targets;
	private Set<BoardCell> visited;

	//Singleton Design Pattern implementation
	private static Board theInstance = new Board();
	private Board() {
		super();
	}
	public static Board getInstance() {
		return theInstance;
	}
	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (FileNotFoundException | BadConfigFormatException e) {
			e.printStackTrace();
		}

		calcAdj();
	}

	//initialization methods
	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		this.layoutConfigFile = "data/" + layoutConfigFile;
		this.setupConfigFile = "data/" + setupConfigFile;
	}

	//load setup config and check for errors
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException{
		Scanner fileReader = new Scanner(new File(setupConfigFile));
		roomMap = new HashMap<Character, Room>();
		//parse through file and add each room to roomMap
		while(fileReader.hasNext()) {
			String line = fileReader.nextLine();
			if(line.contains("//"))
				continue;

			//make sure only two types of spaces exist, Card and Other
			//throws error if otherwise
			String[] lineContents = line.split(", ");
			if(!lineContents[0].equals("Room") && !lineContents[0].equals("Space"))
				throw new BadConfigFormatException("Setup contains more than two types of cells. Likely because a comment in setup is nost valid");

			String roomName = lineContents[1];
			Character roomChar = lineContents[2].charAt(0);

			Room room = new Room(roomName);
			roomMap.put(roomChar, room);
		}
		fileReader.close();
	}

	//load layout config and check for errors
	@SuppressWarnings("resource")
	public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException {
		//get number of rows and columns to initialze the grid
		File file = new File(layoutConfigFile);
		Scanner fileReader = new Scanner(file);
		setRowsColumns(fileReader);
		grid = new BoardCell[numRows][numColumns];
		fileReader.close();

		//re-run through file line by line
		fileReader = new Scanner(file);
		for(int i = 0; i < numRows; i++) {
			//get line contents and split into each cell
			String line = fileReader.nextLine();
			String[] lineContents = line.split(",");
			for(int j = 0; j < numColumns; j++) {
				//verify that room is a valid room from setup file
				if(!roomMap.containsKey(lineContents[j].charAt(0))) {
					throw new BadConfigFormatException("Room map contains a letter that isn't specified in the setup");
				} else {
					//update grid cell with proper infromation from file
					grid[i][j] = new BoardCell(i, j, lineContents[j].charAt(0));
					if(lineContents[j].contains("*")) {
						grid[i][j].setRoomCenter(true);
						roomMap.get(grid[i][j].getInitial()).setCenterCell(grid[i][j]);
					}
					if(lineContents[j].contains("#")) {
						grid[i][j].setLabel(true);
						roomMap.get(lineContents[j].charAt(0)).setLabelCell(grid[i][j]);
					}
					if(lineContents[j].contains("^")) {
						grid[i][j].setDoorway(DoorDirection.UP);
					} else if(lineContents[j].contains("v")) {
						grid[i][j].setDoorway(DoorDirection.DOWN);
					} else if(lineContents[j].contains("<")) {
						grid[i][j].setDoorway(DoorDirection.LEFT);
					} else if(lineContents[j].contains(">")) {
						grid[i][j].setDoorway(DoorDirection.RIGHT);
					}

					if(lineContents[j].length() > 1 && roomMap.containsKey(lineContents[j].charAt(1))) {
						grid[i][j].setSecretPassage(lineContents[j].charAt(1));
					}
				}
			}
		}

	}
	//calculate row and column counts
	private void setRowsColumns(Scanner fileReader) throws BadConfigFormatException {
		int rows = 0;
		int cols = 0;
		numColumns = 0;
		while(fileReader.hasNextLine()) {
			String line = fileReader.nextLine();
			String[] lineContents = line.split(",");
			cols = lineContents.length;
			if(numColumns == 0)
				numColumns = cols;
			else if(numColumns != cols)
				throw new BadConfigFormatException("Different number of columns in each row");

			rows++;
		}
		numRows = rows;
	}
	//getters for rows and columns
	public int getNumRows() {
		return numRows;
	}
	public int getNumColumns() {
		return numColumns;
	}

	//getters for room of cell or character
	public Room getRoom(char roomChar) {
		return roomMap.get(roomChar);
	}
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}

	//methods for getting target spaces
	public void calcTargets(BoardCell startCell, int pathLength) {
		visited = new HashSet<>();
		visited.add(startCell);
		targets = recursiveTargets(startCell, pathLength);
	}
	public Set<BoardCell> getTargets() {
		return targets;
	}	
	private Set<BoardCell> recursiveTargets(BoardCell startCell, int pathLength) {
		Set<BoardCell> targetSet = new HashSet<>();

		for(BoardCell adjCell : startCell.getAdjList()) {
			if(visited.contains(adjCell) || adjCell.isOccupied()) {
				//checks if occupied is a room
				if(adjCell.isOccupied() && adjCell.isRoom())
					targetSet.add(adjCell);
				continue;
			} 
			visited.add(adjCell);
			if(pathLength == 1 || adjCell.isRoom()) {
				targetSet.add(adjCell);
			} else {
				targetSet.addAll(recursiveTargets(adjCell, pathLength-1));
			}
			visited.remove(adjCell);
		}

		return targetSet;
	}

	public Set<BoardCell> getAdjList(int i, int j) {
		return grid[i][j].getAdjList();
	}
	private void calcAdj() {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				//calculates the adjencies for the cell if its a room, doorway, or walkway
				//calcRoomAdj only calculates adjacencies for room centers
				if(grid[i][j].isRoom()) {
					calcRoomAdj(i, j);
				} else if (grid[i][j].isDoorway()) {
					calcDoorwayAdj(i, j);
				} else if (grid[i][j].getInitial() == 'W') {
					calcWalkwayAdj(i, j);
				}
			}
		}
	}

	private void calcRoomAdj(int i, int j) {
		//checks if room is a secret passage
		if(grid[i][j].isSecretPassage()) {
			roomMap.get(grid[i][j].getInitial()).getCenterCell().addAdj(roomMap.get(grid[i][j].getSecretPassage()).getCenterCell());
		}
		//checks for each adj if its a doorway that is pointing into active room
		if(i != 0 && grid[i-1][j].isDoorway() && grid[i-1][j].getDoorDirection() == DoorDirection.DOWN) {
			roomMap.get(grid[i][j].getInitial()).getCenterCell().addAdj(grid[i-1][j]);
		}
		if(i != numRows-1 && grid[i+1][j].isDoorway() && grid[i+1][j].getDoorDirection() == DoorDirection.UP) {
			roomMap.get(grid[i][j].getInitial()).getCenterCell().addAdj(grid[i+1][j]);
		}
		if(j != 0 && grid[i][j-1].isDoorway() && grid[i][j-1].getDoorDirection() == DoorDirection.RIGHT) {
			roomMap.get(grid[i][j].getInitial()).getCenterCell().addAdj(grid[i][j-1]);
		}
		if(j != numColumns-1 && grid[i][j+1].isDoorway() && grid[i][j+1].getDoorDirection() == DoorDirection.LEFT) {
			roomMap.get(grid[i][j].getInitial()).getCenterCell().addAdj(grid[i][j+1]);
		}
	}
	private void calcDoorwayAdj(int i, int j) {
		//checks if each adj is a room that the active doorway is pointing into
		//if it's not a room, checks if it's a walkway
		if(grid[i][j].getDoorDirection() == DoorDirection.UP) {
			grid[i][j].addAdj(roomMap.get(grid[i-1][j].getInitial()).getCenterCell());
		}
		if(grid[i][j].getDoorDirection() == DoorDirection.DOWN) {
			grid[i][j].addAdj(roomMap.get(grid[i+1][j].getInitial()).getCenterCell());
		}
		if(grid[i][j].getDoorDirection() == DoorDirection.LEFT) {
			grid[i][j].addAdj(roomMap.get(grid[i][j-1].getInitial()).getCenterCell());
		}
		if(grid[i][j].getDoorDirection() == DoorDirection.RIGHT) {
			grid[i][j].addAdj(roomMap.get(grid[i][j+1].getInitial()).getCenterCell());
		}
	}
	private void calcWalkwayAdj(int i, int j) {
		//checks if the adj cell is a walkway
		if(i != 0 && grid[i-1][j].getInitial() == 'W') {
			grid[i][j].addAdj(grid[i-1][j]);
		}
		if(i != numRows-1 && grid[i+1][j].getInitial() == 'W') {
			grid[i][j].addAdj(grid[i+1][j]);
		}
		if(j != 0 && grid[i][j-1].getInitial() == 'W') {
			grid[i][j].addAdj(grid[i][j-1]);
		}
		if(j != numColumns-1 && grid[i][j+1].getInitial() == 'W') {
			grid[i][j].addAdj(grid[i][j+1]);
		}
	}
	//getter for cell in grid
	public BoardCell getCell(int i, int j) {
		return grid[i][j];
	}
}
