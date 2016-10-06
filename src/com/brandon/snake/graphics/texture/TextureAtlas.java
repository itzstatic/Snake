package com.brandon.snake.graphics.texture;

import java.awt.image.BufferedImage;

import com.brandon.snake.graphics.Texture;

public class TextureAtlas extends Texture {

	private int cols;
	private int rows;
	
	private float scale; //An aspect ratio modification of each cell to make it prettier: x / y scale
	
	public TextureAtlas(BufferedImage image, int cols, int rows, float aspectRatioMod) {
		super(image);
		construct(cols, rows, aspectRatioMod);
	}
	public TextureAtlas(BufferedImage image, int cols, int rows) {
		this(image, cols, rows, 1);
	}
	
	public TextureAtlas(String path, int cols, int rows, float aspectRatioMod) {
		super(path);
		construct(cols, rows, aspectRatioMod);
	}
	public TextureAtlas(String path, int cols, int rows) {
		this(path, cols, rows, 1);
	}
	
	private void construct(int cols, int rows, float aspectRatioMod) {
		this.cols = cols;
		this.rows = rows;
		scale = aspectRatioMod;
	}
	
	/**
	 * 
	 * @param col the column that the cell is in
	 * @param row the row
	 * @return texture coordinates for the cell
	 */
	public float[] getTextureCoords(int col, int row) {
		float u = (float) col / cols;
		float v = (float) row / rows;
		
		float du = (float) 1 / cols;
		float dv = (float) 1 / rows;
		
		float[] tcs = new float[]{
			u, v,
			u, v + dv,
			u + du, v + dv,
			u + du, v
		};
		return tcs;
	}
	
	/**
	 * 
	 * @param i the index of the cell in:            0 | 1 | 2
	 * 												 3 | 4 | 5 ... etc
	 * @return texture coordinates for the cell		 
	 */
	public float[] getTextureCoords(int i) {
		int col = i % cols;
		int row = i / cols;
		
		return getTextureCoords(col, row);
	}
	
	public int getColumns() {
		return cols;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getCellWidth() {
		return (int)(getWidth() / cols * scale);
	}
	
	public int getCellHeight() {
		return (int)(getHeight() / rows); //Only divide the y-dimension by scale because it is x / y
	}

}
