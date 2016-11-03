package com.brandon.snake;

import com.brandon.snake.graphics.Animation;

public class TestAnimation extends Animation {

	@Override
	protected void onBegin() {
		System.out.println("It has begun!");
	}

	@Override
	protected void onUpdate(float time, float deltaTime) {
		System.out.println("At " + time + ": Updated after " + deltaTime);
	}

	@Override
	protected void onEnd() {
		System.out.println("Ended.");
	}

	@Override
	public long getInitialDelay() {
		return 1000;
	}

	@Override
	public long getDelay() {
		return 500;
	}

	@Override
	public long getDuration() {
		return 5000;
	}

}
