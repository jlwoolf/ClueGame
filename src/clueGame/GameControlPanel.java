package clueGame;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel{
	private TurnPanel turnPanel;
	private RollPanel rollPanel;
	private ButtonPanel buttonPanel;

	private GuessPanel guessPanel;
	private ResultPanel resultPanel;

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
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(0,2));

		JPanel left = new JPanel();
		left.setLayout(new GridLayout(0,2));
		turnPanel = new TurnPanel();
		rollPanel = new RollPanel();
		left.add(turnPanel);
		left.add(rollPanel);

		buttonPanel = new ButtonPanel();

		top.add(left);
		top.add(buttonPanel);
		return top;
	}

	//bottom half of the game control panel
	//contains the current guess
	//contains the result of a guess
	private JPanel bottomHalf() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));

		guessPanel = new GuessPanel();
		resultPanel = new ResultPanel();
		panel.add(guessPanel);
		panel.add(resultPanel);
		return panel;
	}

	//panel setup for the current turn to display player name and color
	private static class TurnPanel extends JPanel {
		private final JLabel turnLabel;
		private final JTextField turnField;

		public TurnPanel() {
			setLayout(new GridLayout(2, 0));

			turnLabel = new JLabel("Whose turn?", SwingConstants.CENTER);
			add(turnLabel);

			turnField = new JTextField();
			turnField.setEditable(false);
			add(turnField);
		}

		public void setTurn(Player player) {
			turnField.setText(player.getName());
			turnField.setBackground(player.getColor());
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			turnLabel.setFont(new Font("Label.font", Font.PLAIN, getWidth()/12));
			turnField.setFont(new Font("Label.font", Font.PLAIN, getWidth()/12));
		}
	}

	//panel setup for the current roll containing # of roll
	private static class RollPanel extends JPanel {
		private final JLabel rollLabel;
		private final JTextField rollField;

		public RollPanel() {
			setLayout(new GridLayout(0, 2));

			rollLabel = new JLabel("Roll:", SwingConstants.RIGHT);
			add(rollLabel);

			rollField = new JTextField();
			rollField.setEditable(false);
			rollField.setBackground(Color.WHITE);
			rollField.setHorizontalAlignment(SwingConstants.RIGHT);
			add(rollField);
		}

		public void setRoll(int roll) {
			rollField.setText(Integer.toString(roll));
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			rollLabel.setFont(new Font("Label.font", Font.PLAIN, getWidth()/12));
			rollField.setFont(new Font("Label.font", Font.PLAIN, getWidth()/12));
		}
	}

	private static class ButtonPanel extends JPanel {
		private final JButton accusationButton;
		private final JButton nextButton;

		public ButtonPanel() {
			setLayout(new GridLayout(0, 2));

			accusationButton = new JButton("Make Accusation");
			nextButton = new JButton("NEXT");

			add(accusationButton);
			add(nextButton);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			accusationButton.setFont(new Font("Label.font", Font.PLAIN, getWidth()/24));
			nextButton.setFont(new Font("Label.font", Font.PLAIN, getWidth()/24));
		}
	}

	private static class GuessPanel extends JPanel {
		private final JTextField guess;
		private final TitledBorder border;

		public GuessPanel() {
			border =new TitledBorder(new EtchedBorder(), "Guess");
			setBorder(border);
			setLayout(new GridLayout(0, 1));

			guess = new JTextField();
			guess.setEditable(false);
			guess.setHorizontalAlignment(SwingConstants.CENTER);
			guess.setBackground(Color.WHITE);

			add(guess);
		}

		public void setGuess(String guess) {
			this.guess.setText(guess);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			guess.setFont(new Font("Label.font", Font.PLAIN, getWidth()/24));
			border.setTitleFont(new Font("Label.font", Font.PLAIN, getWidth()/48));
		}
	}


	private static class ResultPanel extends JPanel {
		private final JTextField result;
		private final TitledBorder border;

		public ResultPanel() {
			border =new TitledBorder(new EtchedBorder(), "Result");
			setBorder(border);
			setLayout(new GridLayout(0, 1));

			result = new JTextField();
			result.setEditable(false);
			result.setHorizontalAlignment(SwingConstants.CENTER);
			result.setBackground(Color.WHITE);

			add(result);
		}

		public void setResult(String guess) {
			this.result.setText(guess);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			result.setFont(new Font("Label.font", Font.PLAIN, getWidth()/24));
			border.setTitleFont(new Font("Label.font", Font.PLAIN, getWidth()/48));
		}
	}

	//function to update the current player's turn data
	public void setTurn(Player player) {
		turnPanel.setTurn(player);
	}

	//function to update the number of rolls
	public void setRoll(int roll) {
		rollPanel.setRoll(roll);
	}

	//functions to update the guess and result panels text
	public void setGuess(String guess) {
		guessPanel.setGuess(guess);
	}
	public void setResult(String result) {
		resultPanel.setResult(result);
	}
}
