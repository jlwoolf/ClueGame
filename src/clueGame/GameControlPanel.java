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
	
	public GameControlPanel() {
		setLayout(new GridLayout(2,0));
		add(topHalf());
		add(bottomHalf());
	}
	
	private JPanel topHalf() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,4));
		panel.add(turnPanel());
		panel.add(rollPanel());
		panel.add(accusationButton());
		panel.add(nextButton());
		return panel;
	}
	
	private JPanel bottomHalf() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel.add(guessPanel());
		panel.add(resultPanel());
		return panel;
	}
	
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
	
	private JPanel rollPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		
		JLabel rollLabel = new JLabel("Roll:", SwingConstants.RIGHT);
		panel.add(rollLabel);
		
		rollField = new JTextField();
		rollField.setEditable(false);
		panel.add(rollField);
		
		return panel;
	}
	
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
	
	private JPanel guessPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		panel.setLayout(new GridLayout(1,0));
		
		guessField = new JTextField();
		guessField.setEditable(false);
		panel.add(guessField);
		
		return panel;
	}
	private JPanel resultPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		panel.setLayout(new GridLayout(1,0));
		
		resultField = new JTextField();
		resultField.setEditable(false);
		panel.add(resultField);
		
		return panel;
	}
	
	public void setTurn(Player player) {
		turnField.setText(player.getName());
		turnField.setBackground(player.getColor());
	}
	
	public void setRoll(int roll) {
		rollField.setText(Integer.toString(roll));
	}
	
	public void setGuess(String guess) {
		guessField.setText(guess);
	}
	public void setResult(String result) {
		resultField.setText(result);
	}
	
	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();  // create the panel
        JFrame frame = new JFrame();  // create the frame
        frame.setContentPane(panel); // put the panel in the frame
        frame.setSize(750, 180);  // size the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
        frame.setVisible(true); // make it visible

        panel.setRoll(5);
        panel.setTurn(new ComputerPlayer("Some Cool Guy", 0, 0, new Color(66, 135, 245)));
        panel.setGuess("It was somebody, in some room, with some weapon!");
        panel.setResult("You were right!");
		
	}
}
