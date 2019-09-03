package com.koowalk.shop;
import java.awt.Point;
import com.koowalk.shop.pathfinding.InvalidPathException;
import com.koowalk.shop.util.Logger;
import com.koowalk.shop.world.Camera;
import com.koowalk.shop.world.World;
import com.koowalk.shop.world.entity.Entity;
import com.koowalk.shop.world.entity.Player;

import static org.lwjgl.glfw.GLFW.*;

public class Game {
	private static final int RIGHT = 0;
	private static final int DOWN = 1;
	private static final int LEFT = 2;
	private static final int UP = 3;
	
	private boolean[] movement;
	
	private World world;
	private Camera c;
	private Entity player;
	
	private int currBlock = 1;
	
	
	public Game() {
		movement = new boolean[4];
		
		Logger.info("Loading world file");
		GameFile g = new GameFile("hello");
		world = new World(g.getMap());
		
		Logger.info("Loading player");
		player = new Player(world, 100, 300);
		world.addEntity(player);
		c = world.getCamera();
		c.setFollowing(player);
	}
	
	public void update() {
		// Zero the speeds before we adjust them
		if (!player.hasTarget())
			player.zero();
		
		// Determine the new movement speed of the player
		for (int i = 0; i < 4; i++) {
			if (movement[i]) {
				player.cancelTarget();
				
				double m = 3;
				
				//If there is movement in the x 
				if (movement[(i+3) % 4] || movement[(i+1) % 4])
					m = 2.1;
				
				if (i > 1)
					m = -m;
				
				if (i % 2 == 0) 
					player.dx += m;
				else
					player.dy += m;	
			}
		}
						
		world.update();
		player.update();
	}
	
	public void draw() {
		world.draw();
	}
	
	public void windowClosing() {
		world.saveChunks();
	}
	
	public void mouseClicked(int button, double x, double y) {
		Point pos = world.pixel2coord(c.getX((int)x), c.getY((int)y));
		
		if (button == GLFW_MOUSE_BUTTON_RIGHT) {
			try {
				player.setTarget(new Point(c.getX((int)x), c.getY((int)y)));
			} catch (InvalidPathException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(pos);
			int id = world.blockAt(pos.x, pos.y).getID();
			System.out.println(id);
			if (id == 0) {
				world.place(pos.x, pos.y, currBlock);
			} else {
				world.place(pos.x, pos.y, 0);
			}
		}
	}
	
	
	public void keyReleased(int key, int mods) {
		if (key == GLFW_KEY_SPACE) {
			if (++currBlock > 2) {
				currBlock = 1;
			}
		}
		
		switch (key) {
		case GLFW_KEY_W:
			movement[UP] = false;
			break;
		case GLFW_KEY_S:
			movement[DOWN] = false;
			break;
		case GLFW_KEY_D:
			movement[RIGHT] = false;
			break;
		case GLFW_KEY_A:
			movement[LEFT] = false;
			break;
		}
	}
	
	public void keyPressed(int key, int mods) {
		switch (key) {
		case GLFW_KEY_W:
			movement[UP] = true;
			break;
		case GLFW_KEY_S:
			movement[DOWN] = true;
			break;
		case GLFW_KEY_D:
			movement[RIGHT] = true;
			break;
		case GLFW_KEY_A:
			movement[LEFT] = true;
			break;
		}
	}

}
