package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private int row;
	private int col;
	
	private boolean room;
	private boolean occupied;
	private boolean roomLabel;
	private boolean roomCenter;

	private char initial;
	private char secretPassage;

	private Set<BoardCell> adjList;
	private DoorDirection doorDirection;

	//constructor
	public BoardCell(int row, int col, char initial) {
		super();
		this.row = row;
		this.col = col;
		
		if(initial != 'W' && initial != 'X')
			this.room = true;
		else
			this.room = false;
		this.occupied = false;
		this.roomLabel = false;
		this.roomCenter = false;
		
		this.initial = initial;
		this.adjList = new HashSet<>();
		this.doorDirection = DoorDirection.NONE;
	}
	
	//integer getters and setters
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}

	//boolean getters and setters
	public boolean isRoom() {
		return room;
	}
	public void setRoom(boolean room) {
		this.room = room;
	}
	public boolean isOccupied() {
		return occupied;
	}
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	public boolean isLabel() {
		return roomLabel;
	}
	public void setLabel(boolean roomLabel) {
		this.roomLabel = roomLabel;
	}
	public boolean isRoomCenter() {
		return roomCenter;
	}
	public void setRoomCenter(boolean roomCenter) {
		this.roomCenter = roomCenter;
	}

	//char getters and setters
	public char getInitial() {
		return initial;
	}
	public void setInitial(char initial) {
		this.initial = initial;
	}
	public boolean isSecretPassage() {
		if(secretPassage == 0)
			return false;
		return true;
	}
	public char getSecretPassage() {
		return secretPassage;
	}
	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}

	//adjList getters and setters
	public void addAdj(BoardCell cell) {
		adjList.add(cell);
	}
	public Set<BoardCell> getAdjList() {
		return adjList;
	}
	//doorway getters and setters
	public boolean isDoorway() {
		if(doorDirection == DoorDirection.NONE)
			return false;
		else
			return true;
	}
	public void setDoorway(DoorDirection direction) {
		doorDirection = direction;
	}
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
}
