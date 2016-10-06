package com.brandon.snake.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.brandon.snake.game.Cell;
import com.brandon.snake.game.Direction;
import com.brandon.snake.graphics.BitmapFont;
import com.brandon.snake.graphics.Mesh;
import com.brandon.snake.graphics.Shader;
import com.brandon.snake.graphics.texture.TextureAtlas;
import com.brandon.snake.math.Matrix4f;
import com.brandon.snake.math.Vector3f;
import com.brandon.snake.render.GameOverHandler;
import com.brandon.snake.util.MeshUtil;
import com.brandon.snake.util.Time;
import com.brandon.snake.util.UIUtil;
import com.brandon.snake.util.schedule.ScheduledAction;
import com.brandon.snake.util.schedule.Scheduler;

public class LWJGLGameUI {
	//UI Constants
	final private static Vector3f POISON_COLOR = new Vector3f(1, 0, 0);
	final private static Vector3f FOOD_COLOR = new Vector3f(0, 1, 0);
	final private static Vector3f SEGMENT_COLOR = new Vector3f(0, 1, 1);
	
	final private static long ENTITY_SPIN_DURATION = 750; //ms
	final private static float ENTITY_SPIN_SPEED = 360 * 1000; //deg per ms
	
	//General state
	private boolean paused;
	private boolean isGameOver;
	private int score;
	private Cell head; //Null after game over and before the first segment is added to the ui
	private Direction previousDir; //Directions null while snake is still
	private Direction currentDir;
	
	//Head flickering 
	private GameOverHandler gameOverHandler;
	
	private Scheduler animator;
	private ScheduledAction foodAnimation;
	
	//Models for drawing
	private Map<Cell, Matrix4f> segmentModels;
	private Map<Cell, Integer> segmentIndices; //Index into segmentMeshes and texture atlas
	private Map<Cell, Matrix4f> poisonModels;
	private Matrix4f foodModel;
	
	//Resources for drawing
	private Shader entityShader;
	private Mesh entityMesh;
	
	private Shader segmentShader;
	private TextureAtlas segmentTexture;
	private Mesh[] segmentMeshes; //straight, T, tail, head indexed by texture
	
	private BitmapFont font;
	
	//Game constants
	final private int GAME_WIDTH;
	final private int GAME_HEIGHT;
	
	public LWJGLGameUI(int gameWidth, int gameHeight) {
		gameOverHandler = new GameOverHandler();
		segmentModels = new HashMap<>();
		segmentIndices = new HashMap<>();
		poisonModels = new HashMap<>();
		segmentMeshes = new Mesh[4];
		animator = new Scheduler();
		
		GAME_WIDTH = gameWidth;
		GAME_HEIGHT = gameHeight;
	}
	
	public void init(int windowWidth, int windowHeight) {
		entityShader = new Shader("res/shaders/entity.vs", "res/shaders/entity.fs");
		segmentShader = new Shader("res/shaders/segment.vs", "res/shaders/segment.fs");
		Matrix4f ortho = Matrix4f.orthographic(0, GAME_WIDTH, 0, GAME_HEIGHT, 1, -1);
		entityShader.setUniformMat4f("proj", ortho);
		segmentShader.setUniformMat4f("proj", ortho);
		
		
		font = new BitmapFont("res/text.png", 16, 16, 1.0f, windowWidth, windowHeight);
		font.setShader(new Shader("res/shaders/text.vs", "res/shaders/text.fs"), 0, "tex");
		
		segmentTexture = new TextureAtlas("res/snake.png", 2, 2);
		
		entityMesh = MeshUtil.createEntityMesh();
		
		for (int i = 0; i < segmentMeshes.length; i++) {
			float[] tcs = segmentTexture.getTextureCoords(i);
			segmentMeshes[i] = MeshUtil.createSegmentMesh(-.5f, -.5f, .5f, .5f, tcs);
		}
		
	}

	public void update() {
		
	}

