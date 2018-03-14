import java.util.ArrayList;

public class ChunkList extends ArrayList<Chunk> {
	public Chunk at(int x, int y) {
		// Slightly modified binary search
		int l = 0;
		int r = size() - 1; //2
		int m;
		
		while (l <= r) {
			m = l + ((r - l) / 2); // 1
			// System.out.println(r);
			
			int lx = get(m).x; // 1
			if (lx == x) {
				int ly = get(m).y;
				if (ly == y) {
					return get(m);
				}
				else if (ly < y)
					l = m + 1;
				else
					r = m - 1;
			} else if (lx < x)
				l = m + 1;
			else
				r = m - 1;
		}
		
		return null;
	}
	
	// TODO: Make this more efficient at finding the right place to insert
	public boolean add(Chunk c) {
		for (int j = 0; j < size(); j++) {
			int lx = get(j).x;
			
			if (get(j).x == c.x) {
				int ly = get(j).y;
				
				if (c.y <= ly) {
					super.add(j, c);
					return true;
				} else if (j != size() - 1 && get(j + 1).x != c.x) {
					super.add(j + 1, c);
					return true;
				}
			} else if (c.x < lx) {
				super.add(j, c);
				return true;
			}
		}
		
		super.add(c);
		return true;
	}
}
