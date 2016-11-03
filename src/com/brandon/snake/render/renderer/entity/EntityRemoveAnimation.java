package com.brandon.snake.render.renderer.entity;

import com.brandon.snake.math.Matrix4f;

public class EntityRemoveAnimation extends EntityAnimation {

	final private static long DURATION = 1000; //ms
	
	public EntityRemoveAnimation(Matrix4f model) {
		this.model = model;
	}
	
	@Override
	protected void onBegin() {
		
	}

	@Override
	protected void onUpdate(float time, float deltaTime) {
		float scale = 1 - deltaTime / 2 / (float) (DURATION - time);
		model = model.mul(Matrix4f.scale(scale, scale, 1));
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
		return 0;
	}
	@Override
	public long getDuration() {
		return DURATION;
	}
}