	public void render() {
		gameOverHandler.update();
		if (!paused) {
			animator.update();
		}
		if (isGameOver) {
			segmentModels.put(head, gameOverHandler.getHeadModel());
		}
		
		segmentShader.bind();
		segmentTexture.bind(0);
		segmentShader.setUniform3f("color", SEGMENT_COLOR);
		for (Cell segment : segmentModels.keySet()) {
			Matrix4f model = segmentModels.get(segment);
			int index = segmentIndices.get(segment);
			
			segmentShader.setUniformMat4f("model", model);
			segmentMeshes[index].render();
		}
		
		entityShader.setUniform3f("color", POISON_COLOR);
		for (Matrix4f model : poisonModels.values()) {
			entityShader.setUniformMat4f("model", model);
			entityMesh.render();
		}
		
		entityShader.setUniform3f("color", FOOD_COLOR);
		entityShader.setUniformMat4f("model", foodModel);
		entityMesh.render();
		
		font.bind(0);
		
		font.drawString("Score:", 0, 0, 2);
		font.drawString(Integer.toString(score), 6 * 2, 0, 2);
		
		if (paused) {
			font.drawString("Paused", 2);
		}
		if (gameOverHandler.isGameOverVisible()) {
			font.drawString("Game Over!", 2);
		}
		
	}

	public void reset() {
		paused = false;
		isGameOver = false;
		score = 0;
		head = null;
		previousDir = null;
		currentDir = null;
		
		segmentModels.clear();
		segmentIndices.clear();
		poisonModels.clear();
		
		gameOverHandler.reset();
	}

	public void gameOver(Cell head) {
		isGameOver = true;
		gameOverHandler.gameOver(segmentModels.get(head));
	}

	public void setScore(int score) {
		font.destroyString(Integer.toString(this.score));
		this.score = score;
	}

	public void setPaused(boolean b) {
		paused = b;
	}

	public void setFood(Cell food) {
		foodModel = Matrix4f.translate(food.getX(), food.getY(), 0);
		foodAnimation = animator.scheduleAtFixedRate(
			() -> {
				foodModel = foodModel.mul(Matrix4f.rotateZ(ENTITY_SPIN_SPEED * (float) Time.getDeltaMilliseconds()));
			}, 
			0, 
			0, //Always update whenever you can 
			TimeUnit.MILLISECONDS
		);
		animator.schedule(() -> {
				foodAnimation.cancel();
				foodModel = Matrix4f.translate(food.getX(), food.getY(), 0);
			}, 
			ENTITY_SPIN_DURATION, 
			TimeUnit.MILLISECONDS
		);
	}
	
	public void setDirection(Direction nu) {
		previousDir = currentDir;
		currentDir = nu;
	}

	public void addPoison(Cell poison) {
		poisonModels.put(poison, Matrix4f.translate(poison.getX(), poison.getY(), 0));
	}

	public void removePoison(Cell poison) {
		poisonModels.remove(poison);
	}

	/**
	 * Adds the specified segment to be the head of the snake. Should agree with the snake direction for this UI.
	 * Also might change the old head segment to deal with turns properly. 
	 * @param segment the new head to set
	 */
	
	public void addSegment(Cell segment) {
		//On the very first add segment, redrawing the head is meaningless.
		if (head == null) { 
			//Draw the very first centered end
			segmentModels.put(segment, getSegmentTranslation(segment));
			segmentIndices.put(segment, 3); //3 is the centered end (starting head)
			head = segment;
			return;
		} else {
			//Redraw the head.
			segmentModels.put(head, getSegmentTranslation(head).mul(getBodyRotation()));
			segmentIndices.put(head, UIUtil.getTextureIndex(previousDir, currentDir));
			previousDir = currentDir;
		}
		head = segment;
		segmentModels.put(segment, getSegmentTranslation(segment).mul(getEndRotation()));
		segmentIndices.put(segment, 2); //2 is the END
	}
	
	private Matrix4f getSegmentTranslation(Cell segment) {
		//the .5f displacement moves the origin from the center of the mesh to the bottom left.
		return Matrix4f.translate(segment.getX() + .5f, segment.getY() + .5f, 0);
	}
	
	private Matrix4f getBodyRotation() {
		return Matrix4f.rotateZ(UIUtil.getBodyMeshRotation(previousDir, currentDir));
	}
	
	private Matrix4f getEndRotation() {
		return Matrix4f.rotateZ(UIUtil.getEndMeshRotation(currentDir));
	}
	
	public void removeSegment(Cell segment) {
		segmentModels.remove(segment);
	}

	public void destroy() {
		entityShader.destroy();
		segmentShader.destroy();
		
		font.destroy();
		segmentTexture.destroy();
		
		entityMesh.destroy();
		
		for (Mesh mesh : segmentMeshes) {
			mesh.destroy();
		}
	}
	
}
