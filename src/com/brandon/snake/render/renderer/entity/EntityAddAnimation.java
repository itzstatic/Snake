package com.brandon.snake.render.renderer.entity;

import com.brandon.snake.graphics.Animation;
import com.brandon.snake.math.Matrix4f;

public class EntityAddAnimation extends Animation {

	final private static long DURATION = 250; //ms 250
	final private static float ANGLE = 90f; //deg per ms
	
	private Matrix4f[] modelPtr;
	private Matrix4f translation;
	
	public EntityAddAnimation(Matrix4f[] modelPtr) {
		this.modelPtr = modelPtr;
		translation = modelPtr[0];
	}
	
	@Override
	protected void onBegin() {
		
	}

	@Override
	protected void onUpdate(long time, long deltaTime) {
		modelPtr[0] = modelPtr[0].mul(Matrix4f.rotateZ(ANGLE / DURATION * (float)deltaTime));
	}

	@Override
	protected void onEnd() {
		modelPtr[0] = translation;
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
