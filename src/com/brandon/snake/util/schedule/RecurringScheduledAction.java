package com.brandon.snake.util.schedule;



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
		timeAdded = System.currentTimeMillis();
		lastRun = System.currentTimeMillis();
	}
	
	@Override
	void run() {
		runnable.run();
		lastRun = System.currentTimeMillis();
	}

	@Override
	boolean shouldRun() {
		long currentTime = System.currentTimeMillis();
		return currentTime - lastRun >= period && currentTime - timeAdded >= initialDelay;
	}

}
