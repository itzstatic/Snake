package com.brandon.snake.game;

import com.brandon.snake.graphics.Animation;

public class PoisonGenerator extends Animation {

	final private static long DELAY = 2500; //ms 2500
	
	private Game game;
	private float accumulated;
	
	public PoisonGenerator(Game game) {
		this.game = game;
	}
	
	@Override
	protected void onBegin() {
		
	}

	@Override
	protected void onUpdate(long time, long deltaTime) {
		game.addPoison();
	}

	@Override
	protected void onEnd() {

	}

	@Override
	public long getInitialDelay() {
		return 0;
	}

	@Override
	public long getDelay() {
		return DELAY;
	}

	@Override
	public long getDuration() {
		return -1;
	}

}
