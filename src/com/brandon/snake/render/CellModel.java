package com.brandon.snake.render;

import com.brandon.snake.game.Cell;
import com.brandon.snake.math.Matrix4f;

public abstract class CellModel {
	
	protected Matrix4f getCellModel(Cell cell) {
		//the .5f displacement moves the origin from the center of the mesh to the bottom left.
		return Matrix4f.translate(cell.getX() + .5f, cell.getY() + .5f, 0);
	}
}
