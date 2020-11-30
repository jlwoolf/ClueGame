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
		setSize(1280, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			setIconImage(ImageIO.read(new File("data/icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		//creates the gameboard panel
		gameBoard = Board.getInstance();
		gameBoard.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		gameBoard.initialize();

		//creates the control and card panel
		controlPanel = new GameControlPanel(gameBoard);
		cardPanel = new GameCardsPanel();
		controlPanel.setPreferredSize(new Dimension(getWidth(), getHeight()/6));
		cardPanel.setPreferredSize(new Dimension(getWidth()/8, getHeight()));

		//adds the panels to the frame
		add(cardPanel, BorderLayout.WEST);
		add(controlPanel, BorderLayout.EAST);
		add(gameBoard, BorderLayout.CENTER);

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

		JDialog optionDialog = optionPane.createDialog(this,"");
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
			controlPanel.setPreferredSize(new Dimension(getWidth()/6, getHeight()));
			cardPanel.setPreferredSize(new Dimension(getWidth()/6, getHeight()));
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			super.componentMoved(e);
			controlPanel.setPreferredSize(new Dimension(getWidth()/6, getHeight()));
			cardPanel.setPreferredSize(new Dimension(getWidth()/6, getHeight()));
		}
	}
}
