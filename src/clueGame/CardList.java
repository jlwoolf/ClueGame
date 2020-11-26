package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class CardList extends JPanel{
    private final Set<JTextField> cardList;
    private boolean newPanel = true;

    public CardList() {
        cardList = new TreeSet<>(new SortByColor());
        setLayout(new GridLayout(1,0));

        JTextField none = none();
        cardList.add(none);
        add(none);
    }

    private JTextField none() {
        JTextField none = new JTextField("None");
        none.setBackground(Color.WHITE);
        none.setEditable(false);
        none.setHorizontalAlignment(SwingConstants.CENTER);
        return none;
    }

    private class SortByColor implements Comparator<JTextField> {
        @Override
        public int compare(JTextField j1, JTextField j2) {
            Color rgb1 = j1.getBackground();
            Color rgb2 = j2.getBackground();
            int o1 = rgb1.getRed()*31 + rgb1.getBlue()*17 + rgb1.getGreen() + StringToInt(j1.getText());
            int o2 = rgb2.getRed()*31 + rgb2.getBlue()*17 + rgb2.getGreen() + StringToInt(j2.getText());

            return o1 - o2;
        }
    }

    private int StringToInt(String string) {
        int sum = 0;
        for(Character c : string.toCharArray()) {
            sum += c;
        }
        return sum;
    }

    public void addCard(Card card, Color color) {
        if(newPanel) {
            cardList.clear();
            removeAll();
            newPanel = false;
        }

        JTextField cardField = new JTextField(card.getCardName());
        cardField.setBackground(color);
        cardField.setEditable(false);
        cardField.setHorizontalAlignment(SwingConstants.CENTER);
        cardList.add(cardField);
    }

    public void updateCardList() {
        removeAll();
        setLayout(new GridLayout(cardList.size(), 0));
        for(JTextField cardField : cardList) {
            add(cardField);
        }
    }

    public void paintComponent(Graphics g, int boxSize, int fontSize) {
        super.paintComponent(g);
        for(JTextField cardField : cardList) {
            cardField.setFont(new Font("Label.font", Font.PLAIN, fontSize));
        }
    }
}
