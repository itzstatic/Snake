package com.brandon.snake.util;


import com.brandon.snake.graphics.Mesh;

public class MeshUtil {
	private MeshUtil(){}
	
	public static Mesh createEntityMesh(float left, float bottom, float right, float top) {
		float border = 1 / 32f;
		return new Mesh()
		.putVertices(new float[]{ //positions
			left + border, bottom + border,
			right - border, bottom + border,
			right - border, top - border,
			left + border, top - border
		}).putIndices(new int[]{ //indices
			3, 0, 1,		//bottom left
			1, 2, 3 		//top right
		});
	}
	
	public static Mesh createTexturedMesh(float left, float bottom, float right, float top, float[] tcs) {
		return new Mesh()
		.putVertices(new float[]{
			left, top,
			left, bottom,
			right, bottom,
			right, top
		})
		.putTexCoords(tcs)
		.putIndices(new int[]{ //indices
			3, 0, 1,		//bottom left
			1, 2, 3 		//top right
		});
	}
}
