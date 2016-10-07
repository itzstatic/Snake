package com.brandon.snake.render.renderer.entity;

import java.util.Deque;

import com.brandon.snake.graphics.Animation;
import com.brandon.snake.math.Matrix4f;

public class PoisonAddAnimation extends Animation {
	
	final private static long DURATION = 250; //ms
	final private static float SPEED = 90f / DURATION; //deg per ms
	
	private Deque<Matrix4f> models;
	private Matrix4f model;
	private Matrix4f translation;
	
	public PoisonAddAnimation(Deque<Matrix4f> models) {
		this.models = models;
	}
	
	public void setModel(Matrix4f model) {
		translation = this.model = model;
	}
	
	@Override
	protected void onStart() {
		
	}
	
	@Override
	protected void onUpdate(float deltaTime) {
		model = model.mul(Matrix4f.rotateZ(SPEED * (float)deltaTime));
		models.removeLast();
		models.addLast(model);
		
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
