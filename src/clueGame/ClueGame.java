package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClueGame extends JFrame{
	
	final int WIDTH = 1280;
	final int HEIGHT = 720;
	
	private GameCardsPanel cardPanel;
	private GameControlPanel controlPanel;
	private Board gameBoard;

	//constructor for the game
	public ClueGame() {
		//initializes the frame data
		setTitle("CLUE GAME");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//creates the control and card panel
		controlPanel = new GameControlPanel();
		controlPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT/6));
		cardPanel = new GameCardsPanel();
		cardPanel.setPreferredSize(new Dimension(WIDTH/8, HEIGHT));
		cardPanel.addSeenCard(new Card("test", CardType.ROOM), Color.CYAN);

		//creates the gameboard panel
		gameBoard = Board.getInstance();
		gameBoard.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		gameBoard.initialize();

		//adds the panels to the frame
		add(controlPanel, BorderLayout.SOUTH);
		add(cardPanel, BorderLayout.EAST);
		add(gameBoard);
	}

	//main function
	public static void main(String[] args) {
		ClueGame game = new ClueGame();
		game.setVisible(true);
	}
}
