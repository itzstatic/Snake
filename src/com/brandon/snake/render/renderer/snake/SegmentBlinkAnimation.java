package com.brandon.snake.render.renderer.snake;

import com.brandon.snake.graphics.Animation;
import com.brandon.snake.math.Matrix4f;

public class SegmentBlinkAnimation extends Animation {

	final private static long DURATION = 1100; //ms
    final private static long DELAY = 120; //ms
	
    final private static int VISIBLE = 0;
    final private static int INVISIBLE = 1;
    
	private Matrix4f[] headModels;
	private int current;
	private Matrix4f[] modelPtr;
	
	public SegmentBlinkAnimation(Matrix4f[] modelPtr) {
		this.modelPtr = modelPtr;
		headModels = new Matrix4f[2];
		headModels[VISIBLE] = modelPtr[0];
		headModels[INVISIBLE] = headModels[VISIBLE].mul(Matrix4f.rotateX(90));
		current = VISIBLE;
	}
	
	@Override
	protected void onBegin() {
		
	}

	@Override
	protected void onUpdate(long time, long deltaTime) {
		current = 1 - current; //Toggles between 0 and 1
		modelPtr[0] = headModels[current];
	}

	@Override
	protected void onEnd() {
		modelPtr[0] = headModels[VISIBLE];
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
