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

import java.util.*;

public class GameCardsPanel extends JPanel{
	private final CardPanel[] cardPanels;
	private final TitledBorder border;

	//constructor to create the the panel
	//adds different card panel for person, room, and weapons
	public GameCardsPanel() {
		setLayout(new GridLayout(3,0));

		border = new TitledBorder(new EtchedBorder(), "Known Cards");
		cardPanels = new CardPanel[]{
				new CardPanel(CardType.PERSON),
				new CardPanel(CardType.ROOM),
				new CardPanel(CardType.WEAPON)
		};

		setBorder(border);
		for(CardPanel cardPanel : cardPanels) {
			add(cardPanel);
		}
	}

	public void addHandCard(Card card, Color color) {
		for(CardPanel cardPanel : cardPanels) {
			if(card.getCardType().equals(cardPanel.getCardPanelType())) {
				cardPanel.addHandCard(card, color);
			}
		}
	}
	public void addSeenCard(Card card, Color color) {
		for(CardPanel cardPanel : cardPanels) {
			if(card.getCardType().equals(cardPanel.getCardPanelType())) {
				cardPanel.addSeenCard(card, color);
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		border.setTitleFont(new Font("Label.font", Font.PLAIN, getWidth()/16));
	}
}
