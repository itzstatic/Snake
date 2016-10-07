package com.brandon.snake.util.schedule;



public abstract class ScheduledAction {
	private boolean scheduled;
	
	public ScheduledAction() {
		scheduled = true;
	}
	
	public void cancel() {
		scheduled = false;
	}
	
	public boolean isScheduled() {
		return scheduled;
	}
	public abstract void postpone();
	
	abstract void run();
	abstract boolean shouldRun();

}
