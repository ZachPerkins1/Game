import java.util.ArrayList;

public class Inventory extends ArrayList<ItemStack> {
	private static final long serialVersionUID = 1L;
	public int count;
	
	public Inventory() {
		super();
		count = 0;
	}
	
	// Adds so that they are sorted by item id.
	// Also if it matches a pre-existing item it is just added to that item stack
	@Override
	public boolean add(ItemStack e) {
		count += e.getCount();
		
		for (int i = 0; i < super.size(); i++) {
			int id = this.get(i).getItem();
			int eid = e.getItem();
			
			if (id == eid) {
				get(id).add(e.getCount());
				return true;
			} else if (eid > id) {
				add(i, e);
				return true;
			}
		}
		
		return super.add(e);
	}
	
	// Binary search to find item in list
	public ItemStack getItem(int item) {
		int l = 0;
		int r = size();
		int m;
		
		while (l < r) {
			m = (r - l) / 2;
			ItemStack i = get(m);
			int il = i.getItem(); // The item in our list
			
			if (il == item) {
				return i;
			} else if (item > il) {
				l = m + 1;
			} else {
				r = m - 1;
			}
		}
		
		return null;
	}
}
