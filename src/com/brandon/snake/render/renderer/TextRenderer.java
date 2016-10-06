package com.brandon.snake.render.renderer;

import com.brandon.snake.game.Game;
import com.brandon.snake.graphics.BitmapFont;
import com.brandon.snake.graphics.Shader;
import com.brandon.snake.render.GameOverHandler;
import com.brandon.snake.render.Renderer;

public class TextRenderer implements Renderer {
	//Constants
	final private int WINDOW_WIDTH;
	final private int WINDOW_HEIGHT;
	
	//Read only
	private GameOverHandler gameOverHandler;
	
	//Game State
	private int score;
	
	//Rendering resources
	private BitmapFont font;
	
	public TextRenderer(GameOverHandler gameOverHandler, int windowWidth, int windowHeight) {
		this.gameOverHandler = gameOverHandler;
		
		WINDOW_WIDTH = windowWidth;
		WINDOW_HEIGHT = windowHeight;
	}
	
	@Override
	public void init() {
		font = new BitmapFont("res/text.png", 16, 16, WINDOW_WIDTH, WINDOW_HEIGHT);
		font.setShader(new Shader("res/shaders/text.vs", "res/shaders/text.fs"), 0, "tex");
	}
	
	@Override
	public void reset() {
		score = 0;
	}
	
	@Override
	public void render(Game game) {
		font.bind(0);

		font.drawString("Score: ", 0, 0, 2);
		font.drawString(Integer.toString(score), 6 * 2, 0, 2); //"Score: " has length 6.
		
		if (game.isPaused()) {
			font.drawString("Paused", 2);
		}
		if (gameOverHandler.isGameOverVisible()) {
			font.drawString("Game Over!", 2);
		}
	}
	
	@Override
	public void update(Game game) {
		int newScore = game.getScore();
		if (score < newScore) {
			font.destroyString(Integer.toString(score));
		}
		score = newScore;
	}
	
	@Override
	public void destroy() {
		font.destroy();
	}
}
