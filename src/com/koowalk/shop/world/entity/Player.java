package com.koowalk.shop.world.entity;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.IOException;

import com.koowalk.shop.graphics.GLProgram;
import com.koowalk.shop.graphics.GLTexture;
import com.koowalk.shop.world.Camera;
import com.koowalk.shop.world.World;

public class Player extends Entity {
	private static int width = World.BLOCK_SIZE - 6;
	private static int height = World.BLOCK_SIZE - 6;
	
	private static GLTexture texture;
	private static int vao;
	private static GLProgram program;

	public Player(World world, int x, int y) {
		super(world, x, y);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void update() {
		super.update();
		
		
	}
	
	public void draw(Camera cam) {		
		glBindVertexArray(vao);
		program.use();
		program.setUniformTranslationMatrix("offset", (float)x, (float)y);
		texture.use(0, program, "tex");
		cam.setUniforms(program);
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		
	}
	
	public static void load() {
		float[] vertices = new float[] {
				-(width/2), -(height/2), 0, 1,
				-(width/2), (height/2), 0, 0,
				(width/2), (height/2), 1, 0,
				(width/2), -(height/2), 1, 1
		};
		
		int[] items = new int[] {
				0, 1, 2,
			    2, 3, 0
		};
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		int buffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		int itemBuffer = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, itemBuffer);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, items, GL_STATIC_DRAW);
		
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, Float.BYTES*4, 0);
		
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES*4, Float.BYTES*2);
		
		texture = new GLTexture("player.png");
		texture.load();
		
		program = new GLProgram();
		
		try {
			program.loadShader("efragment.glsl", GL_FRAGMENT_SHADER);
			program.loadShader("evertex.glsl", GL_VERTEX_SHADER);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		program.link();
	}
}
