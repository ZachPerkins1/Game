import java.awt.Graphics2D;

public class Shelf extends BlockEntity {
	private Inventory i;
	
	public Shelf(int id) {
		super(id);
		i = new Inventory();
		i.add(new ItemStack(0));
	}

	@Override
	public boolean canUse() {
		return true;
	}
	
	@Override
	public boolean canAccess() {
		return true;
	}
	
	@Override
	public void use() {
		// TODO Auto-generated method stub
	}

	@Override
	public Inventory access() {
		return i;
	}
	
	@Override
	public void remove() {
		System.out.println("Removed Shelf!");
		
	}

	@Override
	public BlockEntity getNew() {
		return new Shelf(_ID);
	}
	
	@Override
	public void draw(Graphics2D g) {
		for (ItemStack item : i) {
			g.setColor(item.getTexture());
			g.fillOval(getPX(), getPY(), 10, 10);
		}
	}

	@Override
	public int getLuminescence() {
		return 0;
	}
	
	@Override
	public void update() {
		
	}
}
