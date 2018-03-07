import java.awt.Graphics2D;

public abstract class BlockEntity {
	protected final int _ID;
	private int x;
	private int y;
	private int p;
	
	public BlockEntity(int id) {
		_ID = id;
	}
	
	public abstract int getLuminescence();
	public abstract void use();
	public abstract Inventory access();
	public abstract void remove();
	
	public void update() {}
	public void draw(Graphics2D g) {}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getPixels() {
		return p;
	}
	
	public int getPX() {
		return p*x;
	}
	
	public int getPY() {
		return p*y;
	}
	
	public BlockEntity getNew() {
		return null;
	}
	
	public BlockEntity create(int x, int y, int p) {
		BlockEntity e = getNew();
		e.x = x;
		e.y = y;
		e.p = p;
		return e;
	}
	
	public boolean canUse() {
		return false;
	}
	
	public boolean canAccess() {
		return false;
	}
}
