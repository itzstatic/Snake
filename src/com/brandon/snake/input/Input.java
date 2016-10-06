package com.brandon.snake.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import java.util.ArrayDeque;
import java.util.Queue;

import org.lwjgl.glfw.GLFWKeyCallbackI;

import com.brandon.snake.game.Direction;
import com.brandon.snake.game.Game;
import com.brandon.snake.render.GameRenderer;

public class Input implements GLFWKeyCallbackI {
	public final int keyUp;
	public final int keyDown;
	public final int keyLeft;
	public final int keyRight;
	public final int keyPause;
	public final int keyReset;
	public final int keyExit;
	
	private Game game;
	private GameRenderer renderer;
	private Queue<Direction> moves;
	
	
	public Input(int up, int down, int left, int right, int pause, int reset, int exit) {
		moves = new ArrayDeque<>();
		keyUp = up;
		keyDown = down;
		keyLeft = left;
		keyRight = right;
		
		keyPause = pause;
		keyReset = reset;
		keyExit = exit;
	}
	
	public void init(Game game, GameRenderer renderer) {
		this.game = game;
		this.renderer = renderer;
	}
	
	public void pollNext() {
		if (!moves.isEmpty()) {
			game.setSnakeDirection(moves.poll());
		}
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		//Only respond to key presses
		if (action != GLFW_PRESS) {
			return;
		}
		
		if (key == keyExit) {
			if (game.isPaused() || !game.isRunning()) {
				glfwSetWindowShouldClose(window, true);
			}
		} else if (key == keyPause) {
			if (game.isRunning()) {
				game.setPaused(!game.isPaused());
			}
		} else if (key == keyReset) {
			if (game.isPaused() || !game.isRunning()) {
				game.reset();
				renderer.reset();
			}
		} else if (key == keyUp) {
			moves.add(Direction.UP);
		} else if (key == keyDown) {
			moves.add(Direction.DOWN);
		} else if (key == keyLeft) {
			moves.add(Direction.LEFT);
		} else if (key == keyRight) {
			moves.add(Direction.RIGHT);
		}
	}
}
