import java.awt.Color;

public class Statics {
	// A list of textures for blocks (just colors rn) Blocks occupy the first 255 slots and then
	// items occupy the rest
	public static Color[] TEX = new Color[512];
	// A list specifying whether each block is solid or "collidable"
	public static boolean[] COL = new boolean[255];
	// A list stating the associated block entity for each block
	public static BlockEntity[] BEN = new BlockEntity[255];
	
	//The amount of block textures there are
	public static int BLK_TEX_CNT = 255;
	
	// Block static initializer
	static {
		// Air
		TEX[0] = null; 
		COL[0] = false;
		BEN[0] = null;
		
		// Wall
		TEX[1] = Color.BLACK;
		COL[1] = true;
		BEN[1] = null;
		
		// Shelf
		TEX[2] = new Color(222, 184, 135); 
		COL[2] = true;
		BEN[2] = new Shelf(2);
	}
	
	// List of item entities
	public static ItemInfo[] IEN = new ItemInfo[255];
	
	public static int ITM(int id) {
		return BLK_TEX_CNT + id;
	}
	
	public static Color ITM_TEX(ItemInfo i) {
		return TEX[ITM(i._ID)];
	}
	
	// Item static initializer
	static {
		// Apple
		TEX[ITM(0)] = Color.RED;
		    IEN[0]  = new Apple(0);
	}
}
