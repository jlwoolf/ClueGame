package clueGame;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.util.Set;

public class GameCardsPanel extends JPanel{
	private JPanel peopleHand;
	private JPanel peopleSeen;

	private JPanel roomHand;
	private JPanel roomSeen;

	private JPanel weaponHand;
	private JPanel weaponSeen;

	public GameCardsPanel() {
		setLayout(new GridLayout(3,0));
		setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		add(peoplePanel());
		add(roomPanel());
		add(weaponsPanel());
	}

	private JTextField none() {
		JTextField field = new JTextField("None");
		field.setBackground(Color.WHITE);
		field.setEditable(false);
		return field;
	}
	
	private JPanel peoplePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,0));
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));

		peopleHand = new JPanel();
		peopleHand.setLayout(new GridLayout(0,1));

		JLabel handLabel = new JLabel("In Hand: ");
		peopleHand.add(handLabel);
		panel.add(peopleHand);

		peopleSeen = new JPanel();
		peopleSeen.setLayout(new GridLayout(0,1));

		JLabel seenLabel = new JLabel("Seen: ");
		peopleSeen.add(seenLabel);
		panel.add(peopleSeen);

		return panel;
	}
	private JPanel roomPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,0));
		panel.setBorder(new TitledBorder(new EtchedBorder(), "People"));

		roomHand = new JPanel();
		roomHand.setLayout(new GridLayout(0,1));

		JLabel handLabel = new JLabel("In Hand: ");
		roomHand.add(handLabel);
		panel.add(roomHand);

		roomSeen = new JPanel();
		roomSeen.setLayout(new GridLayout(0,1));

		JLabel seenLabel = new JLabel("Seen: ");
		roomSeen.add(seenLabel);
		panel.add(roomSeen);

		return panel;
	}
	private JPanel weaponsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,0));
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));

		weaponHand = new JPanel();
		weaponHand.setLayout(new GridLayout(0,1));

		JLabel handLabel = new JLabel("In Hand: ");
		weaponHand.add(handLabel);
		panel.add(weaponHand);

		weaponSeen = new JPanel();
		weaponSeen.setLayout(new GridLayout(0,1));

		JLabel seenLabel = new JLabel("Seen: ");
		weaponSeen.add(seenLabel);
		panel.add(weaponSeen);

		return panel;

	}

	private void update() {

	}

	public void addSeenCard(Card card, Color color) {
		JTextField cardField = new JTextField(card.getCardName());
		cardField.setEditable(false);
		cardField.setBackground(color);
		switch (card.getCardType()) {
		case PERSON:
			peopleSeen.add(cardField);
			peopleSeen.revalidate();
			break;
		case ROOM:
			roomSeen.add(cardField);
			roomSeen.revalidate();
			break;
		case WEAPON:
			weaponSeen.add(cardField);
			weaponSeen.revalidate();
			break;
		default:
			break;
		}

	}

public void addHandCard(Card card, Color color) {		
	JTextField cardField = new JTextField(card.getCardName());
	cardField.setBackground(color);
	cardField.setEditable(false);
	switch (card.getCardType()) {
	case PERSON:
		peopleHand.add(cardField);
		peopleHand.revalidate();
		break;
	case ROOM:
		roomHand.add(cardField);
		roomHand.revalidate();
		break;
	case WEAPON:
		weaponHand.add(cardField);
		weaponHand.revalidate();
		break;
	default:
		break;
	}
}

public static void main(String[] args) {
	GameCardsPanel panel = new GameCardsPanel();  // create the panel
	JFrame frame = new JFrame();  // create the frame
	frame.setContentPane(panel); // put the panel in the frame
	frame.setSize(180, 750);  // size the frame
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
	frame.setVisible(true); // make it visible	

	panel.addHandCard(new Card("Test Card", CardType.PERSON), Color.WHITE);
	panel.addHandCard(new Card("Test Card", CardType.PERSON), Color.WHITE);
	
	panel.addHandCard(new Card("Test Card", CardType.ROOM), Color.WHITE);
	
	panel.addSeenCard(new Card("Test Card", CardType.ROOM), Color.CYAN);
	panel.addSeenCard(new Card("Test Card", CardType.WEAPON), Color.CYAN);
	panel.addSeenCard(new Card("Test Card", CardType.WEAPON), Color.CYAN);
	
	panel.addSeenCard(new Card("Test Card", CardType.PERSON), Color.YELLOW);
	panel.addSeenCard(new Card("Test Card", CardType.WEAPON), Color.YELLOW);
	
	panel.addSeenCard(new Card("Test Card", CardType.ROOM), Color.MAGENTA);

}
}
