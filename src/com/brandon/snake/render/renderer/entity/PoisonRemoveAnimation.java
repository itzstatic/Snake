package com.brandon.snake.render.renderer.entity;

import java.util.Deque;

import com.brandon.snake.graphics.Animation;
import com.brandon.snake.math.Matrix4f;

public class PoisonRemoveAnimation extends Animation {

	final private static long DURATION = 1000; //ms
	
	private Matrix4f model;
	private Deque<Matrix4f> models;
	
	public PoisonRemoveAnimation(Deque<Matrix4f> models) {
		this.models = models;
	}
	
	public void setModel(Matrix4f model) {
		this.model = model;
	}

	@Override
	protected void onBegin() {

	}

	@Override
	protected void onUpdate(float deltaTime) {
//		float scale = deltaTime / DURATION;
		model = model.mul(Matrix4f.scale(.5f, .5f, 1));
		models.removeFirst(); 
		models.addFirst(model);
	}

	@Override
	protected void onEnd() {
		models.removeFirst();
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
