package com.brandon.snake.graphics;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;

import com.brandon.snake.graphics.texture.TextureAtlas;
import com.brandon.snake.math.Matrix4f;

public class BitmapFont {

	private TextureAtlas texture;
	private Shader shader;
	private Matrix4f ortho;
	
	private Map<String, Mesh> meshCache;
	private Map<String, Matrix4f> originCache;
	
	//Dimensions of SCREEN measured by Bitmap cell
	private float width;
	private float height;
	
	
	public BitmapFont(BufferedImage image, int cols, int rows, float aspectMod, int windowWidth, int windowHeight) {
		texture = new TextureAtlas(image, cols, rows, aspectMod);
		construct(windowWidth, windowHeight);
	}
	public BitmapFont(BufferedImage image, int cols, int rows, int windowWidth, int windowHeight) {
		texture = new TextureAtlas(image, cols, rows, 1);
		construct(windowWidth, windowHeight);
	}
	
	public BitmapFont(String path, int cols, int rows, float aspectMod, int windowWidth, int windowHeight) {
		texture = new TextureAtlas(path, cols, rows, aspectMod);
		construct(windowWidth, windowHeight);
	}
	public BitmapFont(String path, int cols, int rows, int windowWidth, int windowHeight) {
		texture = new TextureAtlas(path, cols, rows, 1);
		construct(windowWidth, windowHeight);
	}
	
	private void construct(int windowWidth, int windowHeight) {
		width = windowWidth / (float) texture.getCellWidth();
		height = windowHeight / (float) texture.getCellHeight();
		ortho = Matrix4f.orthographic(0, width, 0, height, 1, -1);
		meshCache = new HashMap<>();
		originCache = new HashMap<>();
	}
	
	public void bind(int channel) { 
		texture.bind(channel);
		shader.bind();
		shader.setUniformMat4f("proj", ortho);
	}
	
	public void unbind() {
		texture.unbind();
		shader.unbind();
	}
	
	/**
	 * Draws a centered string
	 * @param s the string
	 * @param scale multiplies size; position unchanged
	 */
	public void drawString(String s, float scale) {
		s = s.trim(); //any newline characters must have non whitespace characters on each side. That is, no newlines on the outside allowed
		
		Mesh mesh = getMesh(s);
		
		float x = (float) width / 2;
		float y = (float) height / 2;
		Matrix4f model = Matrix4f.translate(x, y, 0).mul(Matrix4f.scale(scale, scale, 1));
		
		renderString(mesh, model);
	}
	public void drawString(String s) {
		drawString(s, 1);
	}
	
	/**
	 * Draws a string anywhere
	 * @param s the string
	 * @param x left coordinant (by bitmap characters fit to screen)
	 * @param y bottom coordinant
	 * @param scale multiplies size; position may be wonky
	 */
	public void drawString(String s, float x, float y, float scale) {
		s = s.trim();
		
		Mesh mesh = getMesh(s); //getMesh(s) guarantees the presence of an origin for s after its call
		Matrix4f origin = originCache.get(s);
		
		Matrix4f model = Matrix4f.translate(x, y, 0).mul(Matrix4f.scale(scale, scale, 1).mul(origin));
		
		renderString(mesh, model);
	}
	
	public void drawString(String s, float x, float y) {
		drawString(s, x, y, 1);
	}
	
	/**
	 * Renders the string mesh using the model
	 * @param mesh the string mesh
	 * @param model the model pertaining to the exact location of the string
	 */
	private void renderString(Mesh mesh, Matrix4f model) {
		bind(0);
		shader.setUniformMat4f("model", model);
		mesh.render();
	}
	
	/**
	 * Obtains a mesh for the particular string. If necessary, creates a mesh and origin
	 * Side Effect: an origin and mesh for the string are guaranteed to be available after this method call
	 * @param s the string
	 * @return the mesh
	 */
	private Mesh getMesh(String s) {
		Mesh mesh = meshCache.get(s);
		if (mesh == null) { //s is a key in meshCache iff it is a key in originCache
			int length = s.length(); //including internal newlines
			String[] lines = s.split("\\n");
			// - lines.length + 1 to compensate for newline characters
			// that is, boxes = sum of lines[i].length() for all lines
			int boxes = length - lines.length + 1;
			
			mesh = createMesh(lines, boxes);
			
			meshCache.put(s, mesh);
			originCache.put(s, createOrigin(lines));
		}
		return mesh;
	}
	
