import java.awt.Point;

public class APoint extends Point {
	private int g;
	private int h;
	private int score;
	private APoint parent;
	
	public APoint(Point rel, APoint parent, int x, int y) {
		super(x, y);
		updateG(parent.getG());
		calculateH(rel);
		this.parent = parent;
	}
	
	public APoint(Point rel, int x, int y) {
		super(x, y);
		g = 1;
		calculateH(rel);
	}
	
	public APoint getParent() {
		return parent;
	}
	
	public void updateG(int iscore) {
		g = iscore + 1;
		score = h + g;
	}
	
	public int getG() {
		return g;
	}
	
	public int getScore() {
		return score;
	}
	
	private void calculateH(Point p) {
		h = Math.abs(p.y - y) + Math.abs(p.x - x);
		score = h + g;
	}
	
	public String toString() {
		return "APoint[x=" + x + ",y=" + y + ",score=" + score + "]";
	}
	
	public boolean equals(Point p2) {
		return x == p2.x && y == p2.y;
	}
	
	public Point getAdjusted(int pixels) {
		return new Point(x*pixels + pixels/2, y*pixels + pixels/2);
	}
}
