package clueGame;

public class Solution {
	Card person;
	Card room;
	Card weapon;
	public Solution(Card person, Card room, Card weapon) {
		super();
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}
	public Card getPerson() {
		return person;
	}
	public Card getRoom() {
		return room;
	}
	public Card getWeapon() {
		return weapon;
	}
	
	public boolean equals(Solution solution) {
		if(!this.person.equals(solution.getPerson())) {
			return false;
		}
		if(!this.room.equals(solution.getRoom())) {
			return false;
		}
		if(!this.weapon.equals(solution.getWeapon())) {
			return false;
		}
		return true;
	}
}
