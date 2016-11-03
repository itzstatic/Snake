package com.brandon.snake.render.renderer.entity;

import com.brandon.snake.math.Matrix4f;

public class EntityAddAnimation extends EntityAnimation {

	final private static long DURATION = 250; //ms
	final private static float SPEED = 90f / DURATION; //deg per ms
	
	private Matrix4f translation;
	
	public EntityAddAnimation(Matrix4f model) {
		super.model = translation = model;
	}
	
	@Override
	protected void onBegin() {
		
	}

	@Override
	protected void onUpdate(long time, long deltaTime) {
		model = model.mul(Matrix4f.rotateZ(SPEED * (float)deltaTime));
	}

	@Override
	protected void onEnd() {
		model = translation;
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
