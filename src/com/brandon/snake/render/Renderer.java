package com.brandon.snake.render;

import com.brandon.snake.game.Game;


public interface Renderer {
	void init();
	void reset(Game game);
	void render(Game game);
	void update(Game game);
	void destroy();
}
