package com.brandon.snake.render;

import java.util.concurrent.TimeUnit;

import com.brandon.snake.math.Matrix4f;
import com.brandon.snake.util.schedule.ScheduledAction;
import com.brandon.snake.util.schedule.Scheduler;

public class GameOverHandler {
	//Constants
	final private static long HEAD_FLICKER_DURATION = 1100; //ms
    final private static long HEAD_FLICKER_DELAY = 120; //ms
    
    final private static int VISIBLE = 0;
    final private static int INVISIBLE = 1;
    
	private Scheduler headFlickerScheduler;
	
	//Only non null while flickering. One is null iff the other is null.
	private ScheduledAction flickerHandle;
	private ScheduledAction flickerCancelHandle;
	
	private Matrix4f[] headModels;
	private int index;
	
	private boolean isGameOverVisible;
	
	public GameOverHandler() {
		headFlickerScheduler = new Scheduler();
		headModels = new Matrix4f[2];
	}
	
	public void update() {
		headFlickerScheduler.update();
	}
	
	private void stopFlickering() {
		//If we are still flickering: Each is null iff the other is null
		if (flickerCancelHandle != null && flickerHandle != null) {
			flickerCancelHandle.cancel();
			flickerCancelHandle = null;
			
			flickerHandle.cancel();
			flickerHandle = null;
		}
	}
	
	public void gameOver(Matrix4f headModel) {
		//Assign head models
		headModels[VISIBLE] = headModel;
		headModels[INVISIBLE] = headModel.mul(Matrix4f.rotateX(90));
		index = VISIBLE;
		//Flicker every DELAY
		flickerHandle = headFlickerScheduler.scheduleAtFixedRate(
			() -> {
				index = 1 - index;
			},  
			HEAD_FLICKER_DELAY, 
			HEAD_FLICKER_DELAY, 
			TimeUnit.MILLISECONDS
		);
		
		//After DURATION, cancel the flicker and display the game over screen
		flickerCancelHandle = headFlickerScheduler.schedule(
			() -> {
				stopFlickering();
				isGameOverVisible = true;
				index = VISIBLE;
			}, 
			HEAD_FLICKER_DURATION, 
			TimeUnit.MILLISECONDS
		);
	}
	
	public boolean isGameOverVisible() {
		return isGameOverVisible;
	}
	
	public Matrix4f getHeadModel() {
		return headModels[index];
	}
	
	public void reset() {
		isGameOverVisible = false;
		stopFlickering();
	}
}
