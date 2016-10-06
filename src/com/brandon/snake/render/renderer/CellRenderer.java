package com.brandon.snake.render.renderer;

import com.brandon.snake.game.Cell;
import com.brandon.snake.math.Matrix4f;
import com.brandon.snake.render.Renderer;

public abstract class CellRenderer implements Renderer {
	
	protected Matrix4f getCellTranslation(Cell cell) {
		//the .5f displacement moves the origin from the center of the mesh to the bottom left.
		return Matrix4f.translate(cell.getX() + .5f, cell.getY() + .5f, 0);
	}
}
