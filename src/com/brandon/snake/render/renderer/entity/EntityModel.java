package com.brandon.snake.render.renderer.entity;

import com.brandon.snake.game.Cell;
import com.brandon.snake.graphics.Mesh;
import com.brandon.snake.graphics.Shader;
import com.brandon.snake.math.Matrix4f;
import com.brandon.snake.render.CellModel;

public class EntityModel extends CellModel {
	private Matrix4f[] modelPtr;
	private EntityAddAnimation add;
	private EntityRemoveAnimation rem;
	
	public EntityModel(Cell cell) {
		modelPtr = new Matrix4f[] { getCellModel(cell) };
		add = new EntityAddAnimation(modelPtr);
		add.start();
	}
	
	public void render(Shader s, Mesh m, boolean paused) {
		if (add != null) {
			add.update(paused);
			
			if (add.isFinished()) {
				add = null;
			}
		}
		if (rem != null) {
			rem.update(paused);
		}
		s.setUniformMat4f("model", modelPtr[0]);
		m.render();
	}
	
	public void beginRemoving() {
		rem = new EntityRemoveAnimation(modelPtr);
		rem.start();
	}
	
	public boolean isDoneRemoving() {
		return rem != null && rem.isFinished();
	}
}