	/**
	 * Creates a matrix that will translate the origin from the center of the mesh to the lower left corner of the smallest bounding rectangle around the mesh
	 * @param lines array of lines
	 * @return the translation matrix
	 */
	private Matrix4f createOrigin(String[] lines) {
		int maxWidth = 0;
		for (String line : lines) {
			maxWidth = Math.max(maxWidth, line.length());
		}
		
		float x = (float) maxWidth / 2;
		float y = (float) lines.length / 2;
		
		return Matrix4f.translate(x, y, 0);
	}
	
	
	/**
	 * Creates a mesh of some text centered at the origin
	 * @param lines array of the lines
	 * @param length the number of characters; sum lines[i].length()
	 * @return the mesh
	 */
	private Mesh createMesh(String[] lines, int boxes) {
		FloatBuffer vertices = BufferUtils.createFloatBuffer(boxes * 4 * 2);
		IntBuffer indices = BufferUtils.createIntBuffer(boxes * 2 * 3);
		FloatBuffer texCoords = BufferUtils.createFloatBuffer(boxes * 4 * 2); 
		
		int box = 0; //Character index
		for (int i = 0; i < lines.length; i++) {
//			//Top left of this line (Top left of first line is at origin)
//			float x = - (lines[i].length() - lines[0].length()) / 2;
//			float y = - i;
			float x = - (float) lines[i].length() / 2;
			float y = (float) lines.length / 2 - i;
			
			putLine(lines[i], x, y, box, vertices, texCoords, indices);
			box += lines[i].length(); //Increment the character index
		}
		
		vertices.flip();
		indices.flip();
		texCoords.flip();
		
		return new Mesh().putVertices(vertices).putTexCoords(texCoords).putIndices(indices);
	}
	
	/**
	 * All buffers unflipped
	 * @param line the text in the line
	 * @param x left of the line
	 * @param y top of the line
	 * @param box the character index of the first character in the string
	 * @param vertices the buffer to add position data to
	 * @param texCoords the buffer to add texture coordinate data to
	 * @param indices the buffer to add index data to
	 */
	private void putLine(String line, float x, float y, int box, FloatBuffer vertices, FloatBuffer texCoords, IntBuffer indices) {
		int length = line.length();
		for (int i = 0; i < length; i++) {
			putVerticies(vertices, x + i, y);
			putIndicies(indices, box);
			putTexCoords(texCoords, line.charAt(i));
			box++; //Increment the character index
		}
	}
	
	/**
	 * 
	 * @param vertices positions in buffer
	 * @param x left of this character
	 * @param y top of this character
	 * @param w width of this character
	 * @param h height of this character
	 */
	private void putVerticies(FloatBuffer vertices, float x, float y) {
		//top left, CCW
		vertices.put(x).put(y);
		vertices.put(x).put(y - 1);
		vertices.put(x + 1).put(y -1);
		vertices.put(x + 1).put(y);
	}
	
	/**
	 * Places indices in buffer
	 * @param indices buffer to store all indices
	 * @param i index of the character in the string
	 */
	private void putIndicies(IntBuffer indices, int i) {
		int tl = 4 * i; //Top left, bottom left, and others.
		int bl = tl + 1;
		int br = bl + 1;  
		int tr = br + 1; 
		indices.put(bl).put(br).put(tl); //Bottom left triangle CCW
		indices.put(tr).put(tl).put(br); //Top right triangle CCW
	}
	
	/**
	 * 
	 * @param texCoords texture coordinates in buffer
	 * @param c the character
	 */
	private void putTexCoords(FloatBuffer texCoords, char c) {
		texCoords.put(texture.getTextureCoords((int) c));
	}

	public void setShader(Shader s, int texIndex, String sampler) {
		shader = s;
		shader.bind();
		texture.bind(texIndex);
		shader.setUniform1f(sampler, texIndex);
	}

	public void destroyString(String s) {
		if (meshCache.containsKey(s)) {
			meshCache.remove(s).destroy();
		}
		
		System.out.println("Destroyed " + s + ": " + meshCache.size());
	}
	
	public void destroy() {
		meshCache.values().forEach(e -> e.destroy());
		shader.destroy();
		texture.destroy();
	}
}
