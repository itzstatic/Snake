package com.brandon.snake.render.renderer.entity;

import com.brandon.snake.graphics.Animation;
import com.brandon.snake.math.Matrix4f;

public abstract class EntityAnimation extends Animation {
	protected Matrix4f model;
	
	public Matrix4f getModel() {
		return model;
	}
}
