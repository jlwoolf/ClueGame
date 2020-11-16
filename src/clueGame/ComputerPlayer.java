package clueGame;

import java.awt.Color;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player{

	public ComputerPlayer(String name) {
		super(name);
	}
	
	public ComputerPlayer(String name, int row, int col, Color color) {
		super(name, row, col, color);
	}

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
		
		while(personCard == null) {
			int cardIt = new Random().nextInt(personCards.size());
			int i = 0;
			
			for(Card card : personCards) {
				if(i == cardIt) {
					if(getSeenCards().contains(card)) {
						break;
					} else {
						personCard = card;
						break;
					}
				}
				i++;
			}
		}
		
		while(weaponCard == null) {
			int cardIt = new Random().nextInt(weaponCards.size());
			int i = 0;
			
			for(Card card : weaponCards) {
				if(i == cardIt) {
					if(getSeenCards().contains(card)) {
						break;
					} else {
						weaponCard = card;
						break;
					}
				}
				i++;
			}
		}
		
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
	public BoardCell selectTargets(Set<BoardCell> targets) {
		BoardCell returnTarget = null;
		for(BoardCell target : targets) {
			if(target.isRoom()) {
				if(getSeenCards().contains(new Card(target.getRoomName(), CardType.ROOM))) {
					continue;
				} else {
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
