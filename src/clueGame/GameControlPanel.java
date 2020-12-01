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

	private JTextField guessPerson;
	private JTextField guessRoom;
	private JTextField guessWeapon;
	private TitledBorder guessBorder;

	private JTextField result;
	private TitledBorder resultBorder;

	private JButton accusationButton;
	private JButton nextButton;

	private final Board board;

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

	//top portion of the game control panel
	//contains panel for current player
	//contains panel for current roll
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

	//middle portion of the game control panel
	//contains the current guess
	//contains the result of a guess
	private JPanel middle() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,0));

		JPanel guessPanel = new JPanel();
		guessBorder =new TitledBorder(new EtchedBorder(), "Guess");
		guessPanel.setBorder(guessBorder);
		guessPanel.setLayout(new GridLayout(3, 0));

		guessPerson = new JTextField();
		guessPerson.setEditable(false);
		guessPerson.setHorizontalAlignment(SwingConstants.CENTER);
		guessPerson.setBackground(Color.WHITE);

		guessWeapon = new JTextField();
		guessWeapon.setEditable(false);
		guessWeapon.setHorizontalAlignment(SwingConstants.CENTER);
		guessWeapon.setBackground(Color.WHITE);

		guessRoom = new JTextField();
		guessRoom.setEditable(false);
		guessRoom.setHorizontalAlignment(SwingConstants.CENTER);
		guessRoom.setBackground(Color.WHITE);

		guessPanel.add(guessPerson);
		guessPanel.add(guessRoom);
		guessPanel.add(guessWeapon);

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

	//bottom portion of the game control panel
	//contains the accusation and next buttons
	private JPanel bottom() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 0));

		accusationButton = new JButton("Make Accusation");
		accusationButton.addActionListener(new AccusationListener());
		nextButton = new JButton("NEXT");
		nextButton.addActionListener(new NextListener());

		panel.add(accusationButton);
		panel.add(nextButton);
		return panel;
	}

	//action listener for the next button
	private class NextListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			board.clickNext();
			setTurn(board.getCurrentPlayer());
			setRoll(board.getDiceRoll());
		}
	}
	//action listener for the make accusation button
	private class AccusationListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(board.getHumanPlayer().isUnfinished()) {
				board.doHumanAccusation();
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		turnLabel.setFont(new Font("Label.font", Font.PLAIN, getWidth()/12));
		turnField.setFont(new Font("Label.font", Font.PLAIN, getWidth()/12));

		rollLabel.setFont(new Font("Label.font", Font.PLAIN, getWidth()/12));
		rollField.setFont(new Font("Label.font", Font.PLAIN, getWidth()/12));

		guessPerson.setFont(new Font("Label.font", Font.PLAIN, getWidth()/20));
		guessRoom.setFont(new Font("Label.font", Font.PLAIN, getWidth()/20));
		guessWeapon.setFont(new Font("Label.font", Font.PLAIN, getWidth()/20));
		guessBorder.setTitleFont(new Font("Label.font", Font.PLAIN, getWidth()/12));

		result.setFont(new Font("Label.font", Font.PLAIN, getWidth()/20));
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
	//decided to split the guess into three seperate
	//text fields as it looks nicer
	public void setGuess(Solution guess) {
		if(guess == null) {
			this.guessPerson.setText("");
			this.guessRoom.setText("");
			this.guessWeapon.setText("");
		} else {
			this.guessPerson.setText(guess.getPerson().getCardName());
			this.guessRoom.setText(guess.getRoom().getCardName());
			this.guessWeapon.setText(guess.getWeapon().getCardName());
		}
	}
	//function to update the text for the result
	public void setResult(String guess) {
		this.result.setText(guess);
	}

}
