package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {
	private int row, col;
	private boolean room, occupied;
	private Set<TestBoardCell> adjList;
	
	public TestBoardCell(int row, int col) {
		this.row = row;
		this.col = col;
		this.occupied = false;
		this.room = false;
		this.adjList = new HashSet<TestBoardCell>();
	}
	
	@Override
	public String toString() {
		return "TestBoardCell [row=" + row + ", col=" + col + "]";
	}

	public void addAdj(TestBoardCell cell) {
		adjList.add(cell);
	}
	public Set<TestBoardCell> getAdjList() {
		return adjList;
	}
	
	public void setRoom(boolean room) {
		this.room = room;
	}
	public boolean isRoom() {
		return this.room;
	}
	
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	public boolean isOccupied() {
		return this.occupied;
	}
}
