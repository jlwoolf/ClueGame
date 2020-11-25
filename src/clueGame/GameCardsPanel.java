package clueGame;

import java.awt.*;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class GameCardsPanel extends JPanel{
	private Map<CardType, JPanel> hands;
	private Map<CardType, JPanel> seens;

	//constructor to create the the panel
	//adds different card panel for person, room, and weapons
	public GameCardsPanel() {
		hands = new HashMap<>();
		seens = new HashMap<>();

		setLayout(new GridLayout(3,0));
		setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		add(cardPanel(CardType.PERSON));
		add(cardPanel(CardType.ROOM));
		add(cardPanel(CardType.WEAPON));
	}

	//attempt to add a none card and remove it that I haven't quite figured out
	private JTextField none() {
		JTextField field = new JTextField("None");
		field.setBackground(Color.WHITE);
		field.setEditable(false);
		return field;
	}

	//creates a card panel for a given card type
	private JPanel cardPanel(CardType cardType) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
		panel.setBorder(new TitledBorder(new EtchedBorder(), cardType.toString()));

		JPanel hand = new JPanel();
		hand.setLayout(new GridLayout(0,1));

		JLabel handLabel = new JLabel("In Hand: ");

		hand.add(handLabel);
		panel.add(hand);

		JPanel seen = new JPanel();
		seen.setLayout(new GridLayout(0,1));

		JLabel seenLabel = new JLabel("Seen: ");
		seen.add(seenLabel);
		panel.add(seen);

		hands.put(cardType, hand);
		seens.put(cardType, seen);

		return panel;
	}

	//adds a seen card to panel
	public void addSeenCard(Card card, Color color) {
		JTextField cardField = new JTextField(card.getCardName());
		cardField.setEditable(false);
		cardField.setBackground(color);

		seens.get(card.cardType).add(cardField);
		revalidate();
		repaint();
	}

	//adds a hand card to panel
	public void addHandCard(Card card, Color color) {		
		JTextField cardField = new JTextField(card.getCardName());
		cardField.setBackground(color);
		cardField.setEditable(false);

		hands.get(card.cardType).add(cardField);
		revalidate();
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

	}
}
