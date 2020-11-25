package clueGame;

public class Solution {
	Card person;
	Card room;
	Card weapon;

	//constructor for a solution
	public Solution(Card person, Card room, Card weapon) {
		super();
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}

	//getters
	public Card getPerson() {
		return person;
	}
	public Card getRoom() {
		return room;
	}
	public Card getWeapon() {
		return weapon;
	}

	//equals function for comparing solutions
	public boolean equals(Solution solution) {
		if(!this.person.equals(solution.getPerson())) {
			return false;
		}
		if(!this.room.equals(solution.getRoom())) {
			return false;
		}
		return this.weapon.equals(solution.getWeapon());
	}
}
