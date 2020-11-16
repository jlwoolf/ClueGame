package clueGame;

import java.awt.Color;
import java.util.HashSet;
import java.util.Random;
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
			
			this.hand = new HashSet<Card>();
			this.seenCards = new HashSet<Card>();
		}
		
		public Player(String name, int row, int col, Color color) {
			super();
			this.name = name;
			this.row = row;
			this.col = col;
			this.color = color;
			
			this.hand = new HashSet<Card>();
			this.seenCards = new HashSet<Card>();
		}
		public void updateHand(Card card) {
			this.hand.add(card);
			this.seenCards.add(card);
		}
		public void updateSeen(Card seenCard) {
			this.seenCards.add(seenCard);
		}
		public Card disproveSuggestion(Solution solution) {
			Set<Card> disproveCards = new HashSet<>();
			Card returnCard = null;
			if(hand.contains(solution.getWeapon())) {
				disproveCards.add(solution.getWeapon());
			}
			if(hand.contains(solution.getRoom())) {
				disproveCards.add(solution.getRoom());
			}
			if(hand.contains(solution.getPerson())) {
				disproveCards.add(solution.getPerson());
			}
			
			if(disproveCards.size() == 0) {
				return returnCard;
			}
			
			int disproveIt = new Random().nextInt(disproveCards.size());
			int i = 0;
			for(Card card : disproveCards) {
				if(i == disproveIt) {
					returnCard = card;
					break;
				}
				i++;
			}
			
			return returnCard;
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
