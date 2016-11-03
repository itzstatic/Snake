package com.brandon.snake.render.renderer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import com.brandon.snake.game.Cell;
import com.brandon.snake.game.Direction;
import com.brandon.snake.game.Game;
import com.brandon.snake.graphics.Mesh;
import com.brandon.snake.graphics.Shader;
import com.brandon.snake.graphics.texture.TextureAtlas;
import com.brandon.snake.math.Matrix4f;
import com.brandon.snake.math.Vector3f;
import com.brandon.snake.render.renderer.snake.HeadBlinkAnimation;
import com.brandon.snake.util.MeshUtil;

public class SnakeRenderer extends CellRenderer {
	//Constants
	final private static Vector3f COLOR = new Vector3f(0, 1, 1);

	//-1 indicates two opposite directions, OR (new is NONE and old is not NONE)
	//New direction: UP, DOWN, LEFT, RIGHT, NONE
	private final static int[][] BODY_INDICES = {
		{ 0, -1,  1,  1, -1}, //Old direction: UP
		{-1,  0,  1,  1, -1}, //Old direction: DOWN
		{ 1,  1,  0, -1, -1}, //Old direction: LEFT
		{ 1,  1, -1,  0, -1}, //Old direction: RIGHT
		{ 0,  0,  0,  0,  3}  //Old direction: NONE
	};
	
	//45 indicates two opposite directions, OR (new is NONE and old is not NONE)
	//New direction: UP, DOWN, LEFT, RIGHT, NONE
	private final static float[][] BODY_ROTATIONS = { //in Degrees CCW
		{ 90,  45,  90, 180,  45}, //Old direction: UP
		{ 45,  90,   0, -90,  45}, //Old direction: DOWN
		{-90, 180,   0,  45,  45}, //Old direction: LEFT
		{  0,  90,  45,   0,  45},  //Old direction: RIGHT
		{ 90,  90,   0,   0,   0} //Old direction: NONE
	};
		
	//New direction: UP, DOWN, LEFT, RIGHT, NONE
	private final static int[] HEAD_INDICES = {2, 2, 2, 2, 3};
	
	//New direction: UP, DOWN, LEFT, RIGHT, NONE
	private final static float[] HEAD_ROTATIONS = {90, -90, 180, 0, 0};
	
	//Parallel deques: First is Snake head, Last is Snake tail
	private Deque<Matrix4f> models;
	private Deque<Integer> indices;
	private HeadBlinkAnimation headBlinkAnimation;
	private Cell head;
	
	//Rendering resources
	private TextureAtlas texture;
	private Shader shader;
	private Mesh[] meshes;
	
	final private int GAME_WIDTH;
	final private int GAME_HEIGHT;
	
	public SnakeRenderer(int gameWidth, int gameHeight) {
		models = new ArrayDeque<>();
		indices = new ArrayDeque<>();
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
	public void reset() {
		models.clear();
		indices.clear();
	}
	
	@Override
	public void render(Game game) {
		if (!game.isRunning()) {
			headBlinkAnimation.update(false);
		}
		
		texture.bind(0);
		shader.bind();
		shader.setUniform3f("color", COLOR);
		
		Iterator<Matrix4f> modelIterator = models.iterator();
		Iterator<Integer> indexIterator = indices.iterator();
		
		while (modelIterator.hasNext() && indexIterator.hasNext()) {
			shader.setUniformMat4f("model", modelIterator.next());
			meshes[indexIterator.next()].render();
		}
		
	}
	
	@Override
	public void update(Game game) {
		Cell segment = game.getAddedSegment();
		if (segment != null) {
			add(game.getPreviousDirection(), game.getCurrentDirection(), segment);
		}
		
		if (game.shouldRemoveSegment()) {
			models.removeLast();
			indices.removeLast();
		}
		
		if (game.onGameOver()) {
			headBlinkAnimation = new HeadBlinkAnimation(models, models.getFirst());
			headBlinkAnimation.start();
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
	
	private void add(Direction previous, Direction current, Cell segment) {
		int old = previous.getValue();
		int nu = current.getValue();

		//Not on the first draw
		if (!models.isEmpty() || !indices.isEmpty()) {
			//Redraw old head as body (Rotate model, set texture index)
			models.removeFirst();
			models.addFirst(getCellTranslation(head).mul(Matrix4f.rotateZ(BODY_ROTATIONS[old][nu]))); //Rotate model
			indices.removeFirst(); //Change its texture index
			indices.addFirst(BODY_INDICES[old][nu]);
		}
		
		//Draw new head
		head = segment;
		models.addFirst(getCellTranslation(segment).mul(Matrix4f.rotateZ(HEAD_ROTATIONS[nu])));
		indices.addFirst(HEAD_INDICES[nu]);
	}
	
}
