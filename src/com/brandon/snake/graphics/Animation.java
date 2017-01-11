package com.brandon.snake.graphics;

public abstract class Animation {
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

	private long time;
	private boolean started;
	private boolean animating;
	private boolean finished;
	
	/**
	 * As measured in real time.
	 */
	private long lastTimeUpdate;
	/**
	 * As measured in animation time.
	 */
	private long lastUpdate;
	
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * Starts this Animation, possibly waiting for it to Begin.
	 */
	public void start() {
		time = - getInitialDelay();
		lastTimeUpdate = System.currentTimeMillis();
		lastUpdate = 0;
		
		started = true;
		animating = false;
		finished = false;
	}
	
	/**
	 * Updates this Animation, possibly calling onUpdate
	 * 
	 * @return whether this Animation is done.
	 */
	public boolean update(boolean paused) {
		if (started) {
			updateTime(paused, System.currentTimeMillis());
			
			long deltaTime = time - lastUpdate;
			
			if (!paused) {
				if (time >= 0 && !animating) { //begin if you haven't
					onBegin();
					animating = true;
				}
				if (deltaTime >= getDelay()) { //update if you need to
					onUpdate(time, deltaTime);
					lastUpdate = time;
				}
				if (getDuration() > 0 && time > getDuration()) { //stop/end if you need to
					stop();
				} 
			}
			
		}
		return !started;
	}
	
	/**
	 * Stops this animation, immediately Ending it.
	 */
	public void stop() {
		if (started) {
			started = false;
			finished = true;
			onEnd();
		}
	}
	
	private void updateTime(boolean paused, long currentTime) {
		if (!paused) {
			time += currentTime - lastTimeUpdate;
			lastTimeUpdate = currentTime;
		}
		lastTimeUpdate = currentTime;
	}
}
