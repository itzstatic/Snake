package com.brandon.snake.graphics;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.brandon.snake.util.BufferUtil;


public class Mesh {
	private int vao, vbo, ibo, tbo, cbo;
	private int count;
	
	public Mesh() {
		vao = glGenVertexArrays();
	}
	
	public Mesh putVertices(FloatBuffer vertexBuffer) {
		glBindVertexArray(vao);
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
		
		return this;
	}
	
	public Mesh putVertices(float[] vertices) {
		return putVertices(BufferUtil.createFlippedBuffer(vertices));
	}
	
	public Mesh putTexCoords(FloatBuffer coordBuffer) {
		glBindVertexArray(vao);
		tbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, coordBuffer, GL_STATIC_DRAW);
		
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		
		return this;
	}
	
	public Mesh putTexCoords(float[] texCoords) {
		return putTexCoords(BufferUtil.createFlippedBuffer(texCoords));
	}
	
	public Mesh putColors(FloatBuffer colorBuffer) {
		glBindVertexArray(vao);
		cbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, cbo);
		glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
		
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0); 
		
		return this;
	}
	
	public Mesh putColors(float[] colors) {
		return putColors(BufferUtil.createFlippedBuffer(colors));
	}
	
	public Mesh putIndices(IntBuffer indexBuffer) {
		glBindVertexArray(vao);
		count = indexBuffer.limit();
		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
		
		return this;
	}
	
	public Mesh putIndices(int[] indices) {
		return putIndices(BufferUtil.createFlippedBuffer(indices));
	}
	
	
	public void bind() {
		glBindVertexArray(vao);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
	}
	
	public void render() {
		bind();
		glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
	}
	
	public void unbind() {
		glBindVertexArray(0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void destroy() {
		glDeleteBuffers(vbo);
		glDeleteBuffers(ibo);
		glDeleteBuffers(tbo);
		glDeleteBuffers(cbo);
		glDeleteVertexArrays(vao);
	}
}
