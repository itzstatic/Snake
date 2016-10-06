package com.brandon.snake.render;

import com.brandon.snake.game.Game;
import com.brandon.snake.render.renderer.EntityRenderer;
import com.brandon.snake.render.renderer.SnakeRenderer;
import com.brandon.snake.render.renderer.TextRenderer;

public class GameRenderer {

	private Renderer[] renderers;
	private GameOverHandler gameOverHandler;
	private Game game;
	
	public GameRenderer(Game game, int gameWidth, int gameHeight, int windowWidth, int windowHeight) {
		this.game = game;
		gameOverHandler = new GameOverHandler();
		
		renderers = new Renderer[]{
			new SnakeRenderer(gameOverHandler, gameWidth, gameHeight),
			new EntityRenderer(gameWidth, gameHeight),
			new TextRenderer(gameOverHandler, windowWidth, windowHeight)
		};
	}

	public void init() {
		for (Renderer renderer : renderers) {
			renderer.init();
		}
	}

	public void reset() {
		gameOverHandler.reset();
		for (Renderer renderer : renderers) {
			renderer.reset();
		}
	}

	public void render() {
		gameOverHandler.update(); //Even when the game is paused
		for (Renderer renderer : renderers) {
			renderer.render(game);
		}
	}

	public void update() {
		for (Renderer renderer : renderers) {
			renderer.update(game);
		}
	}

	public void destroy() {
		for (Renderer renderer : renderers) {
			renderer.destroy();
		}
	}
}
