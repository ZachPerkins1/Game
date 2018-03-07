import java.awt.Point;
import java.util.ArrayList;

public class APointList extends ArrayList<APoint> {
	public APoint getLowest() {
		return this.get(0);
	}
	
	public boolean add(APoint p) {
		for (int i = 0; i < this.size(); i++) {
			if (super.get(i).getScore() >= p.getScore()) {
				super.add(i, p);
				return true;
			}
		}
		
		return super.add(p);
	}
	
	public boolean n_add(APoint p) {
		return super.add(p);
	}
	
	public boolean preExisting(Point p, int iscore) {
		for (APoint point : this) {
			if (p.equals(point)) {
				if (point.getG() > iscore + 1)
					point.updateG(iscore);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean contains(APoint p) {
		for (APoint point : this) { 
			if (point.equals(p))
				return true;
		}
		
		return false;
	}
	
	public void addSurrounding(World world, APoint parent, APointList closedList, Point end) {
		Point[] surrounding = getSurrounding(parent);
		for (int i = 0; i < 4; i++) {
			if (!world.isSolid(surrounding[i].x, surrounding[i].y)) {
				if (!closedList.contains(surrounding[i]) && !preExisting(surrounding[i], parent.getG()))
					add(new APoint(end, parent, surrounding[i].x, surrounding[i].y));
			}
		}
	}
	
	private Point[] getSurrounding(Point pos) {
		Point[] p = new Point[4]; //new Point[8];
		p[0] = new Point(pos.x, pos.y + 1);
		p[1] = new Point(pos.x - 1, pos.y);
		p[2] = new Point(pos.x, pos.y - 1);
		p[3] = new Point(pos.x + 1, pos.y);
		
		// Have to make sure a valid gap exists in the diagonal for this to work
		//p[4] = new Point(pos.x + 1, pos.y + 1);
		//p[5] = new Point(pos.x - 1, pos.y - 1);
		//p[6] = new Point(pos.x + 1, pos.y - 1);
		//p[7] = new Point(pos.x - 1, pos.y + 1);
		return p;
	}
}
