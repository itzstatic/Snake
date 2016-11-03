package com.brandon.snake.graphics;


public abstract class Animation2 {
	protected abstract void onBegin();
	protected abstract void onUpdate(long time, long deltaTime);
	protected abstract void onEnd();
	
	public abstract long getInitialDelay();
	public abstract long getDelay();
	/**
	 * A negative integer indicates never ending
	 * @return a long int representing the duration
	 */
	public abstract long getDuration();
	
	/**
	 * Stamps
	 */
	private long time;
	
	private boolean running;
	private boolean updating;
	
	private long lastUpdateAttempt;
	private long lastUpdate;
	/**
	 * An animation starts running with a call to start and stops running with a call to stop or the duration has ended
	 * An animation begins updating at the same time as onBegin. 
	 * If an animation has begun updating, then start was called before at some point.
	 * An animation ceases to update with a call to stop or the duration has ended.
	 */
	
	/**
	 * Starts this Animation, possibly waiting for it to begin updating.
	 */
	public void start() {
		time = - getInitialDelay();
		running = true;
		updating = false;
	}

	public boolean isFinished() {
		return !running;
	}
	
	public void update() {
		update(false);
	}
	
	/**
	 * Tries to update this animation
	 */
	public void update(boolean paused) {
		if (!running) return; //Need to call start first
		
		long currentTime = System.currentTimeMillis();
		
		if (!paused) {
			time += currentTime - lastUpdateAttempt;
		}
		lastUpdateAttempt = currentTime;
		if (paused) return;
		
		
		//Ensure this animation has begun
		if (!updating && time > 0) {
			onBegin();
			updating = true;
		}

		long duration = getDuration();

		//If this animation should stop, then stop it.
		if (0 < duration && duration < time) {
			stop();
			return;
		}
		
		long deltaTime = currentTime - lastUpdate; //Time since last update
		
		//If we should update
		if (deltaTime >= getDelay()) {
			onUpdate(time, deltaTime);
			lastUpdate = currentTime;
		}
	}
	
	/**
	 * Stops this animation, immediately Ending it.
	 */
	public void stop() {
		running = updating = false;
		onEnd();
	}
}
