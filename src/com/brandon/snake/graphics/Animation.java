package com.brandon.snake.graphics;

import java.util.concurrent.TimeUnit;

import com.brandon.snake.util.schedule.ScheduledAction;
import com.brandon.snake.util.schedule.Scheduler;

public abstract class Animation {
	protected abstract void onStart();
	protected abstract void onUpdate(float deltaTime);
	protected abstract void onStop();
	
	public abstract long getDuration();
	
	private long lastUpdate;
	
	private Scheduler animator;
	private ScheduledAction bodyHandle;
	private ScheduledAction stopHandle;
	
	protected Animation() {
		animator = new Scheduler();
	}
	
	public void start() {
		onStart();
		lastUpdate = System.currentTimeMillis();
		bodyHandle = animator.scheduleAtFixedRate(
			() -> {
				onUpdate(System.currentTimeMillis() - lastUpdate);
			}, 
			0, 
			0, 
			TimeUnit.MILLISECONDS);
		stopHandle = animator.schedule(
			this::stop,
			getDuration(), 
			TimeUnit.MILLISECONDS
		);
	}
	
	public void update() {
		animator.update();
		lastUpdate = System.currentTimeMillis();
	}
	
	public void stop() {
		onStop();
		if (bodyHandle != null && stopHandle != null) {
			bodyHandle.cancel();
			bodyHandle = null;
			
			stopHandle.cancel();
			stopHandle = null;
		}
	}
	
}
