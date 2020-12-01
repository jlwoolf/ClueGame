package clueGame;

import java.awt.*;

public class Card {
	String cardName;
	CardType cardType;
	Color cardColor;

	//basic constructor
	public Card(String cardName, CardType cardType) {
		super();
		this.cardName = cardName;
		this.cardType = cardType;
	}

	//getters
	public String getCardName() {
		return cardName;
	}
	public CardType getCardType() {
		return cardType;
	}

	//equals function for card comparison
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Card))
			return false;
		
		Card card = (Card) obj;
		return this.cardName.equals(card.getCardName()) && this.cardType.equals(card.getCardType());
	}

	//hash code to make sorting cards using built in java functions easier
	@Override
	public final int hashCode() {
		int result = 19;
		if(cardName != null) {
			result = 31 * result + cardName.hashCode();
		}
		if(cardType != null) {
			result = 31 * result + cardType.hashCode();
		}
		return result;
	}

	//added a card color that corresponds with the player to
	//make coloring cards on the card panel easier
	public void setCardColor(Color cardColor) {
		this.cardColor = cardColor;
	}
	public Color getColor() {
		return cardColor;
	}
}
