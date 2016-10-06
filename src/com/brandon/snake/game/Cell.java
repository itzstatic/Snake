package com.brandon.snake.game;


public class Cell {

	private int x;
	private int y;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Cell translate(int distance, Direction dir) {
		switch (dir) {
		case UP: 
			return new Cell(x, y + distance);
		case DOWN: 
			return new Cell(x, y - distance);
		case LEFT: 
			return new Cell(x - distance, y);
		case RIGHT:
			return new Cell(x + distance, y);
		case NONE:
			return this;
		default:
			throw new IllegalArgumentException(dir.toString());
		}
	}
	
	public Cell translate(int dx, int dy) {
		return new Cell(x + dx, y + dy);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		
		if (o == null || !(o instanceof Cell)) {
			return false;
		}
		
		Cell c = (Cell) o;
		
		return x == c.x && y == c.y;
	}
	
	@Override
	public String toString() {
		return x + " " + y;
	}
}
