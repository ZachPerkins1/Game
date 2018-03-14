import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Chunk {
	int x = 0;
	int y = 0;
	
	public static int SIZE = 64;
	public static int F_SIZE = (int) Math.pow((SIZE), 2) / Byte.SIZE;
	public static int P_SIZE = SIZE * World.BLOCK_SIZE;
	
	byte[][] blocks;
	BlockEntity[][] blockEntities;
	private World world;
	
	public Chunk(World world, int x, int y) {
		this.x = x;
		this.y = y;
		this.world = world;
		blocks = new byte[SIZE][SIZE];
		blockEntities = new BlockEntity[SIZE][SIZE];
	}
	
	public void place(int x, int y, int id) {
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
	
	public int blockAt(int x, int y) {
		try {
			return (int) blocks[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return 1;
		}
	}
	
	public boolean isSolid(int x, int y) {
		return Statics.COL[blockAt(x, y)];
	}
	
	public BlockEntity getEntityAt(int x, int y) {
		return blockEntities[x][y];
	}
	
	// Load a file from start returning the new start
	public int load(byte[] file, int start) {
		int end = start + F_SIZE;
		
		for (int i = start; i < start + F_SIZE; i++) {
			int o = (i - start) * Byte.SIZE;
			
			for (int j = 0; j < 8; j++) {
				int x = (o + j) / SIZE;
				int y = (o + j) % SIZE;
				
				if (((byte)(file[i] >> (7 - j)) & (byte) 0x01) == (byte)0x01) {
					place(x, y, file[end++]);
				} else {
					blocks[x][y] = 0;
				}
			}
		}
		
		return end;
	}
	
	// Save the chunk to a file
	public void save(BufferedOutputStream file) throws IOException {
		ArrayList<Byte> blockList = new ArrayList<Byte>();
		byte[] buffer = new byte[F_SIZE];
		
		for (int i = 0; i < F_SIZE; i++) {
			int o = i * Byte.SIZE;
			buffer[i] = 0;
			
			for (int j = 0; j < 8; j++) {
				int x = (o + j) / SIZE;
				int y = (o + j) % SIZE;
				
				int b = blockAt(x, y);
				byte comparator = (byte)0x00;
				
				if (b != 0) {
					blockList.add((byte)b);
					comparator = (byte)0x01;
				}
				
				buffer[i] = (byte) ((buffer[i] | comparator) << (j == 7? 0: 1));
			}
		}
		
		file.write(buffer);
		for (byte b : blockList) {
			file.write(b);
		}
	}
	
	public void update() {
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				if (blockEntities[x][y] != null)
					blockEntities[x][y].update();
			}
		}
	}
	
	public void draw(Graphics2D g, Camera cam) {
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				int id = blocks[x][y];
				BlockEntity e = blockEntities[x][y];
				Color c = Statics.TEX[id];
				if (c != null) {
					g.setColor(c);
					int ox = P_SIZE*this.x;
					int oy = P_SIZE*this.y;
					
					g.fillRect(cam.getAX(ox + x*World.BLOCK_SIZE), cam.getAY(oy + y*World.BLOCK_SIZE), World.BLOCK_SIZE, World.BLOCK_SIZE);
				}
				if (e != null) {
					e.draw(g, cam);
				}
			}
		}
	}
	
	public String toString() {
		return "Chunk: x=" + x + " y=" + y;
	}
}
