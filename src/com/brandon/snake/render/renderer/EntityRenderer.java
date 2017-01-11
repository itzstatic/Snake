package com.brandon.snake.render.renderer;

import java.util.HashMap;
import java.util.Map;

import com.brandon.snake.game.Cell;
import com.brandon.snake.game.Game;
import com.brandon.snake.graphics.Mesh;
import com.brandon.snake.graphics.Shader;
import com.brandon.snake.math.Matrix4f;
import com.brandon.snake.math.Vector3f;
import com.brandon.snake.render.Renderer;
import com.brandon.snake.render.renderer.entity.EntityModel;
import com.brandon.snake.util.MeshUtil;

public class EntityRenderer implements Renderer {
	//Constants
	final private static Vector3f POISON_COLOR = new Vector3f(1, 0, 0);
	final private static Vector3f FOOD_COLOR = new Vector3f(0, 1, 0);
	
	private Map<Cell, EntityModel> poisonModels;
	private EntityModel foodModel;
	
	//Rendering resources
	private Shader shader;
	private Mesh mesh;
	
	final private int GAME_WIDTH;
	final private int GAME_HEIGHT;
	
	public EntityRenderer(int gameWidth, int gameHeight) {
		GAME_WIDTH = gameWidth;
		GAME_HEIGHT = gameHeight;
		poisonModels = new HashMap<>();
	}
	
	@Override
	public void init() {
		shader = new Shader("res/shaders/entity.vs", "res/shaders/entity.fs");
		shader.setUniformMat4f("proj", Matrix4f.orthographic(0, GAME_WIDTH, 0, GAME_HEIGHT, 1, -1));
		mesh = MeshUtil.createEntityMesh(-.5f, -.5f, .5f, .5f);
	}
	
	@Override
	public void reset(Game game) {
		poisonModels.clear();
		foodModel = new EntityModel(game.getFood());
	}
	
	@Override
	public void render(Game game) {
		boolean paused = game.isPaused();
		shader.bind();
		
		//Draw Food
		shader.setUniform3f("color", FOOD_COLOR);
		foodModel.render(shader, mesh, paused);
		
		//Draw poison models
		shader.setUniform3f("color", POISON_COLOR);
		poisonModels.values().removeIf(e -> renderAndRemove(e, shader, mesh, paused));
	}

	private boolean renderAndRemove(EntityModel poison, Shader s, Mesh m, boolean paused) {
		poison.render(shader, mesh, paused);
		return poison.isDoneRemoving();
	}
	@Override
	public void update(Game game) {
		Cell poison = game.getAddedPoison();
		if (poison != null) {
			poisonModels.put(poison, new EntityModel(poison));
		}
		
		poison = game.getRemovedPoison();
		if (poison != null) {
			poisonModels.get(poison).beginRemoving();
		}
		
		Cell food = game.getAddedFood();
		if (food != null) {
			foodModel = new EntityModel(food);
		}
	}
	
	@Override
	public void destroy() {
		shader.destroy();
		mesh.destroy();
	}
}
