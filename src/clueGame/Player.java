package clueGame;

import java.awt.Color;
import java.util.Set;

public abstract class Player {
		private String name;
		private Color color;
		
		private Set<Card> hand;

		private int row;
		private int col;
		
		abstract void updateHand(Card card);
		
		public Player(String name) {
			super();
			this.name = name;
		}
		
		public String getName() {
			return name;
		}

		public Color getColor() {
			return color;
		}

		public Set<Card> getHand() {
			return hand;
		}

		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}
}
