package com.koowalk.shop.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

import com.koowalk.shop.graphics.Font.Alignment;
import com.koowalk.shop.guis.GUIComponent;
import com.koowalk.shop.guis.GUIFrame;
import com.koowalk.shop.guis.GUIImage;
import com.koowalk.shop.guis.GUILabel;
import com.koowalk.shop.guis.GUITypeIdentifier;
import com.koowalk.shop.util.Logger;

public class GUIRenderEngine {
	public static final String SHADER_DIRECTORY = "gui_shaders/";
	public static final String[] SHADER_NAMES = new String[] {"image", "text", "frame"};
	
	class RenderData {
		private int arrayBuffer;
		private int elementArrayBuffer;
		private int vao;
		
		public RenderData(int arrayBuffer, int elementArrayBuffer, int vao) {
			this.arrayBuffer = arrayBuffer;
			this.elementArrayBuffer = elementArrayBuffer;
			this.vao = vao;
		}
		
		public int getArrayBuffer() {
			return arrayBuffer;
		}
		
		public int getElementArrayBuffer() {
			return elementArrayBuffer;
		}
		
		public int getVAO() {
			return vao;
		}
	}
		
	abstract class RenderMode {
		private final GLProgram program;
		private HashMap<Long, RenderData> dataMap;
		
		public abstract void draw(GUIComponent component);
		public abstract void fillBuffers(GUIComponent component);
		public abstract String getProgramName();
		
		public void bindRenderData(GUIComponent component) {
			if (!dataMap.containsKey(component.getUID())) {
				dataMap.put(component.getUID(), new RenderData(glGenBuffers(), glGenBuffers(), glGenVertexArrays()));
			}
			
			RenderData item = dataMap.get(component.getUID());
			glBindVertexArray(item.getVAO());
			glBindBuffer(GL_ARRAY_BUFFER, item.getArrayBuffer());
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, item.getElementArrayBuffer());
		}
		
		public RenderMode() {
			this.program = new GLProgram();
			dataMap = new HashMap<>();
			loadProgram(getProgramName());
		}
		
		public GLProgram getProgram() {
			return this.program;
		}
		
		public void useProgram() {
			this.program.use();
		}
		
		private void loadProgram(String program) {
			try {
				this.program.loadShader(SHADER_DIRECTORY + program + "_vertex.glsl", GL_VERTEX_SHADER);
				this.program.loadShader(SHADER_DIRECTORY + program + "_fragment.glsl", GL_FRAGMENT_SHADER);
				this.program.link();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class FrameRenderMode extends RenderMode {
		
		@Override
		public void draw(GUIComponent component) {
			GUIFrame frame = (GUIFrame) component;
			
			Color color = frame.getColor();
			if (color.getAlpha() != 0) {
				float[] colorComponents = new float[4];
				color.getComponents(colorComponents);
				getProgram().setUniform4Vec("color", colorComponents);
			}
			
			getProgram().setUniform2Vec("offset", new float[] {frame.getX(), frame.getY()});
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		}

		@Override
		public void fillBuffers(GUIComponent component) {
			GUIFrame frame = (GUIFrame)component;
			
			float[] data = new float[] 
						   {0,                      0,                     
							frame.getPaddedWidth(), 0,                    
							frame.getPaddedWidth(), frame.getPaddedHeight(),
							0,                      frame.getPaddedHeight()};
			
			int[] indices = new int[] {3, 1, 0, 3, 2, 1};
					
			glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
					
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 2, GL_FLOAT, false, Float.BYTES*2, 0);
			
		}

		@Override
		public String getProgramName() {
			return "frame";
		}
	}
	
	class ImageRenderMode extends RenderMode {
		
		@Override
		public void draw(GUIComponent component) {
			GUIImage image = (GUIImage) component;
			
			image.getImage().use(0, getProgram(), "tex");
			getProgram().setUniform2Vec("offset", new float[] {image.getPaddedX(), image.getPaddedY()});
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		}

		@Override
		public void fillBuffers(GUIComponent component) {
			GUIImage image = (GUIImage)component;
			
			float[] data = new float[] 
						   {0,                0,                 0, 1,
							image.getWidth(), 0,                 1, 1,
							image.getWidth(), image.getHeight(), 1, 0,
							0,                image.getHeight(), 0, 0};
			
			int[] indices = new int[] {3, 1, 0, 3, 2, 1};
					
			glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
					
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 2, GL_FLOAT, false, Float.BYTES*4, 0);
			
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES*4, Float.BYTES*2);
			
		}

		@Override
		public String getProgramName() {
			return "image";
		}
		
	}
	
	class LabelRenderMode extends RenderMode {

		@Override
		public void draw(GUIComponent component) {
			GUILabel label = (GUILabel) component;
			
			// Grab the color components and stick them into the shader
			Color color = label.getColor();
			float[] colorComponents = new float[4];
			color.getComponents(colorComponents);
			getProgram().setUniform4Vec("color", colorComponents);
			
			// Use the font sheet for rendering
			label.getFont().useTexture(getProgram(), "tex");
			getProgram().setUniform2Vec("offset", new float[] {label.getPaddedX(), label.getPaddedY()});
			glDrawElements(GL_TRIANGLES, label.getVertexCount(), GL_UNSIGNED_INT, 0);
		}

		@Override
		public void fillBuffers(GUIComponent component) {
			GUILabel label = (GUILabel) component;
			label.getFont().fillBuffers(0, 0, label.getRenderTarget(), Alignment.LEFT);
		}

		@Override
		public String getProgramName() {
			return "text";
		}
		
	}
	
	private RenderMode[] renderModes;
	
	HashMap<Long, Integer> vaos;
	
	public GUIRenderEngine() {
		vaos = new HashMap<Long, Integer>();
		renderModes = new RenderMode[GUITypeIdentifier.TYPE_COUNT];
		Logger.info("Loading GUI render modes...");
		Logger.info("- Frame");
		renderModes[GUITypeIdentifier.TYPE_FRAME.getIndex()] = new FrameRenderMode();
		Logger.info("- Image");
		renderModes[GUITypeIdentifier.TYPE_IMAGE.getIndex()] = new ImageRenderMode();
		Logger.info("- Label");
		renderModes[GUITypeIdentifier.TYPE_LABEL.getIndex()] = new LabelRenderMode();
	}
	
	public void drawComponent(GUIComponent component) {
		RenderMode mode = renderModes[component.getType().getIndex()];
		mode.bindRenderData(component);
		mode.useProgram();
		if (component.hasBeenUpdated()) {
			Logger.info("Updating buffers");
			mode.fillBuffers(component);
		}
		mode.draw(component);
	}
}