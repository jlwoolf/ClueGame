package clueGame;

public class Card {
	String cardName;
	CardType cardType;
	
	public Card(String cardName, CardType cardType) {
		super();
		this.cardName = cardName;
		this.cardType = cardType;
	}

	public String getCardName() {
		return cardName;
	}
	public CardType getCardType() {
		return cardType;
	}
	boolean equals(Card card) {
		return true;
	}
}
