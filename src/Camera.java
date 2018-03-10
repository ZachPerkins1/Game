
public class Camera {
	private int x;
	private int y;
	
	private int w;
	private int h;
	
	private Entity e = null;
	
	public Camera(int x, int y, int sWidth, int sHeight) {
		this.x = x;
		this.y = y;
		
		h = sHeight;
		w = sWidth;
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
	
	public void setFollowing(Entity en) {
		e = en;
	}
	
	public void cancelFollow() {
		e = null;
	}
	
	public void update() {
		setCenter(e.x, e.y);
	}
	
	public int getAX(int rx) {
		return rx - x;
	}
	
	public int getAY(int ry) {
		return ry - y;
	}
}
