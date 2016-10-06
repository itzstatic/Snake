package com.brandon.snake.util.schedule;

import com.brandon.snake.util.Time;


public class RecurringScheduledAction extends ScheduledAction {

	private Runnable runnable;
	private long initialDelay;
	private long period;
	private long timeAdded;
	private long lastRun;
	
	public RecurringScheduledAction(Runnable runnable, long initialDelay, long period, long timeAdded) {
		this.runnable = runnable;
		this.initialDelay = initialDelay;
		this.period = period;
		this.timeAdded = timeAdded;
	}
	
	@Override
	public void postpone() {
		timeAdded = Time.getTimeMilliseconds();
		lastRun = Time.getTimeMilliseconds();
	}
	
	@Override
	void run() {
		runnable.run();
		lastRun = Time.getTimeMilliseconds();
	}

	@Override
	boolean shouldRun() {
		return Time.getTimeMilliseconds() - lastRun >= period && Time.getTimeMilliseconds() - timeAdded >= initialDelay;
	}

}
