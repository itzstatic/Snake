package com.brandon.snake.graphics;


public abstract class Animation {
	protected abstract void onBegin();
	protected abstract void onUpdate(float time, float deltaTime);
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
	private boolean begun;
	
	/**
	 * As measured in real time.
	 */
	private long lastTimeUpdate;
	/**
	 * As measured in animation time.
	 */
	private long lastUpdate;
	
	/**
	 * Starts this Animation, possibly waiting for it to Begin.
	 */
	public void start() {
		if (!started) {
			time = - getInitialDelay();
			lastTimeUpdate = System.currentTimeMillis();
			
			started = true;
			begun = false;
		}
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
				if (time >= 0 && !begun) { //begin if you haven't
					onBegin();
					begun = true;
				} else if (getDuration() > 0 && time > getDuration()) { //stop/end if you need to
					stop();
				} else if (deltaTime >= getDelay()) { //update if you need to
					onUpdate(time, deltaTime);
					lastUpdate = time;
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
