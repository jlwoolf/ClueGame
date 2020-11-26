package clueGame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

//this class exists mostly to make painting components more intelligent and structured
public class CardPanel extends JPanel {
    private final JLabel handLabel;
    private final JLabel seenLabel;
    private final TitledBorder border;

    private final CardList hand;
    private final CardList seen;

    private final CardType cardPanelType;
    private int numCards;

    public CardPanel(CardType cardType) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        handLabel = new JLabel("In Hand:");
        seenLabel = new JLabel("Seen:");
        border = new TitledBorder(new EtchedBorder(), cardType.name());

        cardPanelType = cardType;
        numCards = 2;

        hand = new CardList();
        seen = new CardList();

        add(handLabel);
        add(hand);
        add(seenLabel);
        add(seen);
        setBorder(border);
    }

    public void addSeenCard(Card card, Color color) {
        seen.addCard(card, color);
        numCards++;
        seen.updateCardList();
    }
    public void addHandCard(Card card, Color color) {
        hand.addCard(card, color);
        numCards++;
        hand.updateCardList();
    }

    public CardType getCardPanelType() {
        return cardPanelType;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        handLabel.setFont(new Font("Label.font", Font.PLAIN, getWidth()/16));
        seenLabel.setFont(new Font("Label.font", Font.PLAIN, getWidth()/16));
        border.setTitleFont(new Font("Label.font", Font.PLAIN, getWidth()/12));

        hand.paintComponent(g, getHeight()/numCards, getHeight()/24);
        seen.paintComponent(g, getHeight()/numCards, getHeight()/24);
    }
}
