package com.brandon.snake.render.renderer.entity;

import com.brandon.snake.graphics.Animation;
import com.brandon.snake.math.Matrix4f;

public class EntityAddAnimation extends Animation {

	final private static long DURATION = 250; //ms
	final private static float SPEED = 90f / DURATION; //deg per ms
	
	private Matrix4f translation;
	private Matrix4f model;
	
	public void setModel(Matrix4f model) {
		translation = this.model = model;
	}
	
	public Matrix4f getModel() {
		return model;
	}
	
	@Override
	protected void onStart() {
		
	}

	@Override
	protected void onUpdate(float deltaTime) {
		model = model.mul(Matrix4f.rotateZ(SPEED * (float)deltaTime));
	}

	@Override
	protected void onStop() {
		model = translation;
	}

	@Override
	public long getDuration() {
		return DURATION;
	}

}
