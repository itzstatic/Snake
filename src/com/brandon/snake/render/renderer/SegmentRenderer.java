package com.brandon.snake.render.renderer;

import java.util.HashMap;
import java.util.Map;

import com.brandon.snake.game.Cell;
import com.brandon.snake.game.Direction;
import com.brandon.snake.game.Game;
import com.brandon.snake.graphics.Mesh;
import com.brandon.snake.graphics.Shader;
import com.brandon.snake.graphics.texture.TextureAtlas;
import com.brandon.snake.math.Matrix4f;
import com.brandon.snake.math.Vector3f;
import com.brandon.snake.render.Renderer;
import com.brandon.snake.render.renderer.snake.SegmentModel;
import com.brandon.snake.util.MeshUtil;

public class SegmentRenderer implements Renderer {
	//Constants
	final private static Vector3f COLOR = new Vector3f(0, 1, 1);
	
	private Map<Cell, SegmentModel> models;
	private Cell head;
	
	//Rendering resources
	private TextureAtlas texture;
	private Shader shader;
	private Mesh[] meshes;
	
	final private int GAME_WIDTH;
	final private int GAME_HEIGHT;
	
	public SegmentRenderer(int gameWidth, int gameHeight) {
		models = new HashMap<>();
		meshes = new Mesh[4];
		
		GAME_WIDTH = gameWidth;
		GAME_HEIGHT = gameHeight;
	}
	
	@Override
	public void init() {
		texture = new TextureAtlas("res/snake.png", 2, 2);
		shader = new Shader("res/shaders/segment.vs", "res/shaders/segment.fs");
		shader.setUniformMat4f("proj", Matrix4f.orthographic(0, GAME_WIDTH, 0, GAME_HEIGHT, 1, -1));
		for (int i = 0; i < meshes.length; i++) {
			meshes[i] = MeshUtil.createTexturedMesh(-.5f, -.5f, .5f, .5f, texture.getTextureCoords(i));
		}
	}
	
	@Override
	public void reset(Game game) {
		models.clear();
		addSegment(
			game.getPreviousDirection(), 
			game.getCurrentDirection(), 
			game.getAddedSegment()
		);
	}
	
	@Override
	public void render(Game game) {
		texture.bind(0);
		shader.bind();
		shader.setUniform3f("color", COLOR);
		
		for (SegmentModel model : models.values()) {
			model.render(shader, meshes, game.isPaused());
		}
	}
	
	@Override
	public void update(Game game) {
		Cell segment = game.getAddedSegment();
		if (segment != null) {
			addSegment(
				game.getPreviousDirection(), 
				game.getCurrentDirection(), 
				segment
			);
		}
		
		segment = game.getRemovedSegment();
		if (segment != null) {
			models.remove(segment);
		}
		
		if (game.onGameOver()) {
			models.get(head).blink();
		}
		
	}
	
	@Override
	public void destroy() {
		texture.destroy();
		shader.destroy();
		
		for (Mesh mesh : meshes) {
			mesh.destroy();
		}
	}
	
	private void addSegment(Direction previous, Direction current, Cell segment) {
		int old = previous.getValue();
		int nu = current.getValue();

		//Not on the first draw
		if (!models.isEmpty()) {
			//Redraw old head as body
			models.replace(head, SegmentModel.createBodySegment(head, old, nu));
		}
		
		//Draw new head
		models.put(segment, SegmentModel.createHeadSegment(segment, nu));
		head = segment;
	}
	
}

