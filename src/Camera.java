
public class Camera {
	private double scale;
	private int x;
	private int y;
	
	private int w;
	private int h;
	private int fw;
	private int fh;
	
	private Entity e = null;
	
	public Camera(int x, int y, int sWidth, int sHeight) {
		this.x = x;
		this.y = y;
		scale = 1.0;
		
		h = sHeight;
		w = sWidth;
		fw = 200;
		fh = 200;
	}
	
	public Camera(int sWidth, int sHeight) {
		this(0, 0, sWidth, sHeight);
	}
	
	public Camera() {
		this(Window.WIDTH, Window.HEIGHT);
	}
	
	public void setCenter(int cx, int cy) {
		x = cx - (w / 2);
		y = cy - (h / 2);
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
		int cx = x + w/2;
		int cy = y + h/2;
		int fw2 = fw / 2;
		int fh2 = fh / 2;
		
		int maxX = cx + fw2;
		int minX = cx - fw2;
		int maxY = cy + fh2;
		int minY = cy - fh2;
		
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
	
	public int getAX(double rx) {
		return (int) (rx - x);
	}
	
	public int getAY(double ry) {
		return (int) (ry - y);
	}
	
	// given an adjusted x, get a normalized x
	public int getX(int ax) {
		return (ax + x);
	}
	
	public int getY(int ay) {
		return (ay + y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getMaxX() {
		return x + w;
	}
	
	public int getMaxY() { 
		return y + h;
	}
}
