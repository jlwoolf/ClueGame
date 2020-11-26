package clueGame;

import jdk.nashorn.internal.scripts.JO;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.html.Option;

public class ClueGame extends JFrame{
	private final GameCardsPanel cardPanel;
	private final GameControlPanel controlPanel;
	private final Board gameBoard;

	private JOptionPane optionPane;
	//constructor for the game
	public ClueGame() {
		//initializes the frame data
		setTitle("CLUE GAME");
		setSize(1920, 1080);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			setIconImage(ImageIO.read(new File("data/icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}

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
		add(cardPanel, BorderLayout.EAST);
		add(controlPanel, BorderLayout.SOUTH);
		add(gameBoard, BorderLayout.CENTER);


		for(Card card : gameBoard.getPeopleCards()) {
			int i = new Random().nextInt(3);
			if(i == 0)
				cardPanel.addHandCard(card, Color.CYAN);
			else if (i == 1)
				cardPanel.addHandCard(card, Color.RED);
			else
				cardPanel.addHandCard(card, Color.GREEN);
		}

		for(Card card : gameBoard.getRoomCards()) {
			int i = new Random().nextInt(3);
			if(i == 0)
				cardPanel.addHandCard(card, Color.CYAN);
			else if (i == 1)
				cardPanel.addHandCard(card, Color.RED);
			else
				cardPanel.addHandCard(card, Color.GREEN);
		}

		addComponentListener(new ResizeListener());


		createOptionPane();

	}

	public void createOptionPane() {
		JLabel optionPaneText = new JLabel("<html><center>You are a(n) " + gameBoard.getHumanPlayer().getName() +".<br/>Can you find the solution<br/>before the other players? </center></html>");
		optionPaneText.setFont(new Font("Label.font", Font.PLAIN, getHeight()/12));
		JOptionPane optionPane = new JOptionPane(optionPaneText, JOptionPane.PLAIN_MESSAGE);

		JPanel optionPanel = (JPanel)optionPane.getComponent(1);

		JButton optionButton = (JButton)optionPanel.getComponent(0);
		optionButton.setFont(new Font("Label.font", Font.PLAIN, getHeight()/12));
		optionButton.setPreferredSize(new Dimension(getWidth()/4, getHeight()/6));
		optionButton.validate();

		JDialog optionDialog = optionPane.createDialog(this,"URGENT");
		optionDialog.setVisible(true);
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
			cardPanel.setPreferredSize(new Dimension(getHeight()/4, getHeight()));
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			super.componentMoved(e);
			controlPanel.setPreferredSize(new Dimension(getWidth(), getHeight()/6));
			cardPanel.setPreferredSize(new Dimension(getHeight()/4, getHeight()));
		}
	}
}
