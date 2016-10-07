package com.brandon.snake.render.renderer;

import java.util.ArrayDeque;
import java.util.Deque;

import com.brandon.snake.game.Cell;
import com.brandon.snake.game.Game;
import com.brandon.snake.graphics.Mesh;
import com.brandon.snake.graphics.Shader;
import com.brandon.snake.math.Matrix4f;
import com.brandon.snake.math.Vector3f;
import com.brandon.snake.render.renderer.entity.EntityAddAnimation;
import com.brandon.snake.render.renderer.entity.PoisonAddAnimation;
import com.brandon.snake.render.renderer.entity.PoisonRemoveAnimation;
import com.brandon.snake.util.MeshUtil;

public class EntityRenderer extends CellRenderer {
	//Constants
	final private static Vector3f POISON_COLOR = new Vector3f(1, 0, 0);
	final private static Vector3f FOOD_COLOR = new Vector3f(0, 1, 0);
	
	private EntityAddAnimation foodAddAnimation;
	private PoisonAddAnimation poisonAddAnimation;
	private PoisonRemoveAnimation poisonRemoveAnimation;
	
	private Deque<Matrix4f> poisonModels; //First is old poisons; Last is recent poisons
	
	//Rendering resources
	private Shader shader;
	private Mesh mesh;
	
	final private int GAME_WIDTH;
	final private int GAME_HEIGHT;
	
	public EntityRenderer(int gameWidth, int gameHeight) {
		GAME_WIDTH = gameWidth;
		GAME_HEIGHT = gameHeight;
		poisonModels = new ArrayDeque<>();
		
		foodAddAnimation = new EntityAddAnimation();
		poisonAddAnimation = new PoisonAddAnimation(poisonModels);
		poisonRemoveAnimation = new PoisonRemoveAnimation(poisonModels);
	}
	
	public void addPoison(Matrix4f poisonModel) {
		
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
		if (!game.isPaused()) {
			foodAddAnimation.update();
			poisonAddAnimation.update();
			poisonRemoveAnimation.update();
		}
		
		shader.bind();
		shader.setUniform3f("color", POISON_COLOR);
		for (Matrix4f model : poisonModels) {
			shader.setUniformMat4f("model", model);
			mesh.render();
		}
		
		shader.setUniform3f("color", FOOD_COLOR);
		shader.setUniformMat4f("model", foodAddAnimation.getModel());
		mesh.render();
	}
	
	@Override
	public void update(Game game) {
		if (game.shouldRemovePoison()) {
			poisonRemoveAnimation.setModel(poisonModels.getFirst());
			poisonRemoveAnimation.start();
		}
		
		Cell poison = game.getAddedPoison();
		if (poison != null) {
			Matrix4f poisonModel = getCellTranslation(poison);
			poisonModels.addLast(poisonModel);
			poisonAddAnimation.setModel(poisonModel);
			poisonAddAnimation.start();
		}
		
		Cell food = game.getAddedFood();
		if (food != null) {
			foodAddAnimation.setModel(getCellTranslation(food));
			foodAddAnimation.start();
		}
	}
	
	@Override
	public void destroy() {
		shader.destroy();
		mesh.destroy();
	}
}
