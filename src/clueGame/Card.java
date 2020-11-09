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
	@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		if(!(obj instanceof Card))
			return false;
		
		Card card = (Card) obj;
		if(this.cardName.equals(card.getCardName()) && this.cardType.equals(card.getCardType())) {
			return true;
		} else {
			return false;
		}
	}
	
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
