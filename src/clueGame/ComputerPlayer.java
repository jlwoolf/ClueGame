package clueGame;

import java.awt.Color;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player{

	//constructors for the computer player. Simply extensions from the Player class
	public ComputerPlayer(String name) {
		super(name);
	}
	public ComputerPlayer(String name, int row, int col, Color color) {
		super(name, row, col, color);
	}

	//function for computer player to create a suggestion from known cards
	public Solution createSuggestion(BoardCell room, Set<Card> allCards) {
		Card personCard = null;
		Card weaponCard = null;
		Card roomCard = null;
		
		Set<Card> personCards = new HashSet<>();
		Set<Card> weaponCards = new HashSet<>();
		Set<Card> roomCards = new HashSet<>();
		for(Card card : allCards) {
			if(card.getCardType() == CardType.PERSON) {
				personCards.add(card);
			}
			if(card.getCardType() == CardType.WEAPON) {
				weaponCards.add(card);
			}
			if(card.getCardType() == CardType.ROOM) {
				roomCards.add(card);
			}
		}

		personCard = getCard(personCards);

		weaponCard = getCard(weaponCards);

		while(roomCard == null) {
			for(Card card : roomCards) {
				if(room.getRoomName().equals(card.getCardName())) {
					roomCard = card;
					break;
				}
			}
		}
		
		return new Solution(personCard, roomCard, weaponCard);
	}

	//helper function for computer player to select a card from several unknowns
	private Card getCard(Set<Card> cards) {
		Card returnCard = null;
		while(returnCard == null) {
			int cardIt = new Random().nextInt(cards.size());
			int i = 0;

			for(Card card : cards) {
				if(i == cardIt) {
					if (!getSeenCards().contains(card)) {
						returnCard = card;
					}
					break;
				}
				i++;
			}
		}
		return returnCard;
	}

	//function for the computer player to pick its next target location
	public BoardCell selectTargets(Set<BoardCell> targets) {
		BoardCell returnTarget = null;
		for(BoardCell target : targets) {
			if(target.isRoom()) {
				if(!getSeenCards().contains(new Card(target.getRoomName(), CardType.ROOM))) {
					return target;
				}
			}
		}
		
		int targetIt = new Random().nextInt(targets.size());
		int i = 0;
		for(BoardCell target : targets) {
			if(i == targetIt) {
				returnTarget = target;
				break;
			}
			i++;
		}
		
		return returnTarget;
	}
}
