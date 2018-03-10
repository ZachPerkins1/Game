import java.awt.Graphics2D;

public abstract class BlockEntity {
	protected final int _ID;
	private int x;
	private int y;
	private World world;
	
	public BlockEntity(int id) {
		_ID = id;
	}
	
	public abstract int getLuminescence();
	public abstract void use();
	public abstract Inventory access();
	public abstract void remove();
	
	public void update() {}
	public void draw(Graphics2D g, Camera c) {}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getPixels() {
		return world.blockSize;
	}
	
	public int getPX() {
		return world.blockSize*x;
	}
	
	public int getPY() {
		return world.blockSize*y;
	}
	
	public BlockEntity getNew() {
		return null;
	}
	
	public BlockEntity create(int x, int y, World w) {
		BlockEntity e = getNew();
		e.x = x;
		e.y = y;
		e.world = w;
		return e;
	}
	
	public boolean canUse() {
		return false;
	}
	
	public boolean canAccess() {
		return false;
	}
}
