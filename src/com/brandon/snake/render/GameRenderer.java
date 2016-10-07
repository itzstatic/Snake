package com.brandon.snake.render;

import com.brandon.snake.game.Game;
import com.brandon.snake.render.renderer.EntityRenderer;
import com.brandon.snake.render.renderer.SnakeRenderer;
import com.brandon.snake.render.renderer.TextRenderer;

public class GameRenderer {

	private Renderer[] renderers;
	private Game game;
	
	public GameRenderer(Game game, int gameWidth, int gameHeight, int windowWidth, int windowHeight) {
		this.game = game;
		
		renderers = new Renderer[]{
			new SnakeRenderer(gameWidth, gameHeight),
			new EntityRenderer(gameWidth, gameHeight),
			new TextRenderer(windowWidth, windowHeight)
		};
	}

	public void init() {
		for (Renderer renderer : renderers) {
			renderer.init();
		}
	}

	public void reset() {
		for (Renderer renderer : renderers) {
			renderer.reset();
		}
	}

	public void render() {
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
