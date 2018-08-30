package com.koowalk.shop.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import com.koowalk.shop.guis.GUIComponent;
import com.koowalk.shop.guis.GUIImage;
import com.koowalk.shop.guis.GUITypeIdentifier;

public class GUIRenderEngine {
	GLProgram program;
	
	HashMap<Long, Integer> vaos;
	
	public GUIRenderEngine() {
		vaos = new HashMap<Long, Integer>();
		createPrograms();
	}
	
	private void createPrograms() {
		program = new GLProgram();
		try {
			program.loadShader("gui_shaders/image_vertex.glsl", GL_VERTEX_SHADER);
			program.loadShader("gui_shaders/image_fragment.glsl", GL_FRAGMENT_SHADER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		program.link();
	}
	
	public void drawComponent(GUIComponent component) {
		int vao;
		
		if (vaos.containsKey(component.getUID())) {
			vao = vaos.get(component.getUID());
		} else {
			vao = createVAO(component);
		}
		
		glBindVertexArray(vao);
		program.use();
		
		if (component.getType() == GUITypeIdentifier.TYPE_IMAGE) {
			drawImage((GUIImage)component);
		}
	}
	
	private void drawImage(GUIImage image) {
		program.use();
		image.getImage().use(0, program, "tex");
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
	}
	
	private int createVAO(GUIComponent component) {
		int vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		if (component.getType() == GUITypeIdentifier.TYPE_IMAGE) {
			loadImageVAO((GUIImage) component);
		}
		
		vaos.put(component.getUID(), vao);
		System.out.println(component.getUID());
		return vao;
	}
	
	private void loadImageVAO(GUIImage image) {
		int dataBuffer = glGenBuffers();
		int attrBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, dataBuffer);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, attrBuffer);
		
		
		float[] data = new float[] 
					   {image.getX(),                     image.getY(),                      0, 1,
						image.getX() + image._getWidth(), image.getY(),                      1, 1,
						image.getX() + image._getWidth(), image.getY() + image._getHeight(), 1, 0,
						image.getX()                    , image.getY() + image._getHeight(), 0, 0};
		
		int[] indices = new int[] {3, 1, 0, 3, 2, 1};
		
		System.out.println(Arrays.toString(data));
		
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
				
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, Float.BYTES*4, 0);
		
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES*4, Float.BYTES*2);
	}
}