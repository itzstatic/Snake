package com.brandon.snake.render.renderer.text;

import com.brandon.snake.graphics.Animation;
import com.brandon.snake.graphics.BitmapFont;

public class GameOverAnimation extends Animation {

	final private static long INITIAL_DELAY = 1100; //ms
	
	private BitmapFont font;
	
	public GameOverAnimation(BitmapFont font) {
		this.font = font;
	}
	
	@Override
	protected void onBegin() {
		
	}

	@Override
	protected void onUpdate(float time, float deltaTime) {
		font.drawString("Game Over!", 2);
	}

	@Override
	protected void onEnd() {
		
	}

	@Override
	public long getInitialDelay() {
		return INITIAL_DELAY;
	}

	@Override
	public long getDelay() {
		return 0;
	}

	@Override
	public long getDuration() {
		return -1;
	}

}
