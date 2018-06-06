package com.koowalk.shop.world;
import com.koowalk.shop.Window;
import com.koowalk.shop.graphics.GLProgram;
import com.koowalk.shop.world.entity.Entity;

public class Camera {
	private double scale;
	private int x;
	private int y;
	
	private int w;
	private int h;
	private int fw;
	private int fh;
	
	private Entity e = null;
	
	private float[] pos;
	private static final int T_X = 3;
	private static final int T_Y = 7;
	
	public Camera(int x, int y, int sWidth, int sHeight) {
		this.x = x;
		this.y = y;
		scale = 1.0;
		
		h = sHeight;
		w = sWidth;
		fw = 200;
		fh = 200;
		
		pos = new float[] {
				1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1
		};
	}
	
	public Camera(int sWidth, int sHeight) {
		this(0, 0, sWidth, sHeight);
	}
	
	public Camera() {
		this(Window.WIDTH, Window.HEIGHT);
	}
	
	public void setCenter(int cx, int cy) {
		x = cx;
		y = cy;
	}
	
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
	}
	
	public void setFollowPort(int w, int h) {
		fw = w;
		fh = h;
	}
	
	public void setFollowing(Entity en) {
		e = en;
		setCenter((int)e.x, (int)e.y);
	}
	
	public void cancelFollow() {
		e = null;
	}
	
	public void update() {
		int fw2 = fw / 2;
		int fh2 = fh / 2;
		
		if (e != null) {
			int maxX = x + fw2;
			int minX = x - fw2;
			int maxY = y + fh2;
			int minY = y - fh2;
			
			if (e.x > maxX) {
				x += e.x - maxX;
			} else if (e.x < minX) {
				x += e.x - minX;
			}
			
			if (e.y > maxY) {
				y += e.y - maxY;
			} else if (e.y < minY) {
				y += e.y - minY;
			}
		}
		
		pos[T_X] = -x;
		pos[T_Y] = -y;
		
		//prog.setUniform4Matrix("camera", pos);
	}
	
	public int getAX(double rx) {
		return (int) (rx - x);
	}
	
	public int getAY(double ry) {
		return (int) (ry - y);
	}
	
	// given an adjusted x, get a normalized x
	public int getX(int ax) {
		return ((ax - Window.WIDTH / 2) + x);
	}
	
	public int getY(int ay) {
		return ((ay - Window.HEIGHT / 2) + y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getMinX() {
		return x - (Window.WIDTH / 2);
	}
	
	public int getMinY() {
		return y - (Window.HEIGHT / 2);
	}
	
	public int getMaxX() {
		return x + (Window.WIDTH / 2);
	}
	
	public int getMaxY() { 
		return y + (Window.HEIGHT / 2);
	}
	
	public void setUniforms(GLProgram prog) {
		prog.setUniform4Matrix("camera", pos);
	}
}
