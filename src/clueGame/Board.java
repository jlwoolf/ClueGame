package clueGame;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import jdk.nashorn.internal.scripts.JO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.*;

public class Board extends JPanel{
	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private int width;
	private int height;

	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private Set<Integer[]> startingLocations;

	private Solution theAnswer;
	private Solution currentSuggestion;
	private Player[] players;
	private Color[] playerColors;
	private int humanPlayerIndex;
	private int currentPlayerIndex;
	private Set<Card> deck;
	private Set<Card> allCards;

	private Set<BoardCell> targets;
	private Set<BoardCell> visited;

	private int cellSize;
	private int[] wallPadding;
	private int diceRoll;

	private GameCardsPanel gameCardsPanel;
	private GameControlPanel gameControlPanel;

	//singleton design
	/***************************************************************************************************************************************************/
	private static final Board theInstance = new Board();
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

		addMouseListener(new BoardClick());

		calcAdj();
		createAnswer();
		fillPlayers();
		deal();
		rollDice();
		calcCurrentTargets();
	}

	//setters for the game cards and control panels for board manipulation
	public void setGameCardsPanel(GameCardsPanel gameCardsPanel) {
		this.gameCardsPanel = gameCardsPanel;
	}
	public void setGameControlPanel(GameControlPanel gameControlPanel) {
		this.gameControlPanel = gameControlPanel;
	}

	//defines the file location for setup and layout
	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		this.layoutConfigFile = "data/" + layoutConfigFile;
		this.setupConfigFile = "data/" + setupConfigFile;
	}

	//load setup config and check for errors
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException{
		Scanner fileReader = new Scanner(new File(setupConfigFile));
		roomMap = new HashMap<>();
		allCards = new HashSet<>();
		deck = new HashSet<>();
		//parse through file and add each room to roomMap
		while(fileReader.hasNext()) {
			String line = fileReader.nextLine();
			if(line.contains("//"))
				continue;

			//make sure only two types of spaces exist, Card and Other
			//throws error if otherwise
			String[] lineContents = line.split(", ");
			switch (lineContents[0]) {
				case "Room":
					allCards.add(new Card(lineContents[1], CardType.ROOM));
					roomMap.put(lineContents[2].charAt(0), new Room(lineContents[1]));
					break;
				case "Space":
					roomMap.put(lineContents[2].charAt(0), new Room(lineContents[1]));
					break;
				case "Weapon":
					allCards.add(new Card(lineContents[1], CardType.WEAPON));
					break;
				case "Person":
					allCards.add(new Card(lineContents[1], CardType.PERSON));
					break;
				default:
					throw new BadConfigFormatException("Setup contains more than the three card types and space type");
			}
		}
		fileReader.close();
		deck.addAll(allCards);
	}

	//load layout config and check for errors
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
		int cols;
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

	//getter function for a room of a specific cell
	public Room getRoom(char roomChar) {
		return roomMap.get(roomChar);
	}
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}

	//getter for a grid cell
	public BoardCell getCell(int i, int j) {
		return grid[i][j];
	}

	//getters and setters for the player array
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	public Player[] getPlayers() {
		return players;
	}

	//functions that return a set of cards by specifics
	//like the deck, all cards, people cards, room cards, and weapon cards
	public Set<Card> getDeck() {
		return deck;
	}
	public Set<Card> getCards() {
		return allCards;
	}
	public Set<Card> getPeopleCards() {
		Set<Card> peopleCards = new HashSet<>();

		for(Card card : allCards) {
			if(card.getCardType().equals(CardType.PERSON))
				peopleCards.add(card);
		}

		return peopleCards;
	}
	public Set<Card> getRoomCards() {
		Set<Card> roomCards = new HashSet<>();

		for(Card card : allCards) {
			if(card.getCardType().equals(CardType.ROOM))
				roomCards.add(card);
		}

		return roomCards;
	}
	public Set<Card> getWeaponCards() {
		Set<Card> weaponCards = new HashSet<>();

		for(Card card : allCards) {
			if(card.getCardType().equals(CardType.WEAPON))
				weaponCards.add(card);
		}

		return weaponCards;
	}

	//getter and setter for the answer
	public void setAnswer(Solution answer) {
		this.theAnswer = answer;
	}
	public Solution getAnswer() {
		return theAnswer;
	}

	//getters for the human player and current player
	public HumanPlayer getHumanPlayer() {
		return (HumanPlayer) players[humanPlayerIndex];
	}
	public Player getCurrentPlayer() {
		return players[currentPlayerIndex];
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

	//function that sets the colors for each player
	private void fillColors() {
		playerColors = new Color[6];
		playerColors[0] = new Color(255,255,255);
		playerColors[1] = new Color(193, 35, 180);
		playerColors[2] = new Color(118,169,4);
		playerColors[3] = new Color(80,173,207);
		playerColors[4] = new Color(244,1,0);
		playerColors[5] = new Color(245,237,16);
	}

	//function for filling in the players array
	private void fillPlayers() {
		fillColors();
		players = new Player[6];

		int i = 0;
		humanPlayerIndex = new Random().nextInt(6);
		currentPlayerIndex = humanPlayerIndex;
		for(Card card : getPeopleCards()) {
			if(i == humanPlayerIndex) {
				players[i] = new HumanPlayer(card.getCardName());
			} else {
				players[i] = new ComputerPlayer(card.getCardName());
			}
			i++;
		}

		int j = 0;
		while(startingLocations.size() > 0) {
			int startIt = new Random().nextInt(startingLocations.size());
			i = 0;
			for(Integer[] pair : startingLocations) {
				if(i == startIt) {
					players[j].setColor(playerColors[j]);
					players[j].setRow(pair[0]);
					players[j].setCol(pair[1]);
					grid[pair[0]][pair[1]].setOccupied(true);
					j++;
					startingLocations.remove(pair);
					break;
				}
				i++;
			}
		}
	}

	//function for dealing out the cards randomly but
	//as close to equal as possible
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
					card.setCardColor(players[playerIt].getColor());
					players[playerIt].updateHand(card);
					deck.remove(card);
					break;
				}
				i++;
			}
			j++;
		}
	}

	//function for checking if an accusation is right
	public boolean checkAccusation(Solution accusation) {
		return theAnswer.equals(accusation);
	}
	//function for handling a suggestion from a given player
	//giving the player a card for disproval or none if no disproval exists
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
		targets = recursiveTargets(startCell, pathLength);
	}
	public Set<BoardCell> getTargets() {
		return targets;
	}
	private Set<BoardCell> recursiveTargets(BoardCell startCell, int pathLength) {
		Set<BoardCell> targetSet = new HashSet<>();
		visited.add(startCell);
		for(BoardCell adjCell : startCell.getAdjList()) {
			if(visited.contains(adjCell)) {
				continue;
			}
			if(adjCell.isOccupied()) {
				if(adjCell.isRoom()) {
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
		targets = new HashSet<>();
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

	//helper function for calculating a rooms adjacencies
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
	//helper function for calculating a doorways adjacencies
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
	//helper function for calculating a walkway's adjacencies
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

	//functions for drawing the board
	/***************************************************************************************************************************************************/
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		cellSize = Math.min(getWidth()/numColumns, getHeight()/numRows);
		wallPadding = new int[]{(getWidth() - numColumns * cellSize) / 2, (getHeight() - numRows * cellSize) / 2};

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		drawCells(g);
		drawDoors(g);
		if(getHumanPlayer().isUnfinished()) {
			drawTargets(g);
		}
		drawRoomLabels(g);
		drawPlayers(g);

		updateCards();
	}

	//bunch of helper functions to make sure panels are drawn
	//and updated with movements and resizing
	private void updateCards() {
		for(Card card : getHumanPlayer().getHand()) {
			gameCardsPanel.addHandCard(card);
		}
		for(Card card : getHumanPlayer().getSeenCards()) {
			gameCardsPanel.addSeenCard(card);
		}
	}
	private void drawCells(Graphics g) {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				getCell(i,j).drawCell(g, cellSize, wallPadding);
			}
		}
	}
	private void drawDoors(Graphics g) {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				getCell(i,j).drawDoor(g, cellSize, wallPadding);
			}
		}
	}
	private void drawTargets(Graphics g) {
		if(currentPlayerIndex == humanPlayerIndex) {
			for (BoardCell target : targets) {
				target.drawTarget(g, cellSize, wallPadding);
			}
		}
	}
	private void drawRoomLabels(Graphics g) {
		for(Room room : roomMap.values()) {
			room.drawLabel(g, cellSize, wallPadding);
		}
	}
	private void drawPlayers(Graphics g) {
		for(Player player : players) {
			player.drawPlayer(g, cellSize, wallPadding, getCell(player.getRow(),player.getCol()).isRoom());
		}
	}

	//function for gameplay
	/***************************************************************************************************************************************************/
	//function to roll the dice
	public void rollDice() {
		diceRoll = new Random().nextInt(6) + 1 + new Random().nextInt(6) + 1;
	}
	//getter function for dice rolls
	public int getDiceRoll() {
		return diceRoll;
	}
	//helper function that increments the player by one
	public void incrementPlayer() {
		currentPlayerIndex++;
		if (currentPlayerIndex == players.length)
			currentPlayerIndex = 0;
	}
	//helper function that calculates the current players targets
	public void calcCurrentTargets() {
		calcTargets(getCell(getCurrentPlayer().getRow(),getCurrentPlayer().getCol()), diceRoll);
	}
	//function that handles what happens when next button is pressed
	public void clickNext() {
		if(getCurrentPlayer() instanceof HumanPlayer && getHumanPlayer().isUnfinished()) {
			createMessagePane("You haven't finished your move.");
		} else {
			incrementPlayer();
			rollDice();
			calcCurrentTargets();

			if(getCurrentPlayer() instanceof ComputerPlayer) {
				doComputerAccusation();
				doComputerMove();
				doComputerSuggestion();
			} else {
				getHumanPlayer().setUnfinished(true);
			}

			repaint();
		}
	}

	//moves computer players making sure to
	//update cell occupancy
	public void doComputerMove() {
		ComputerPlayer computerPlayer = (ComputerPlayer) getCurrentPlayer();
		BoardCell target = computerPlayer.selectTargets(targets);

		getCell(computerPlayer.getRow(),computerPlayer.getCol()).setOccupied(false);
		computerPlayer.setRow(target.getRow());
		computerPlayer.setCol(target.getCol());

		for(Player player : players) {
			getCell(player.getRow(),player.getCol()).setOccupied(true);
		}

		repaint();
	}
	//function that handles player movement making sure
	//to update cell occupancy
	public void doHumanMove(int row, int col) {
		HumanPlayer humanPlayer = (HumanPlayer) getCurrentPlayer();
		getCell(getCurrentPlayer().getRow(),getCurrentPlayer().getCol()).setOccupied(false);
		getCell(row,col).setOccupied(true);

		getCurrentPlayer().setRow(row);
		getCurrentPlayer().setCol(col);
		repaint();

		getHumanPlayer().setUnfinished(false);
	}
	//function that handles a human suggestion
	//current suggestion will only be null if cancel is pressed
	public void doHumanSuggestion() {
		createSuggestionPane();
		if(currentSuggestion != null) {
			Card suggestionResult = handleSuggestion(getHumanPlayer(), currentSuggestion);

			if (suggestionResult != null) {
				gameCardsPanel.addSeenCard(suggestionResult);
				gameControlPanel.setResult(suggestionResult.getCardName());
			} else {
				gameControlPanel.setResult("No player can disprove");
			}
		} else {
			gameControlPanel.setResult("");
			gameControlPanel.setGuess(currentSuggestion);
		}

	}
	//function that handles a computer players suggestion
	//checks if they are in a room and also checks whether their
	//accusation might be the answer. If it is, updates the
	//computer players accusation to not be null so they can
	//make an accusation next round
	public void doComputerSuggestion() {
		ComputerPlayer computerPlayer = (ComputerPlayer) getCurrentPlayer();
		if(getCell(getCurrentPlayer().getRow(),getCurrentPlayer().getCol()).isRoom()) {
			currentSuggestion = computerPlayer.createSuggestion(grid[getCurrentPlayer().getRow()][getCurrentPlayer().getCol()], getCards());

			Card suggestionResult = handleSuggestion(getCurrentPlayer(), currentSuggestion);

			if (suggestionResult != null) {
				computerPlayer.updateSeen(suggestionResult);
			} else if(computerPlayer.isCorrectSuggestion(currentSuggestion)) {
					computerPlayer.setAccusation(currentSuggestion);
			}
			gameControlPanel.setResult("");
			gameControlPanel.setGuess(currentSuggestion);
		} else {
			gameControlPanel.setResult("");
			gameControlPanel.setGuess(null);
		}
	}

	//function that handles a human accusation
	//current suggestion is only null if the player presses cancel
	public void doHumanAccusation() {
		createAccusationPane();
		if(currentSuggestion != null) {
			gameControlPanel.setGuess(currentSuggestion);
			if (checkAccusation(currentSuggestion)) {
				gameControlPanel.setResult("Correct accusation");
				createMessagePane("Congrats! You found the answer! You win!");
			} else {
				createMessagePane("Sorry... That's the wrong answer. You lose...");
				gameControlPanel.setResult("Wrong accusation");
			}
			System.exit(0);
		}
	}
	//function that determines whether a computer player should make an accusation
	//and whether, if they do, that accusation is right
	public void doComputerAccusation() {
		ComputerPlayer computerPlayer = (ComputerPlayer) getCurrentPlayer();
		if(computerPlayer.getAccusation() != null) {
			if (checkAccusation(computerPlayer.getAccusation())) {
				gameControlPanel.setGuess(computerPlayer.getAccusation());
				gameControlPanel.setResult("Correct accusation");
				createMessagePane("Sorry... Another player found the answer. You lose...");
				System.exit(0);
			} else {
				createMessagePane("Another player guessed the wrong answer and is out.");
			}
		}
	}

	//helper function for boardclick class
	private BoardCell clickTarget(int row, int col) {
		BoardCell clickedTarget = null;
		for(BoardCell target : targets) {
			if(target.getRow() == row && target.getCol() == col) {
				clickedTarget = target;
				break;
			}
		}
		return clickedTarget;
	}

	//handles player movement when clicked. Needs implementation for when player enters a room
	private class BoardClick implements MouseListener {
		@Override
		public void mouseReleased(MouseEvent e) {
			int row = (e.getY() - wallPadding[1])/cellSize;
			int col = (e.getX() - wallPadding[0])/cellSize;

			if(getCurrentPlayer() instanceof HumanPlayer && getHumanPlayer().isUnfinished()) {
				if(clickTarget(row, col) != null) {
					doHumanMove(row, col);
					if (getCell(getHumanPlayer().getRow(),getHumanPlayer().getCol()).isRoom()) {
						doHumanSuggestion();
					}
				} else {
					createMessagePane("This is not a valid target.");
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}

	//creates an accusation panel containing labels
	//for the room, person, and weapons cards and
	//comboboxes of every option so the player can make an accusation
	public void createAccusationPane() {
		JPanel accusationPanel = new JPanel();
		accusationPanel.setLayout(new GridLayout(3,3));

		JComboBox<Card> roomComboBox = createCardComboBox(CardType.ROOM);
		JComboBox<Card> personComboBox = createCardComboBox(CardType.PERSON);
		JComboBox<Card> weaponComboBox = createCardComboBox(CardType.WEAPON);

		accusationPanel.add(createCardLabel(CardType.ROOM));
		accusationPanel.add(roomComboBox);
		accusationPanel.add(createCardLabel(CardType.PERSON));
		accusationPanel.add(personComboBox);
		accusationPanel.add(createCardLabel(CardType.WEAPON));
		accusationPanel.add(weaponComboBox);

		JOptionPane accusationPane = new JOptionPane(accusationPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

		JPanel buttonPanel = (JPanel)accusationPane.getComponent(1);
		JButton okButton = (JButton)buttonPanel.getComponent(0);
		okButton.setFont(new Font("Label.font", Font.PLAIN, getHeight()/32));
		okButton.addActionListener(new OkButtonListener(roomComboBox,personComboBox,weaponComboBox));
		JButton cancelButton = (JButton)buttonPanel.getComponent(1);
		cancelButton.setFont(new Font("Label.font", Font.PLAIN, getHeight()/32));
		cancelButton.addActionListener(new CancelButtonListener());

		JDialog accusationDialog = accusationPane.createDialog(this, "Make a suggestion");
		accusationDialog.setVisible(true);
	}

	//creates a suggestion panel containing three labels
	//for the room, person, and weapon cards
	//and comboboxes containing the options to choose
	//roomComboBox is locked, only displaying the room the player is in
	//also has a cancel and
	public void createSuggestionPane() {
		JPanel suggestionPanel = new JPanel();
		suggestionPanel.setLayout(new GridLayout(3,3));

		JComboBox<Card> roomComboBox = new JComboBox();
		for(Card card : getRoomCards()) {
			if(getCell(getHumanPlayer().getRow(),getHumanPlayer().getCol()).getRoomName().equals(card.getCardName())) {
				roomComboBox.addItem(card);
			}
		}
		roomComboBox.setFont(new Font("Label.font", Font.PLAIN, getHeight()/32));
		roomComboBox.setRenderer(new CardListCellRenderer());

		JComboBox<Card> personComboBox = createCardComboBox(CardType.PERSON);
		JComboBox<Card> weaponComboBox = createCardComboBox(CardType.WEAPON);

		suggestionPanel.add(createCardLabel(CardType.ROOM));
		suggestionPanel.add(roomComboBox);
		suggestionPanel.add(createCardLabel(CardType.PERSON));
		suggestionPanel.add(personComboBox);
		suggestionPanel.add(createCardLabel(CardType.WEAPON));
		suggestionPanel.add(weaponComboBox);

		JOptionPane suggestionPane = new JOptionPane(suggestionPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

		JPanel buttonPanel = (JPanel)suggestionPane.getComponent(1);
		JButton okButton = (JButton)buttonPanel.getComponent(0);
		okButton.setFont(new Font("Label.font", Font.PLAIN, getHeight()/32));
		okButton.addActionListener(new OkButtonListener(roomComboBox,personComboBox,weaponComboBox));
		JButton cancelButton = (JButton)buttonPanel.getComponent(1);
		cancelButton.setFont(new Font("Label.font", Font.PLAIN, getHeight()/32));
		cancelButton.addActionListener(new CancelButtonListener());

		JDialog suggestionDialog = suggestionPane.createDialog(this, "Make a suggestion");
		suggestionDialog.setVisible(true);
	}

	//helper function to create a combobox of a given cardtype
	private JComboBox<Card> createCardComboBox(CardType cardType) {
		JComboBox<Card> comboBox = new JComboBox();
		for(Card card : getCards()) {
			if(card.getCardType().equals(cardType)) {
				comboBox.addItem(card);
			}
		}
		comboBox.setFont(new Font("Label.font", Font.PLAIN, getHeight()/32));
		comboBox.setRenderer(new CardListCellRenderer());
		return comboBox;
	}
	//helper function to create a label for a given cardtype
	private JLabel createCardLabel(CardType cardType) {
		JLabel roomLabel = new JLabel(cardType.toString());
		roomLabel.setFont(new Font("Label.font", Font.PLAIN, getHeight()/32));
		return roomLabel;
	}

	//action listener for the ok button for both suggestion
	//and accusation panels. creates a solution from the selected
	//options in the comboboxes
	private class OkButtonListener implements ActionListener {
		JComboBox<Card> personComboBox;
		JComboBox<Card> roomComboBox;
		JComboBox<Card> weaponComboBox;

		OkButtonListener(JComboBox<Card> roomComboBox,
						 JComboBox<Card> personComboBox,
						 JComboBox<Card> weaponComboBox) {
			this.personComboBox = personComboBox;
			this.roomComboBox = roomComboBox;
			this.weaponComboBox = weaponComboBox;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			currentSuggestion = new Solution(
					(Card) personComboBox.getSelectedItem(),
					(Card) roomComboBox.getSelectedItem(),
					(Card) weaponComboBox.getSelectedItem()
			);

		}
	}
	//action listener for the cancel button to make sure player is
	//not punished for clicking cancel
	private class CancelButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			currentSuggestion = null;
		}
	}

	//used to render the comboboxes of the suggestion
	//and accusation panels as the comboboxes contain
	//cards and not strings
	private class CardListCellRenderer extends DefaultListCellRenderer {

		public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus) {
			if (value instanceof Card) {
				value = ((Card)value).getCardName();
			}
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			return this;
		}
	}

	//used to make a simple message pane when necessary
	public void createMessagePane(String message) {
		JLabel optionPaneText = new JLabel("<html>"+ message +"</html>");
		optionPaneText.setHorizontalAlignment(SwingConstants.CENTER);
		optionPaneText.setFont(new Font("Label.font", Font.PLAIN, getHeight()/20));
		JOptionPane optionPane = new JOptionPane(optionPaneText, JOptionPane.PLAIN_MESSAGE);

		JPanel optionPanel = (JPanel)optionPane.getComponent(1);

		JButton optionButton = (JButton)optionPanel.getComponent(0);
		optionButton.setFont(new Font("Label.font", Font.PLAIN, getHeight()/20));
		optionButton.setPreferredSize(new Dimension(getWidth()/8, getHeight()/12));
		optionButton.validate();

		JDialog optionDialog = optionPane.createDialog(this,"");
		optionDialog.setVisible(true);
	}
}
