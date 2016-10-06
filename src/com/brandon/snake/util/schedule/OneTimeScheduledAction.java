package com.brandon.snake.util.schedule;

import com.brandon.snake.util.Time;

public class OneTimeScheduledAction extends ScheduledAction {

	private Runnable runnable;
	private long initialDelay; //ms
	private long timeAdded; //ms
	
	public OneTimeScheduledAction(Runnable runnable, long initialDelay, long timeAdded) {
		this.runnable = runnable;
		this.initialDelay = initialDelay;
		this.timeAdded = timeAdded;
	}
	
	@Override
	public void postpone() {
		timeAdded = Time.getTimeMilliseconds();
	}
	
	@Override
	void run() {
		runnable.run();
		cancel();
	}

	@Override
	boolean shouldRun() {
		return Time.getTimeMilliseconds() - timeAdded >= initialDelay;
	}

}
