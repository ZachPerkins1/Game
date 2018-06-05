
public class Registry {
	private boolean[] collisionData;
	private String[] names;
	
	public Registry(int capacity) {
		collisionData = new boolean[capacity];
		names = new String[capacity];
	}
	
	public void register(int id, String name, boolean collidable) {
		names[id] = name;
		collisionData[id] = collidable;
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
}
