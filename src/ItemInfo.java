
public abstract class ItemInfo {
	protected final int _ID;
	
	public ItemInfo(int id) {
		_ID = id;
	}
	
	public abstract int getSize();
}
