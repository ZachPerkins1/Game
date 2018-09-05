package com.koowalk.shop.world.entity;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import com.koowalk.shop.Util;
import com.koowalk.shop.pathfinding.APoint;
import com.koowalk.shop.pathfinding.APointList;
import com.koowalk.shop.pathfinding.InvalidPathException;
import com.koowalk.shop.world.Camera;
import com.koowalk.shop.world.World;

public class Entity {
	private static final double AUTO_MOVE_SPEED = 2;
	
	public double x;
	public double y;
	public double dx;
	public double dy;
	
	// width/height
	private int w;
	private int h;
	
	private World world;
		
	private Point[] path;
	private int pathLen;
	private int currentTarget;
	private boolean autoMove;
	
	private boolean hasCollided;
	
	private Point pTarget; // The target block in pixels
	

	public Entity(World world, int x, int y) {
		this.x = x;
		this.y = y;
		this.w = World.BLOCK_SIZE - 6;
		this.h = World.BLOCK_SIZE - 6;
		this.world = world;
		
		this.hasCollided = false;
	}
	
	public void draw(Camera cam) {
		
	}
	
	public void update() {
		if (autoMove) 
			autoMove = autoMove();
	}
	
	public void setTarget(Point target) throws InvalidPathException {
		if (!target.equals(this.pTarget)) {
			this.pTarget = target;
			
			// Convert the path to an array
			Point adjustedTarget = world.pixel2coord(target.x, target.y);
			path = pathFind(new Point(adjustedTarget.x, adjustedTarget.y)).toArray(new Point[0]);
			pathLen = path.length;
			currentTarget = 0;
			autoMove = true;
		}
	}
	
	public void cancelTarget() {
		if (autoMove) {
			autoMove = false;
			zero();
		}
	}
	
	public boolean hasTarget() {
		return autoMove;
	}
	
	// Set the velocity of the entity to be what it needs to be to get to the next
	// point in path
	private boolean autoMove() {
		Point target = path[currentTarget];
		
		// We have collided with something so the path-finding is over
		if (hasCollided && dx == 0 && dy == 0)
			return false;
		
		if (currentTarget == pathLen - 1)
			target = pTarget;
		
		if (!Util.equals(x, target.x) || !Util.equals(y, target.y)) {
			double xDist = (target.x - x);
			double yDist = (target.y - y);
							 
			if (yDist > 0) {
				dy = Math.min(AUTO_MOVE_SPEED, yDist);
			} else if (yDist < 0) {
				dy = Math.max(-AUTO_MOVE_SPEED, yDist);
			} else {
				dy = 0;
			}
			
			if (xDist > 0) {
				dx = Math.min(AUTO_MOVE_SPEED, xDist);
			} else if (xDist < 0) {
				dx = Math.max(-AUTO_MOVE_SPEED, xDist);
			} else {
				dx = 0;
			}
		} else {
			dx = 0; 
			dy = 0;
			
			if (++currentTarget >= pathLen) {
				return false;
			}
		}
		
		return true;
	}
	
	// Use A* to calculate a new path for the entity. Stores it in path variable.
	private ArrayList<Point> pathFind(Point target) throws InvalidPathException {
		Point g = world.pixel2coord(x, y);
		
		// Check to see if the block we're trying to get to is collidable
		if (world.isSolid(g.x, g.y)) 
			throw new InvalidPathException(target);
		
		APointList openList = new APointList();
		APointList closedList = new APointList();
		APoint current = new APoint(target, g.x, g.y);
		closedList.add(current);
				
		while (!current.equals(target)) {
			openList.addSurrounding(world, current, closedList, target);
			
			try {
				current = openList.getLowest();
			} catch (IndexOutOfBoundsException e) {
				throw new InvalidPathException();
			}
			
			openList.remove(current);
			closedList.n_add(current);
		}
		
		ArrayList<Point> f = new ArrayList<Point>();
		
		APoint p = current;
		APoint d = null; // last point
		
		// Stores all the points such that only changes in direction are saved
		while (!p.equals(new Point(g.x, g.y))) {
			if (d != null)  {
				Point z = p.getParent();
				
				// Check to see if there is a turn
				if ((d.x == p.x && p.x != z.x) ||
					(d.y == p.y && p.y != z.y)) {
					f.add(0, p.getAdjusted(World.BLOCK_SIZE));
				}
			} else {
				f.add(0, p.getAdjusted(World.BLOCK_SIZE));
			}
			
			d = p;
			p = p.getParent();
		}
				
		return f;
	}
	
	public int getW() {
		return w;
	}
	
	public int getH() {
		return h;
	}
	
	public void zero() {
		dx = 0;
		dy = 0;
	}
}
