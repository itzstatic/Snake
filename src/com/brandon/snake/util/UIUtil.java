package com.brandon.snake.util;

import com.brandon.snake.game.Direction;

public class UIUtil {
	private UIUtil() {}
	
	//-1 indicates that the two (opposite) directions are imcompatible
	//New direction: UP, DOWN, LEFT, RIGHT
	private final static int[][] TEXTURE_INDICIES = {
		{ 0, -1,  1,  1}, //Old direction: UP
		{-1,  0,  1,  1}, //Old direction: DOWN
		{ 1,  1,  0, -1}, //Old direction: LEFT
		{ 1,  1, -1,  0}  //Old direction: RIGHT
	};
	
	//New direction: UP, DOWN, LEFT, RIGHT
	private final static float[][] BODY_MESH_ROTATIONS = { //in Degrees CCW
		{90, -1, 90, 180}, //Old direction: UP
		{-1, 90, 0, -90}, //Old direction: DOWN
		{-90, 180, 0, -1}, //Old direction: LEFT
		{0, 90, -1, 0}  //Old direction: RIGHT
	};
	
	//New direction: UP, DOWN, LEFT, RIGHT
	private final static float[] END_MESH_ROTATIONS = {
		90, -90, 180, 0 
	};
	
	
	public static int getTextureIndex(Direction old, Direction nu) {
		if (nu == null) {
			return 3; //The centered end
		}
		if (old == null) {
			old = nu;
		}
		
		return TEXTURE_INDICIES[old.getValue()][nu.getValue()];
	}
	
	public static float getBodyMeshRotation(Direction old, Direction nu) {
		if (nu == null) {
			return 0; //The centered end
		}
		if (old == null) {
			old = nu;
		}
		return BODY_MESH_ROTATIONS[old.getValue()][nu.getValue()];
	}
	
	public static float getEndMeshRotation(Direction nu) {
		if (nu == null) {
			return 0; //Centered end
		}
		return END_MESH_ROTATIONS[nu.getValue()];
	}
}
