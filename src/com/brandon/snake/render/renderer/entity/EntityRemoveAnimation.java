package com.brandon.snake.render.renderer.entity;

import com.brandon.snake.graphics.Animation;
import com.brandon.snake.math.Matrix4f;

public class EntityRemoveAnimation extends Animation {

	final private static long DURATION = 1000; //ms
	
	private Matrix4f[] modelPtr;
	
	public EntityRemoveAnimation(Matrix4f[] modelPtr) {
		this.modelPtr = modelPtr;
	}
	
	@Override
	protected void onBegin() {
		
	}

	@Override
	protected void onUpdate(long time, long deltaTime) {
		float scale = 1 - deltaTime / 2 / (float) (DURATION - time);
		modelPtr[0] = modelPtr[0].mul(Matrix4f.scale(scale, scale, 1));
	}

	@Override
	protected void onEnd() {
		modelPtr[0] = modelPtr[0].mul(Matrix4f.rotateX(90)); //Make it invisible
	}

	@Override
	public long getInitialDelay() {
		return 0;
	}

	@Override
	public long getDelay() {
		return 0;
	}
	@Override
	public long getDuration() {
		return DURATION;
	}
}
