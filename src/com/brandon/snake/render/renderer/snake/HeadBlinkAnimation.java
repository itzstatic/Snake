package com.brandon.snake.render.renderer.snake;

import java.util.Deque;

import com.brandon.snake.graphics.Animation;
import com.brandon.snake.math.Matrix4f;

public class HeadBlinkAnimation extends Animation {

	final private static long DURATION = 1100; //ms
    final private static long DELAY = 120; //ms
	
    final private static int VISIBLE = 0;
    final private static int INVISIBLE = 1;
    
	private Deque<Matrix4f> models;
	private Matrix4f[] headModels;
	private int current;
	
	
	public HeadBlinkAnimation(Deque<Matrix4f> models) {
		this.models = models;
		headModels = new Matrix4f[2];
	}
	
	public void setHeadModel(Matrix4f headModel) {
		headModels[VISIBLE] = headModel;
	}
	
	@Override
	protected void onBegin() {
		headModels[INVISIBLE] = headModels[VISIBLE].mul(Matrix4f.rotateX(90));
		current = VISIBLE;
	}

	@Override
	protected void onUpdate(float time, float deltaTime) {
		current = 1 - current; //Toggles between 0 and 1
		models.removeFirst();
		models.addFirst(headModels[current]);
		
	}

	@Override
	protected void onEnd() {
		models.removeFirst();
		models.addFirst(headModels[VISIBLE]);
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
		return DURATION;
	}

}
