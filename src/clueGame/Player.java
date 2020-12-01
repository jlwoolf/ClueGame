package clueGame;

import java.awt.*;
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

	private int randomPaddingX;
	private int randomPaddingY;
	private boolean inRoom;

	//constructors
	public Player(String name) {
		super();
		this.name = name;

		this.hand = new HashSet();
		this.seenCards = new HashSet();
	}
	public Player(String name, int row, int col, Color color) {
		super();
		this.name = name;
		this.row = row;
		this.col = col;
		this.color = color;

		this.hand = new HashSet();
		this.seenCards = new HashSet();
	}

	//functions that add a card to a players hand or seen cards
	public void updateHand(Card card) {
		this.hand.add(card);
	}
	public void updateSeen(Card seenCard) {
		this.seenCards.add(seenCard);
	}

	//function for a player to disprove a suggestion
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

	//getters and setters for the name and color
	public String getName() {
		return name;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Color getColor() {
		return color;
	}

	//getters and setters for the hand and seen cards sets
	public void setHand(Set<Card> hand) {
		this.hand = hand;
	}
	public void clearHand() {
		this.hand = new HashSet();
	}
	public Set<Card> getHand() {
		return hand;
	}
	public void setSeenCards(Set<Card> seenCards) {
		this.seenCards = seenCards;
	}
	public void clearSeenCards() {
		this.seenCards = new HashSet();
	}
	public Set<Card> getSeenCards() {
		return seenCards;
	}

	//getters and setters for the row and col
	//positions of a player
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

	//function for drawing the player
	public void drawPlayer(Graphics g, int size, int[] wallPadding, boolean inRoom) {
		if(!this.inRoom && inRoom && !(this instanceof HumanPlayer)) {
			randomPaddingX = new Random().nextInt(size);
			randomPaddingY = new Random().nextInt(size);
			this.inRoom = true;
		} else if(this.inRoom && !inRoom) {
			randomPaddingX = 0;
			randomPaddingY = 0;
			this.inRoom = false;
		}

		g.setColor(color);
		g.fillOval(col * size + wallPadding[0]+randomPaddingX, row * size + wallPadding[1]+randomPaddingY, size, size);
	}
}
