package com.brandon.snake.util;

public class Wrapper<E> {
	private E value;
	
	public Wrapper() {
		value = null;
	}
	
	public Wrapper(E value) {
		this.value = value;
	}
	
	public E get() {
		return value;
	}
	
	public void set(E value) {
		this.value = value;
	}
}
