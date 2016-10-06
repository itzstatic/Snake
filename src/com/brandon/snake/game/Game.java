package com.brandon.snake.game;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.brandon.snake.util.schedule.ScheduledAction;
import com.brandon.snake.util.schedule.Scheduler;

public class Game {
	//Game constants
	final private static long POISON_GENERATION_DELAY = 2500; //ms 2500
	final private static int INITIAL_SIZE = 3; //Of the snake before any food
	final private static int GROWTH = 3; //The size added to the snake after eating
	
	//Poison generation
	private Scheduler poisonScheduler;
	private ScheduledAction poisonGenerator;
	
	private Random random;
	
	//General game state
	private Queue<Cell> poisons;
	private Deque<Cell> segments; //First is snake head, Last is snake tail
	private int stomachSize;
	private boolean running;
	private boolean paused;
	private int score;
	private Cell food;
	private Direction previousDir; //Directions should never be null
	private Direction currentDir;
	
	//Delta game state (Consumed by access)
	//Default values: null, false, where applicable
	private Cell addedFood;
	private Cell addedSegment;
	private Cell addedPoison;
	private boolean shouldRemoveSegment;
	private boolean shouldRemovePoison;
	private boolean onGameOver;
	
	//Instance constants
	private final int WIDTH; 
	private final int HEIGHT; //By number of squares
	
	public Game(int width, int height) {
		WIDTH = width;
		HEIGHT = height;
		
		random = new Random();
		poisons = new ArrayDeque<>();
		segments = new ArrayDeque<>();
		poisonScheduler = new Scheduler();
		poisonGenerator = poisonScheduler.scheduleAtFixedRate(
			this::addPoison, 
			POISON_GENERATION_DELAY, 
			POISON_GENERATION_DELAY, 
			TimeUnit.MILLISECONDS
		);
	}
	
	private void addPoison() {
		Cell poison = generatePickup();
		poisons.add(poison);
		addedPoison = poison;
	}
	
	public void update() {
		//Before the snake moves
		if (currentDir == Direction.NONE) {
			poisonGenerator.postpone(); //Poison will not start its generation
			return;
		} 
		
		Cell newHead = segments.getFirst().translate(1, currentDir);
		//Eat food
		if (food.equals(newHead)) {
			stomachSize = GROWTH;
			score++;
			//Refresh poison
			if (poisons.isEmpty()) {
				poisonGenerator.postpone();
			} else {
				poisons.remove();
				shouldRemovePoison = true;
			}
			//Generate new food
			addedFood = food = generatePickup();
		} else
		//Snake death
		
		if (!isInBounds(newHead) || poisons.contains(newHead) || segments.contains(newHead)) {
			running = false;
			onGameOver = true;
			return;
		}
		
		//Inch snake forward
		if (stomachSize == 0) {
			segments.removeLast();
			shouldRemoveSegment = true;
		} else {
			stomachSize--;
		}
		
		segments.addFirst(newHead);
		addedSegment = newHead;
		
		poisonScheduler.update();
	}
	
	public void straighten() {
		previousDir = currentDir;
	}
	
	public void reset() {
		poisons.clear();
		segments.clear();
		segments.addFirst(addedSegment = new Cell(WIDTH / 2 , HEIGHT / 2));
		stomachSize = INITIAL_SIZE - 1; //stomach is initially all non-head segments
		
		running = true;
		paused = false;
		score = 0;
		addedFood = food = generatePickup();
		
		previousDir = Direction.NONE;
		currentDir = Direction.NONE;
	}
	
	public void setPaused(boolean b) {
		paused = b;
	}
	
	public void setSnakeDirection(Direction nextDir) {
		if (paused || nextDir == null || nextDir == Direction.NONE || nextDir.opposite().equals(currentDir)) {
			return;
		}
		previousDir = currentDir;
		currentDir = nextDir;
	}
	
	private boolean isInBounds(Cell c) {
		int x = c.getX();
		int y = c.getY();
		return 0 <= x && x < WIDTH && 0 <= y && y < HEIGHT;
	}
	
	private Cell generatePickup() {
		Cell c;
		
		do {
			c = new Cell(random.nextInt(WIDTH), random.nextInt(HEIGHT));
		} while (c.equals(food) || poisons.contains(c) || segments.contains(c));
		
		return c;
	}
	
	public boolean isRunning() {
		return running;
	}
	public boolean isPaused() {
		return paused;
	}
	public int getScore() {
		return score;
	}
	public Cell getFood() {
		return food;
	}
	public Cell getAddedFood() {
		Cell temp = addedFood;
		addedFood = null;
		return temp;
	}
	public Cell getAddedSegment() {
		Cell temp = addedSegment;
		addedSegment = null;
		return temp;
	}
	public Cell getAddedPoison() {
		Cell temp = addedPoison;
		addedPoison = null;
		return temp;
	}
	public boolean shouldRemoveSegment() {
		boolean temp = shouldRemoveSegment;
		shouldRemoveSegment = false;
		return temp;
	}
	public boolean shouldRemovePoison() {
		boolean temp = shouldRemovePoison;
		shouldRemovePoison = false;
		return temp;
	}
	public Direction getCurrentDirection() {
		return currentDir;
	}
	public Direction getPreviousDirection() {
		return previousDir;
	}
	public boolean onGameOver() {
		boolean temp = onGameOver;
		onGameOver = false;
		return temp;
	}
}
