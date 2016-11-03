package com.brandon.snake.render.renderer;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

import com.brandon.snake.game.Cell;
import com.brandon.snake.game.Game;
import com.brandon.snake.graphics.Mesh;
import com.brandon.snake.graphics.Shader;
import com.brandon.snake.math.Matrix4f;
import com.brandon.snake.math.Vector3f;
import com.brandon.snake.render.renderer.entity.EntityAddAnimation;
import com.brandon.snake.render.renderer.entity.EntityAnimation;
import com.brandon.snake.render.renderer.entity.EntityRemoveAnimation;
import com.brandon.snake.util.MeshUtil;

public class EntityRenderer extends CellRenderer {
	//Constants
	final private static Vector3f POISON_COLOR = new Vector3f(1, 0, 0);
	final private static Vector3f FOOD_COLOR = new Vector3f(0, 1, 0);
	
	private Queue<Matrix4f> poisonModels; //First is old poisons; Last is recent poisons
	private Queue<EntityAnimation> addedPoisonModels;
	private Queue<EntityAnimation> removedPoisonModels;
	
	private EntityAnimation foodAnimation;
	
	//Rendering resources
	private Shader shader;
	private Mesh mesh;
	
	final private int GAME_WIDTH;
	final private int GAME_HEIGHT;
	
	public EntityRenderer(int gameWidth, int gameHeight) {
		GAME_WIDTH = gameWidth;
		GAME_HEIGHT = gameHeight;
		poisonModels = new ArrayDeque<>();
		addedPoisonModels = new ArrayDeque<>();
		removedPoisonModels = new ArrayDeque<>();
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
		addedPoisonModels.clear();
		removedPoisonModels.clear();
	}
	
	@Override
	public void render(Game game) {
		updateAnimations(game.isPaused());
		
		shader.bind();
		shader.setUniform3f("color", POISON_COLOR);
		
		for (Matrix4f model : poisonModels) {
			shader.setUniformMat4f("model", model);
			mesh.render();
		}
		for (EntityAnimation animation : addedPoisonModels) {
			shader.setUniformMat4f("model", animation.getModel());
			mesh.render();
		}
		for (EntityAnimation animation : removedPoisonModels) {
			shader.setUniformMat4f("model", animation.getModel());
			mesh.render();
		}
		
		shader.setUniform3f("color", FOOD_COLOR);
		shader.setUniformMat4f("model", foodAnimation.getModel());
		mesh.render();
	}
	
	@Override
	public void update(Game game) {
		if (game.shouldRemovePoison()) {
			Matrix4f poisonModel;
			if (poisonModels.isEmpty()) {
				poisonModel = addedPoisonModels.remove().getModel();
			} else {
				poisonModel = poisonModels.remove();
			}
			EntityAnimation poisonAnimation = new EntityRemoveAnimation(poisonModel);
			removedPoisonModels.add(poisonAnimation);
			poisonAnimation.start();
		}
		
		Cell poison = game.getAddedPoison();
		if (poison != null) {
			EntityAnimation poisonAnimation = new EntityAddAnimation(getCellTranslation(poison));
			poisonAnimation.start();
			addedPoisonModels.add(poisonAnimation);
		}
		
		Cell food = game.getAddedFood();
		if (food != null) {
			foodAnimation = new EntityAddAnimation(getCellTranslation(food));
			foodAnimation.start();
		}
	}
	
	@Override
	public void destroy() {
		shader.destroy();
		mesh.destroy();
	}
	
	private void updateAnimations(boolean paused) {
		Iterator<EntityAnimation> i = addedPoisonModels.iterator();	
		while (i.hasNext()) {
			EntityAnimation animation = i.next();
			if (animation.update(paused)) {
				i.remove();
				poisonModels.add(animation.getModel());
			}
		}
		
		removedPoisonModels.removeIf(e -> e.update(paused));
		foodAnimation.update(paused);
	}
}
