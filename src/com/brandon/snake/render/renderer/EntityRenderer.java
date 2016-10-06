package com.brandon.snake.render.renderer;

import java.util.ArrayDeque;
import java.util.Queue;

import com.brandon.snake.game.Cell;
import com.brandon.snake.game.Game;
import com.brandon.snake.graphics.Mesh;
import com.brandon.snake.graphics.Shader;
import com.brandon.snake.math.Matrix4f;
import com.brandon.snake.math.Vector3f;
import com.brandon.snake.render.Renderer;
import com.brandon.snake.util.MeshUtil;

public class EntityRenderer implements Renderer {
	//Constants
	final private static Vector3f POISON_COLOR = new Vector3f(1, 0, 0);
	final private static Vector3f FOOD_COLOR = new Vector3f(0, 1, 0);
	
	private Queue<Matrix4f> poisonModels;
	private Matrix4f foodModel;
	
	//Rendering resources
	private Shader shader;
	private Mesh mesh;
	
	final private int GAME_WIDTH;
	final private int GAME_HEIGHT;
	
	public EntityRenderer(int gameWidth, int gameHeight) {
		GAME_WIDTH = gameWidth;
		GAME_HEIGHT = gameHeight;
		poisonModels = new ArrayDeque<>();
	}
	
	@Override
	public void init() {
		shader = new Shader("res/shaders/entity.vs", "res/shaders/entity.fs");
		shader.setUniformMat4f("proj", Matrix4f.orthographic(0, GAME_WIDTH, 0, GAME_HEIGHT, 1, -1));
		mesh = MeshUtil.createMesh(-.5f, -.5f, .5f, .5f);
	}
	
	@Override
	public void reset() {
		poisonModels.clear();
	}
	
	@Override
	public void render(Game game) {
		shader.bind();
		shader.setUniform3f("color", POISON_COLOR);
		for (Matrix4f model : poisonModels) {
			shader.setUniformMat4f("model", model);
			mesh.render();
		}
		
		shader.setUniform3f("color", FOOD_COLOR);
		shader.setUniformMat4f("model", foodModel);
		mesh.render();
	}
	
	@Override
	public void update(Game game) {
		if (game.shouldRemovePoison()) {
			poisonModels.remove();
		}
		
		Cell poison = game.getAddedPoison();
		if (poison != null) {
			poisonModels.add(MeshUtil.getCellTranslation(poison));
		}
		
		Cell food = game.getAddedFood();
		if (food != null) {
			foodModel = MeshUtil.getCellTranslation(food);
		}
	}
	
	@Override
	public void destroy() {
		shader.destroy();
		mesh.destroy();
	}
}
