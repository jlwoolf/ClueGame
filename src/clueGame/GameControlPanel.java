package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel{
	JTextField rollField;
	JTextField turnField;
	JTextField guessField;
	JTextField resultField;

	//constructor to create the game control panel
	public GameControlPanel() {
		setLayout(new GridLayout(2,0));
		add(topHalf());
		add(bottomHalf());
	}

	//top half of the game control panel
	//contains panel for current player
	//contains panel for current roll
	//contains panel for accusation and next button
	private JPanel topHalf() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,4));
		panel.add(turnPanel());
		panel.add(rollPanel());
		panel.add(accusationButton());
		panel.add(nextButton());
		return panel;
	}

	//bottom half of the game control panel
	//contains the current guess
	//contains the result of a guess
	private JPanel bottomHalf() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel.add(guessPanel());
		panel.add(resultPanel());
		return panel;
	}

	//panel setup for the current turn to display player name and color
	private JPanel turnPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,0));
		
		JLabel turnLabel = new JLabel("Whose turn?", SwingConstants.CENTER);
		panel.add(turnLabel);

		turnField = new JTextField();
		turnField.setEditable(false);
		panel.add(turnField);
		
		return panel;
	}

	//panel setup for the current roll containing # of roll
	private JPanel rollPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		
		JLabel rollLabel = new JLabel("Roll:", SwingConstants.RIGHT);
		panel.add(rollLabel);
		
		rollField = new JTextField();
		rollField.setEditable(false);
		rollField.setBackground(Color.WHITE);
		panel.add(rollField);
		
		return panel;
	}

	//setup's for the two buttons
	private JButton accusationButton() {
		JButton button = new JButton();
		button.setText("Make Accusation");
		return button;
	}
	private JButton nextButton() {
		JButton button = new JButton();
		button.setText("NEXT!");
		return button;
	}

	//setup for the guess panel
	private JPanel guessPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		panel.setLayout(new GridLayout(1,0));
		
		guessField = new JTextField();
		guessField.setEditable(false);
		guessField.setBackground(Color.WHITE);
		panel.add(guessField);
		
		return panel;
	}
	//setup for the result panel
	private JPanel resultPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		panel.setLayout(new GridLayout(1,0));
		
		resultField = new JTextField();
		resultField.setEditable(false);
		resultField.setBackground(Color.WHITE);
		panel.add(resultField);
		
		return panel;
	}

	//function to update the current player's turn data
	public void setTurn(Player player) {
		turnField.setText(player.getName());
		turnField.setBackground(player.getColor());
	}

	//function to update the number of rolls
	public void setRoll(int roll) {
		rollField.setText(Integer.toString(roll));
	}

	//functions to update the guess and result panels text
	public void setGuess(String guess) {
		guessField.setText(guess);
	}
	public void setResult(String result) {
		resultField.setText(result);
	}
}
