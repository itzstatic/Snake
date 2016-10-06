package com.brandon.snake.graphics;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.util.HashMap;
import java.util.Map;

import com.brandon.snake.math.Matrix4f;
import com.brandon.snake.math.Vector3f;
import com.brandon.snake.util.FileUtil;

public class Shader {
	
	private int id;
	private Map<String, Integer> uniforms;
	
	public Shader(String vertPath, String fragPath) {
		uniforms = new HashMap<>();
		id = glCreateProgram();
		
		int vertID = glCreateShader(GL_VERTEX_SHADER);
		int fragID = glCreateShader(GL_FRAGMENT_SHADER);
		
		initShader(vertID, FileUtil.read(vertPath));
		initShader(fragID, FileUtil.read(fragPath));
		
		glLinkProgram(id);
		glValidateProgram(id);
		
		glDeleteShader(vertID);
		glDeleteShader(fragID);
	}
	
	private void initShader(int id, String source) {
		glShaderSource(id, source);
		glCompileShader(id);
		if (glGetShaderi(id, GL_COMPILE_STATUS) != GL_TRUE) {
			throw new IllegalStateException("Shader compiled unsuccessfully.");
		}
		glAttachShader(this.id, id);
	}
	
	public void bind() {
		glUseProgram(id);
	}
	
	public void unbind() {
		glUseProgram(0);
	}
	
	public void destroy() {
		glDeleteProgram(id);
	}
	
	private int getUniformLocation(String name) {
		if (uniforms.containsKey(name)) {
			return uniforms.get(name);
		}
		
		int location = glGetUniformLocation(id, name);
		uniforms.put(name, location);
		return location;
	}
	
	public void setUniform1f(String name, float f) {
		glUseProgram(id);
		glUniform1f(getUniformLocation(name), f);
	}
	
	public void setUniform3f(String name, Vector3f v) {
		glUseProgram(id);
		glUniform3f(getUniformLocation(name), v.getX(), v.getY(), v.getZ());
	}
	
	public void setUniformMat4f(String name, Matrix4f mat) {
		glUseProgram(id);
		glUniformMatrix4fv(getUniformLocation(name), false, mat.toFloatBuffer());
	}
}
