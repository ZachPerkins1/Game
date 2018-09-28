package com.koowalk.shop;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import com.koowalk.shop.graphics.ChunkRenderEngine;
import com.koowalk.shop.graphics.FontLoader;
import com.koowalk.shop.graphics.GLTexture;
import com.koowalk.shop.graphics.GLTextureArray;
import com.koowalk.shop.graphics.TextureRegistry;
import com.koowalk.shop.guis.DimensionMeasurement;
import com.koowalk.shop.guis.DimensionMeasurement.Mode;
import com.koowalk.shop.guis.GUIFrame;
import com.koowalk.shop.guis.GUIImage;
import com.koowalk.shop.guis.GUILabel;
import com.koowalk.shop.guis.GUILayoutSettingsAbsolute;
import com.koowalk.shop.guis.GUILayoutSettingsGrid;
import com.koowalk.shop.guis.GUIManager;
import com.koowalk.shop.guis.GUILayoutGrid;

import java.awt.Color;
import java.io.IOException;
import java.nio.*;
import java.util.HashMap;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.koowalk.shop.guis.GUILayoutSettingsGrid.Sticky;
import com.koowalk.shop.util.Logger;

public class Window {
	public static int WIDTH = 800;
	public static int HEIGHT = 800;
	
	private long timer;
	private int frameCount;

	// The window handle
	private long window;
	
	private Game game;

	public void run() {
		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	private void loadTextures() {
		GLTextureArray blocks = new GLTextureArray(32, 32);
		blocks.add("texture.png");
		TextureRegistry.add(blocks);
		
		TextureRegistry.add(new GLTexture("player.png"));
		
		TextureRegistry.load();
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();
		Logger.init();
		
		Logger.info("Initializing GLFW");
		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_SAMPLES, 4);

		// Create the window
		Logger.info("Creating window");
		window = glfwCreateWindow(800, 800, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {			
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			else {
				if (action == GLFW_PRESS) 
					game.keyPressed(key, mods);
				else if (action == GLFW_RELEASE) 
					game.keyReleased(key, mods);
			}
		});
		
		glfwSetWindowCloseCallback(window, (window) -> {
			Logger.info("Window close detected");
			Logger.info("Saving gamestate");
			game.windowClosing();
			Logger.info("Saving log (I wish I could stay longer)");
			Logger.finish();
		});
		
		glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
			double[] x = new double[1];
			double[] y = new double[1];
 			glfwGetCursorPos(window, x, y);
 			
 			if (action == GLFW_PRESS)
 				game.mouseClicked(button, x[0], y[0]);
		});

		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); 
			IntBuffer pHeight = stack.mallocInt(1);
			
			glfwGetWindowSize(window, pWidth, pHeight);

			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		}
		
		glfwMakeContextCurrent(window); 
		glfwSwapInterval(0); // Enable V-Sync 0 = no vsync
		GL.createCapabilities();
		
		// Load buffer data for chunk rendering
		Logger.info("Loading textures");
		loadTextures();
		Logger.info("Loading chunk engine");
		ChunkRenderEngine.create();
		GUIImage img = new GUIImage(1);
		
		GUIManager.getInstance().getMaster().getWidthMeasurement().setAbsolute(300);
		DimensionMeasurement m = new DimensionMeasurement(Mode.RELATIVE);
		m.setRelative(1.0);
		((GUILayoutGrid)GUIManager.getInstance().getMaster().getLayoutManager()).setColumnSize(1, m);
		
		GUIFrame frame = new GUIFrame(Color.RED);
		frame.setParent(GUIManager.getInstance().getMaster(), new GUILayoutSettingsGrid(0,0,0,0,0,0,Sticky.POSITIVE, Sticky.POSITIVE));
		frame.setPadding(10);
		
		GUIFrame frame2 = new GUIFrame(Color.BLACK);
		frame2.setParent(GUIManager.getInstance().getMaster(), new GUILayoutSettingsGrid(1,0,0,0,0,0, Sticky.NEGATIVE, Sticky.NEUTRAL));
		
		GUIFrame frame3 = new GUIFrame(Color.GREEN);
		frame3.setParent(GUIManager.getInstance().getMaster(), new GUILayoutSettingsGrid(1,1,0,0,0,0, Sticky.NEUTRAL, Sticky.NEUTRAL));
		frame3.getHeightMeasurement().setRelative(1.0);
		
		// frame.setPadding(20);
		// img.setParent(GUIManager.getInstance().getMaster(), attr);
		
		Logger.info("Loading fonts");
		try {
			FontLoader.getInstance().load("OpenSans-Regular.ttf", 20);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			GUILabel label = new GUILabel("OpenSans-Regular", 60, "Test");
			label.setParent(frame, new GUILayoutSettingsAbsolute(30, 0));
			
			frame2.setPadding(20);
			GUILabel label2 = new GUILabel("OpenSans-Regular", 30, "This would be some form of in-game description");
			label2.setParent(frame2, new GUILayoutSettingsAbsolute(0,0));
			label2.getWidthMeasurement().setAbsolute(75);
			
			GUILabel label3 = new GUILabel("OpenSans-Regular", 30, "Goodbye");
			label3.setParent(frame3, new GUILayoutSettingsAbsolute(0,0));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// FontLoader.getInstance().getPreloaded("OpenSans-Regular", 30).fillBuffers(1, 1, 0, 0, "hello");
		
		game = new Game();
		glfwShowWindow(window);
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				
		// Set the clear color
		glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
	

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {
			long start = System.currentTimeMillis();
			
			glfwPollEvents();
			game.update();
			GUIManager.getInstance().update();
			glClear(GL_COLOR_BUFFER_BIT); // clear the framebuffer
			game.draw();
			GUIManager.getInstance().draw();
			glFlush();
			
		    glfwSwapBuffers(window);
		    
		    long end = System.currentTimeMillis();
		    
		    try {
				Thread.sleep(Math.max((1000/60) - ((end - start)), 0));
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
		    long currentTime = System.currentTimeMillis();
		    if (currentTime - timer >= 1000*60) {
		    	if (timer != 0) {
		    		Logger.info("Average framerate is " + (frameCount * 1.0)/((currentTime - timer)/1000.0));
		    	}
		    	
		    	timer = System.currentTimeMillis();
		    	frameCount = 0;
		    }
		    
		    frameCount++;
		}
	}
	
	public static void main(String[] args) {
		new Window().run();
	}
}
