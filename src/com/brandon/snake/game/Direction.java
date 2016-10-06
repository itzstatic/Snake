package com.brandon.snake.game;

public enum Direction {
	UP(0),
	DOWN(1),
	LEFT(2),
	RIGHT(3),
	NONE(4);
	
	private int value;
	
	private Direction(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public Direction opposite() {
		switch (this) {
		case UP: return DOWN;
		case DOWN: return UP;
		case LEFT: return RIGHT;
		case RIGHT: return LEFT;
		default:
			throw new IllegalArgumentException(toString());
		}
	}
}
