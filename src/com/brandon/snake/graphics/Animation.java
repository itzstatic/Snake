package com.brandon.snake.graphics;


public abstract class Animation {
	protected abstract void onBegin();
	protected abstract void onUpdate(float deltaTime);
	protected abstract void onEnd();
	
	public abstract long getInitialDelay();
	public abstract long getDelay();
	/**
	 * A negative integer indicates never ending
	 * @return a long int representing the duration
	 */
	public abstract long getDuration();
	
	private long lastUpdate;
	private long timeStarted;
	private long timeBegun;
	private boolean started;
	private boolean begun;
	
	/**
	 * Starts this Animation, possibly waiting for it to Begin.
	 */
	public void start() {
		timeStarted = System.currentTimeMillis();
		started = true;
		begun = false;
	}
	
	/**
	 * Updates this Animation, possibly calling onUpdate
	 */
	public void update() {
		if (!started) return;
		
		long currentTime = System.currentTimeMillis();
		long deltaTime = currentTime - lastUpdate;
		
		long duration = getDuration();
		long initialDelay = getInitialDelay();
		
		boolean afterInitialDelay = currentTime - timeStarted >= initialDelay;
		boolean afterUpdateDelay = deltaTime >= getDelay() ;
		
		if (afterInitialDelay) {
			if (!begun) {
				onBegin();
				begun = true;
				timeBegun = System.currentTimeMillis();
			}
		} else {
			lastUpdate = System.currentTimeMillis();
			return;
		}
		
		//Ensure that initial delay does not dig into duration
		boolean beforeDuration = currentTime - timeBegun <= duration || duration < 0;
		
		if (!beforeDuration) {
			stop();
			return;
		}
		
		if (afterUpdateDelay
		) {
			onUpdate(deltaTime);
			lastUpdate = System.currentTimeMillis();
		}
	}
	
	/**
	 * Stops this animation, immediately Ending it.
	 */
	public void stop() {
		begun = false;
		started = false;
		onEnd();
	}
}
