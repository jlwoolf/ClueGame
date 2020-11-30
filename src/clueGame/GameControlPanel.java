package clueGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel{
	private JLabel turnLabel;
	private JTextField turnField;

	private JLabel rollLabel;
	private JTextField rollField;

	private JTextField guess;
	private TitledBorder guessBorder;

	private JTextField result;
	private TitledBorder resultBorder;

	private JButton accusationButton;
	private JButton nextButton;

	private Board board;

	//constructor to create the game control panel
	public GameControlPanel(Board parentBoard) {
		this.board = parentBoard;
		setLayout(new GridLayout(3,0));
		add(top());
		add(middle());
		add(bottom());

		setTurn(board.getCurrentPlayer());
		setRoll(board.getDiceRoll());
	}

	//top half of the game control panel
	//contains panel for current player
	//contains panel for current roll
	//contains panel for accusation and next button
	private JPanel top() {
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(2,0));

		JPanel turnPanel = new JPanel();
		turnPanel.setLayout(new GridLayout(2, 0));
		turnLabel = new JLabel("Whose turn?", SwingConstants.CENTER);
		turnPanel.add(turnLabel);

		turnField = new JTextField();
		turnField.setEditable(false);
		turnField.setHorizontalAlignment(SwingConstants.CENTER);
		turnPanel.add(turnField);


		JPanel rollPanel = new JPanel();
		rollPanel.setLayout(new GridLayout(0, 2));
		rollLabel = new JLabel("Roll:", SwingConstants.RIGHT);
		rollPanel.add(rollLabel);

		rollField = new JTextField();
		rollField.setEditable(false);
		rollField.setBackground(Color.WHITE);
		rollField.setHorizontalAlignment(SwingConstants.RIGHT);
		rollPanel.add(rollField);

		top.add(turnPanel);
		top.add(rollPanel);
		return top;
	}

	//bottom half of the game control panel
	//contains the current guess
	//contains the result of a guess
	private JPanel middle() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,0));

		JPanel guessPanel = new JPanel();
		guessBorder =new TitledBorder(new EtchedBorder(), "Guess");
		guessPanel.setBorder(guessBorder);
		guessPanel.setLayout(new GridLayout(1, 0));

		guess = new JTextField();
		guess.setEditable(false);
		guess.setHorizontalAlignment(SwingConstants.CENTER);
		guess.setBackground(Color.WHITE);
		guessPanel.add(guess);

		JPanel resultPanel = new JPanel();
		resultBorder =new TitledBorder(new EtchedBorder(), "Result");
		resultPanel.setBorder(resultBorder);
		resultPanel.setLayout(new GridLayout(1, 0));

		result = new JTextField();
		result.setEditable(false);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		result.setBackground(Color.WHITE);
		resultPanel.add(result);

		panel.add(guessPanel);
		panel.add(resultPanel);
		return panel;
	}

	private JPanel bottom() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 0));

		accusationButton = new JButton("Make Accusation");
		nextButton = new JButton("NEXT");
		nextButton.addActionListener(new NextListener());

		panel.add(accusationButton);
		panel.add(nextButton);
		return panel;
	}

	private class NextListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			board.next();
			setTurn(board.getCurrentPlayer());
			setRoll(board.getDiceRoll());
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		turnLabel.setFont(new Font("Label.font", Font.PLAIN, getWidth()/12));
		turnField.setFont(new Font("Label.font", Font.PLAIN, getWidth()/12));

		rollLabel.setFont(new Font("Label.font", Font.PLAIN, getWidth()/12));
		rollField.setFont(new Font("Label.font", Font.PLAIN, getWidth()/12));

		guess.setFont(new Font("Label.font", Font.PLAIN, getWidth()/8));
		guessBorder.setTitleFont(new Font("Label.font", Font.PLAIN, getWidth()/12));

		result.setFont(new Font("Label.font", Font.PLAIN, getWidth()/8));
		resultBorder.setTitleFont(new Font("Label.font", Font.PLAIN, getWidth()/12));

		accusationButton.setFont(new Font("Label.font", Font.PLAIN, getWidth()/12));
		nextButton.setFont(new Font("Label.font", Font.PLAIN, getWidth()/12));
	}

	//function to update the text for the current player
	public void setTurn(Player player) {
		turnField.setText(player.getName());
		turnField.setBackground(player.getColor());
	}
	//function to update the number of rolls
	public void setRoll(int roll) {
		rollField.setText(Integer.toString(roll));
	}

	//function to update the text for the guess
	public void setGuess(String guess) {
		this.guess.setText(guess);
	}
	//function to update the text for the result
	public void setResult(String guess) {
		this.result.setText(guess);
	}

}
