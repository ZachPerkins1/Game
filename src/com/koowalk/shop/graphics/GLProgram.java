package com.koowalk.shop.graphics;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GLProgram {
	private int glId;
	private ArrayList<Integer> shaders;
	
	public GLProgram() {
		glId = glCreateProgram();
		shaders = new ArrayList<Integer>(5);
	}
	
	public void loadShader(String filename, int type) throws IOException {
		int id = glCreateShader(type);
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(filename));
		String file = "";
		String line;
		
		while ((line = reader.readLine()) != null) {
			file += line + "\n";
		}
		
		reader.close();
		
		glShaderSource(id, file);
		glCompileShader(id);
		
		if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_TRUE) {
			glAttachShader(glId, id);
			shaders.add(id);
		} else {
			System.err.println("Error attaching shader");
			System.err.println(glGetShaderInfoLog(id, 1000));
		}
	}
	
	public void link() {
		glLinkProgram(glId);
		
		for (int shader : shaders) {
			glDetachShader(glId, shader);
			glDeleteShader(shader);
		}
		
		shaders.clear();
		
		if (glGetProgrami(glId, GL_LINK_STATUS) != GL_TRUE) {
			System.err.println(glGetProgramInfoLog(glId, 1000));
		}
	}
	
	public void use() {
		glUseProgram(glId);
	}
	
	public int getUniformLocation(String uniform) throws IllegalArgumentException {
		int loc = glGetUniformLocation(glId, uniform);
		if (loc == -1) {
			throw new IllegalArgumentException("Uniform variable " + uniform + " does not exist");
		}
		
		return loc;
	}
	
	public void setUniform(String name, int value) {
		glUniform1i(getUniformLocation(name), value);
	}
	
	public void setUniform(String name, float value) {
		glUniform1f(getUniformLocation(name), value);
	}
	
	public void setUniform(String name, int[] value) {
		glUniform1iv(getUniformLocation(name), value);
	}
	
	public void setUniform(String name, float[] value) {
		glUniform1fv(getUniformLocation(name), value);
	}
	
	public void setUniform2Vec(String name, float[] value) {
		glUniform2fv(getUniformLocation(name), value);
	}
	
	public void setUniform4Vec(String name, float[] value) {
		glUniform4fv(getUniformLocation(name), value);
	}

	public void setUniform4Matrix(String name, float[] value) {
		glUniformMatrix4fv(getUniformLocation(name), true, value);
	}
	
	public void setUniformTranslationMatrix(String name, float tx, float ty) {
		glUniformMatrix4fv(getUniformLocation(name), true, new float[] {
				1, 0, 0, tx,
				0, 1, 0, ty,
				0, 0, 1, 0,
				0, 0, 0, 1
		});
	}
}
