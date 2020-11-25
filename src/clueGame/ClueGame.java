package clueGame;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

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
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//creates the control and card panel
		controlPanel = new GameControlPanel();
		cardPanel = new GameCardsPanel();
		controlPanel.setPreferredSize(new Dimension(getWidth(), getHeight()/6));
		cardPanel.setPreferredSize(new Dimension(getWidth()/8, getHeight()));

		//creates the gameboard panel
		gameBoard = Board.getInstance();
		gameBoard.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		gameBoard.initialize();

		//adds the panels to the frame
		add(controlPanel, BorderLayout.SOUTH);
		add(cardPanel, BorderLayout.EAST);
		add(gameBoard);

		addComponentListener(new ResizeListener());
	}

	//main function
	public static void main(String[] args) {
		ClueGame game = new ClueGame();
		game.setVisible(true);
	}

	class ResizeListener extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			super.componentResized(e);
			controlPanel.setPreferredSize(new Dimension(getWidth(), getHeight()/6));
			cardPanel.setPreferredSize(new Dimension(getWidth()/8, getHeight()));
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			super.componentMoved(e);
			controlPanel.setPreferredSize(new Dimension(getWidth(), getHeight()/6));
			cardPanel.setPreferredSize(new Dimension(getWidth()/8, getHeight()));
		}
	}
}
