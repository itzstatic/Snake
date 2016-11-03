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
	
	
	public HeadBlinkAnimation(Deque<Matrix4f> models, Matrix4f headModel) {
		headModels = new Matrix4f[2];
		this.models = models;
		
		headModels[VISIBLE] = headModel;
		headModels[INVISIBLE] = headModels[VISIBLE].mul(Matrix4f.rotateX(90));
		current = VISIBLE;
	}
	
	@Override
	protected void onBegin() {
		
	}

	@Override
	protected void onUpdate(long time, long deltaTime) {
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
