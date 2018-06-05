
public class BlockRegistry extends Registry {
	private BlockEntity[] entities;
	
	public BlockRegistry(int capacity) {
		super(capacity);
		entities = new BlockEntity[capacity];
	}
	
	public void register(int id, String name, boolean collidable, BlockEntity entity) {
		super.register(id, name, collidable);
		entities[id] = entity;
	}
	
	public void register(int id, String name, boolean collidable) {
		register(id, name, collidable, null);
	}
	
	public boolean isCollidable(Block b) {
		return isCollidable(b.getID());
	}
	
	public BlockEntity getBlockEntity(int id) {
		return entities[id];
	}
	
	public BlockEntity getBlockEntity(Block b) {
		return getBlockEntity(b.getID());
	}
	
	public boolean hasBlockEntity(int id) {
		return getBlockEntity(id) != null;
	}
	
	public boolean hasBlockEntity(Block b) {
		return hasBlockEntity(b.getID());
	}
}