package com.brandon.snake.render.renderer.snake;

import com.brandon.snake.game.Cell;
import com.brandon.snake.graphics.Mesh;
import com.brandon.snake.graphics.Shader;
import com.brandon.snake.math.Matrix4f;
import com.brandon.snake.render.CellModel;

public class SegmentModel extends CellModel {
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
	
	//Static factory methods
	public static SegmentModel createHeadSegment(Cell cell, int nu) {
		SegmentModel segment = new SegmentModel();
		segment.modelPtr = new Matrix4f[] { segment.getCellModel(cell).mul(Matrix4f.rotateZ(HEAD_ROTATIONS[nu])) };
		segment.index = HEAD_INDICES[nu];
		return segment;
	}
		
	public static SegmentModel createBodySegment(Cell cell, int old, int nu) {
		SegmentModel segment = new SegmentModel();
		segment.modelPtr = new Matrix4f[] { segment.getCellModel(cell).mul(Matrix4f.rotateZ(BODY_ROTATIONS[old][nu])) }; 
		segment.index = BODY_INDICES[old][nu];
		return segment;
	}
	
	private int index;
	private Matrix4f[] modelPtr;
	private SegmentBlinkAnimation blink;
	
	private SegmentModel() {};
	
	public void render(Shader s, Mesh[] m, boolean paused) {
		if (blink != null) {
			blink.update(paused);
		}
		
		s.setUniformMat4f("model", modelPtr[0]);
		m[index].render();
	}
	
	public void blink() {
		blink = new SegmentBlinkAnimation(modelPtr);
		blink.start();
	}
	
	
}
