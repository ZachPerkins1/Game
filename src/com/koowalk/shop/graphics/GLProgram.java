package com.koowalk.shop.graphics;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents a compiled OpenGL shader program
 */
public class GLProgram {
	private int glId;
	private ArrayList<Integer> shaders;
	
	/**
	 * Initialize a new program
	 */
	public GLProgram() {
		glId = glCreateProgram();
		shaders = new ArrayList<Integer>(5);
	}
	
	/**
	 * Load a new shader into the program by its name and type.
	 * @param filename The filename of the shader.
	 * @param type The shader type, one of: One of:
	 * {@link org.lwjgl.opengl.GL20#VERTEX_SHADER}  {@link org.lwjgl.opengl.GL20#FRAGMENT_SHADER}
	 * {@link org.lwjgl.opengl.GL20#GEOMETRY_SHADER}  {@link org.lwjgl.opengl.GL20#TESS_CONTROL_SHADER}
	 * {@link org.lwjgl.opengl.GL20#TESS_EVALUATION_SHADER}
	 * @throws IOException If the given file cannot be read
	 */
	public void loadShader(String filename, int type) throws IOException {
		int id = glCreateShader(type);
		GLShaderParser parser = new GLShaderParser(filename);
		String file = parser.parse();
		
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
	
	/**
	 * Link the shader program and get rid of the shader source in memory. This is
	 * OpenGLs equivalent of compiling.
	 */
	public void link() {
		glLinkProgram(glId);
		
		// Remove all of the shader stuff from memory
		for (int shader : shaders) {
			glDetachShader(glId, shader);
			glDeleteShader(shader);
		}
		
		shaders.clear();
		
		if (glGetProgrami(glId, GL_LINK_STATUS) != GL_TRUE) {
			System.err.println(glGetProgramInfoLog(glId, 1000));
		}
	}
	
	/**
	 * Just a wrapper around glUseProgam that allows doing so without breaking the illusion
	 * of all of this being object oriented
	 */
	public void use() {
		glUseProgram(glId);
	}
	
	/**
	 * Get the (integer) uniform location based on a (string) uniform variable name
	 * @param uniform The name of the uniform variable
	 * @return The integer OpenGL addresses this particular uniform by
	 * @throws IllegalArgumentException If the uniform variable name does not exist
	 */
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
