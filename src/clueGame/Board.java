package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Board {
	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;

	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private Set<Integer[]> startingLocations;

	private Solution theAnswer;
	private Player[] players;
	private Set<Card> deck;
	private Set<Card> allCards;

	private Set<BoardCell> targets;
	private Set<BoardCell> visited;

	//singleton design
	/***************************************************************************************************************************************************/
	private static Board theInstance = new Board();
	private Board() {
		super();
	}
	public static Board getInstance() {
		return theInstance;
	}

	//initialization and initialization methods
	/***************************************************************************************************************************************************/
	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (FileNotFoundException | BadConfigFormatException e) {
			e.printStackTrace();
		}

		calcAdj();
		createAnswer();
		fillPlayers();
	}

	//defines the file location for setup and layout
	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		this.layoutConfigFile = "data/" + layoutConfigFile;
		this.setupConfigFile = "data/" + setupConfigFile;
	}

	//load setup config and check for errors
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException{
		Scanner fileReader = new Scanner(new File(setupConfigFile));
		roomMap = new HashMap<Character, Room>();
		allCards = new HashSet<Card>();
		deck = new HashSet<Card>();
		//parse through file and add each room to roomMap
		while(fileReader.hasNext()) {
			String line = fileReader.nextLine();
			if(line.contains("//"))
				continue;

			//make sure only two types of spaces exist, Card and Other
			//throws error if otherwise
			String[] lineContents = line.split(", ");
			if(lineContents[0].equals("Room")  ) {
				allCards.add(new Card(lineContents[1], CardType.ROOM));
				roomMap.put(lineContents[2].charAt(0), new Room(lineContents[1]));
			} else if (lineContents[0].equals("Space")) {
				roomMap.put(lineContents[2].charAt(0), new Room(lineContents[1]));
			} else if (lineContents[0].equals("Weapon")) {
				allCards.add(new Card(lineContents[1], CardType.WEAPON));
			} else if(lineContents[0].equals("Person")) {
				allCards.add(new Card(lineContents[1], CardType.PERSON));
			} else {
				throw new BadConfigFormatException("Setup contains more than the three card types and space type");
			}
		}
		fileReader.close();
		for(Card card : allCards) {
			deck.add(card);
		}
	}

	//load layout config and check for errors
	@SuppressWarnings("resource")
	public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException {
		//get number of rows and columns to initialze the grid
		File file = new File(layoutConfigFile);
		Scanner fileReader = new Scanner(file);
		setRowsColumns(fileReader);
		grid = new BoardCell[numRows][numColumns];
		startingLocations = new HashSet<>();
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
					throw new BadConfigFormatException("Layout contains a room not specified in the Setup file");
				} else {
					//update grid cell with proper infromation from file
					grid[i][j] = new BoardCell(i, j, lineContents[j].charAt(0));
					if(lineContents[j].contains("*")) {
						grid[i][j].setRoomCenter(true);
						grid[i][j].setRoomName(roomMap.get(lineContents[j].charAt(0)).getName());
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
					if(lineContents[j].contains("%")) {
						Integer[] location = {i,j};
						startingLocations.add(location);
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
				throw new BadConfigFormatException("Different number of rows and columns in Layout File");

			rows++;
		}
		numRows = rows;
	}

	//getters and setter functions
	/***************************************************************************************************************************************************/
	public int getNumRows() {
		return numRows;
	}
	public int getNumColumns() {
		return numColumns;
	}

	public Room getRoom(char roomChar) {
		return roomMap.get(roomChar);
	}
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}

	public BoardCell getCell(int i, int j) {
		return grid[i][j];
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}
	public Player[] getPlayers() {
		return players;
	}

	public Set<Card> getDeck() {
		return deck;
	}
	public Set<Card> getCards() {
		return allCards;
	}
	public Set<Card> getPeopleCards() {
		Set<Card> peopleCards = new HashSet<Card>();

		for(Card card : allCards) {
			if(card.getCardType().equals(CardType.PERSON))
				peopleCards.add(card);
		}

		return peopleCards;
	}
	public Set<Card> getRoomCards() {
		Set<Card> roomCards = new HashSet<Card>();

		for(Card card : allCards) {
			if(card.getCardType().equals(CardType.ROOM))
				roomCards.add(card);
		}

		return roomCards;
	}
	public Set<Card> getWeaponCards() {
		Set<Card> weaponCards = new HashSet<Card>();

		for(Card card : allCards) {
			if(card.getCardType().equals(CardType.WEAPON))
				weaponCards.add(card);
		}

		return weaponCards;
	}

	public void setAnswer(Solution answer) {
		this.theAnswer = answer;
	}
	public Solution getAnswer() {
		return theAnswer;
	}

	//functions for card handling
	/***************************************************************************************************************************************************/
	private void createAnswer() {
		Card weapon = null, person = null, room = null;
		int weaponIt = new Random().nextInt(this.getWeaponCards().size());
		int peopleIt = new Random().nextInt(this.getPeopleCards().size());
		int roomIt = new Random().nextInt(this.getRoomCards().size());
		
		int i = 0;
		for(Card card : this.getWeaponCards()) {
			if(i == weaponIt) {
				weapon = card;
				this.deck.remove(card);
				break;
			}
			i++;
		}
		i = 0;
		for(Card card : this.getPeopleCards()) {
			if(i == peopleIt) {
				person = card;
				this.deck.remove(card);
				break;
			}
			i++;
		}
		i = 0;
		for(Card card : this.getRoomCards()) {
			if(i == roomIt) {
				room = card;
				this.deck.remove(card);
				break;
			}
			i++;
		}
		
		theAnswer = new Solution(person, room, weapon);
	}
	
	private void fillPlayers() {
		players = new Player[6];
		players[0] = new HumanPlayer("Player 1");
		
		for(int i = 1; i < 6; i++) {
			players[i] = new ComputerPlayer("CPU " + i);
		}
		
		int j = 0;
		while(startingLocations.size() > 0) {
			int startIt = new Random().nextInt(startingLocations.size());
			int i = 0;
			for(Integer[] pair : startingLocations) {
				if(i == startIt) {
					players[j].setRow(pair[0]);
					players[j].setCol(pair[1]);
					j++;
					startingLocations.remove(pair);
					break;
				}
				i++;
			}
		}
	}
	
	public void deal() {
		int j = 0;
		while(deck.size() > 0) {
			int handSize = j / 6;
			int playerIt = new Random().nextInt(players.length);
			while(players[playerIt].getHand().size() > handSize) {
				playerIt = new Random().nextInt(players.length);
			}
			
			int it = new Random().nextInt(deck.size());
			int i = 0;
			for(Card card : deck) {
				if(i == it) {
					players[playerIt].updateHand(card);
					deck.remove(card);
					break;
				}
				i++;
			}
			j++;
		}
	}
	public boolean checkAccusation(Solution accusation) {
		if(theAnswer.equals(accusation)) {
			return true;
		} else {
			return false;
		}
	}
	public Card handleSuggestion(Player suggestingPlayer, Solution suggestion) {
		Card returnCard = null;
		for(Player player : players) {
			if(player.getName().equals(suggestingPlayer.getName()))
				continue;
			
			if(returnCard == null) {
				returnCard = player.disproveSuggestion(suggestion);
			}
		}
		return returnCard;
	}

	//functions for calculating  and getting targets
	/***************************************************************************************************************************************************/
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
				if(adjCell.isOccupied() && adjCell.isRoom()) {
					targetSet.add(adjCell);
				}
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

	//functions for calculating and getting adjacency
	/***************************************************************************************************************************************************/
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
				}
				if (grid[i][j].isDoorway()) {
					calcDoorwayAdj(i, j);
				}
				if (grid[i][j].getInitial() == 'W') {
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
}
