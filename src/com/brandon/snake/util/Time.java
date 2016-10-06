package com.brandon.snake.util;

public class Time {

	public static final long SECOND = 1_000_000_000L;
	public static final long MILLISECOND = 1_000_000L;
	
	private static double delta;
	
	private static double previousTime = getTimeSeconds();
	
	private Time() {}
	
	public static void update() {
		double currentTime = getTimeSeconds();
		delta = currentTime - previousTime;
		previousTime = currentTime;
	}
	
	public static double getDeltaSeconds() {
		return delta;
	}
	
	public static double getDeltaMilliseconds() {
		return delta * 1000;
	}
	
	public static double getTimeSeconds() {
		return (double)System.nanoTime() / SECOND;
	}
	
	public static long getTimeMilliseconds() {
		return System.nanoTime() / MILLISECOND;
	}
	
}