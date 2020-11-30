package clueGame;

public class Card {
	String cardName;
	CardType cardType;

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
}
