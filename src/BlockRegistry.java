
public class BlockRegistry {
	private BlockEntity[] entities;
	private boolean[] collisionData;
	private String[] names;
	
	public BlockRegistry(int capacity) {
		entities = new BlockEntity[capacity];
		collisionData = new boolean[capacity];
		names = new String[capacity];
	}
	
	public void register(int id, String name, boolean collidable, BlockEntity entity) {
		names[id] = name;
		collisionData[id] = collidable;
		entities[id] = entity;
	}
	
	public void register(int id, String name, boolean collidable) {
		register(id, name, collidable, null);
	}
	
	public String getName(int id) {
		return names[id];
	}
	
	public String getName(Block b) {
		return names[b.getID()] + ":" + b.getSubID();
	}
	
	public boolean isCollidable(int id) {
		return collisionData[id];
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
