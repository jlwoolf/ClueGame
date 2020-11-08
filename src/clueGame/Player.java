package clueGame;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public abstract class Player {
		private String name;
		private Color color;
		
		private Set<Card> hand;
		private Set<Card> seenCards;

		private int row;
		private int col;
		
		public Player(String name) {
			super();
			this.name = name;
		}
		public void updateHand(Card card) {
			this.hand.add(card);
		}
		public void updateSeen(Card seenCard) {
			this.seenCards.add(seenCard);
		}
		public Card disproveSuggestion(Solution solution) {
			return null;
		}
		
		public String getName() {
			return name;
		}

		public Color getColor() {
			return color;
		}

		public void setHand(Set<Card> hand) {
			this.hand = hand;
		}
		public void clearHand() {
			this.hand = new HashSet<Card>();
		}
		public Set<Card> getHand() {
			return hand;
		}
		
		public void setSeenCards(Set<Card> seenCards) {
			this.seenCards = seenCards;
		}
		public void clearSeenCards() {
			this.seenCards = new HashSet<Card>();
		}
		public Set<Card> getSeenCards() {
			return seenCards;
		}

		public void setRow(int row) {
			this.row = row;
		}
		public int getRow() {
			return row;
		}

		public void setCol(int col) {
			this.col = col;
		}
		public int getCol() {
			return col;
		}
}
