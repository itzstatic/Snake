package com.brandon.snake.util.schedule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Scheduler {
	
	private List<ScheduledAction> actions;
	
	public Scheduler() {
		actions = new ArrayList<>();
	}
	
	public void update() {	
		for (ScheduledAction action : actions){
			if (action.shouldRun()) {
				action.run();
			}
		}
		Iterator<ScheduledAction> i = actions.iterator();
		while (i.hasNext()) {
			ScheduledAction e = i.next();
			if (!e.isScheduled()) {
				i.remove();
			}
		}
	}
	
	public ScheduledAction schedule(Runnable runnable, long initialDelay, TimeUnit unit) {
		ScheduledAction action = new OneTimeScheduledAction(
			runnable, 
			TimeUnit.MILLISECONDS.convert(initialDelay, unit),
			System.currentTimeMillis()
		);
		actions.add(action);
		return action;
	}
	
	public ScheduledAction scheduleAtFixedRate(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
		ScheduledAction action = new RecurringScheduledAction(
			runnable, 
			TimeUnit.MILLISECONDS.convert(initialDelay, unit), 
			TimeUnit.MILLISECONDS.convert(period, unit),
			System.currentTimeMillis()
		);
		actions.add(action);
		return action;
	}
	
}
