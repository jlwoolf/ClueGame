package clueGame;

public class Card {
	String cardName;
	CardType cardType;
	
	public Card(String cardName, CardType cardType) {
		super();
		this.cardName = cardName;
		this.cardType = cardType;
	}

	boolean equals(Card card) {
		return true;
	}
}
