
public class Chunk {
	int x = 0;
	int y = 0;
	
	public static int SIZE = 64;
	byte[][] blocks;
	BlockEntity[][] blockEntities;
	private World world;
	
	public Chunk(World world) {
		this.world = world;
		blocks = new byte[SIZE][SIZE];
		blockEntities = new BlockEntity[SIZE][SIZE];
	}
	
	public void placeBlock(int x, int y, int id) {
		blocks[x][y] = (byte)id;
		if (blockEntities[x][y] != null) {
			blockEntities[x][y].remove();
		}
		
		
		if (Statics.BEN[id] != null) {
			blockEntities[x][y] = Statics.BEN[id].create(x, y, world);
		} else {
			blockEntities[x][y] = null;
		}
	}
	
	public int getBlockAt(int x, int y) {
		try {
			return (int) blocks[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return 1;
		}
	}
	
	public boolean isSolid(int x, int y) {
		return Statics.COL[getBlockAt(x, y)];
	}
	
	public BlockEntity getEntityAt(int x, int y) {
		return blockEntities[x][y];
	}
}
