package com.brandon.snake.util;


import com.brandon.snake.game.Cell;
import com.brandon.snake.graphics.Mesh;
import com.brandon.snake.math.Matrix4f;

public class MeshUtil {
	private MeshUtil(){}
	
	public static Mesh createMesh(float left, float bottom, float right, float top) {
		return new Mesh()
		.putVertices(new float[]{ //positions
			left, bottom,
			right, bottom,
			right, top,
			left, top
		}).putIndices(new int[]{ //indices
			3, 0, 1,		//bottom left
			1, 2, 3 		//top right
		});
	}
	
	public static Mesh createEntityMesh() {
		float d = 2 / 32f; //border
		return createMesh(d, d, 1 - d, 1 - d);
	}
	
	
	public static Mesh createSegmentMesh(float left, float bottom, float right, float top, float[] tcs) {
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
	

	public static Matrix4f getCellTranslation(Cell cell) {
		//the .5f displacement moves the origin from the center of the mesh to the bottom left.
		return Matrix4f.translate(cell.getX() + .5f, cell.getY() + .5f, 0);
	}
}
