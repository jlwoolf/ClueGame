package clueGame;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private int row;
	private int col;

	private boolean room;
	private String roomName;
	private boolean occupied;
	private boolean roomLabel;
	private boolean roomCenter;

	private char initial;
	private char secretPassage;

	private final Set<BoardCell> adjList;
	private DoorDirection doorDirection;

	//constructor
	public BoardCell(int row, int col, char initial) {
		super();
		this.row = row;
		this.col = col;

		this.room = initial != 'W' && initial != 'X';

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
		return secretPassage != 0;
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
		return doorDirection != DoorDirection.NONE;
	}
	public void setDoorway(DoorDirection direction) {
		doorDirection = direction;
	}
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	//room getters and setters
	public void setRoomName(String name) {
		this.roomName = name;
	}
	public String getRoomName() {
		return roomName;
	}

	//function to draw a board cell
	public void drawCell(Graphics g, int size, int[] wallPadding) {
		if(room) {
			g.setColor(new Color(137, 117, 165));
			g.fillRect(col * size + wallPadding[0], row * size + wallPadding[1], size, size);
		} else if(initial == 'W') {
			g.setColor(new Color(255, 198, 109));
			g.fillRect(col * size + 1 + wallPadding[0], row * size + 1 + wallPadding[1], size - 2, size - 2);
		} else {
			g.setColor(new Color(203, 113, 46));
			g.fillRect(col * size + wallPadding[0], row * size + wallPadding[1], size, size);
		}
	}
	public void drawTarget(Graphics g, int size, int[] wallPadding) {
		g.setColor(new Color(84, 133, 83));
		g.fillRect(col*size + wallPadding[0], row*size + wallPadding[1], size, size);
	}

	//function to draw a door
	public void drawDoor(Graphics g, int size, int[] wallPadding) {
		g.setColor(new Color(104, 151, 187));
		switch (doorDirection) {
			case UP:
				g.fillRect(col*size + 1 + wallPadding[0], row*size - size/4 + 1 + wallPadding[1], size - 2, size/4);
				break;
			case DOWN:
				g.fillRect(col*size + 1 + wallPadding[0], row*size + size - 1 + wallPadding[1], size - 2, size/4);
				break;
			case LEFT:
				g.fillRect(col*size - size/4 + 1 + wallPadding[0], row*size + 1 + + wallPadding[1], size/4, size - 2);
				break;
			case RIGHT:
				g.fillRect(col*size + size - 1 + wallPadding[0], row*size + 1 + wallPadding[1], size/4, size - 2);
				break;
			case NONE:
				break;
		}
	}
}
