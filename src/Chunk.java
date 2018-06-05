import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Chunk {	
	int x = 0;
	int y = 0;
	
	public static int SIZE = 64;
	public static int F_SIZE = (int) Math.pow((SIZE), 2) / Byte.SIZE;
	public static int P_SIZE = SIZE * World.BLOCK_SIZE;
	
	public static final int LAYERS = 2;
	public static final int FOREGROUND = 1;
	public static final int BACKGROUND = 0;
	
	Block[][][] blocks;
	BlockEntity[][] blockEntities;
	private int backdrop = 0;
	
	private World world;
	private BlockRegistry blockRegistry;
		
	public Chunk(World world, int x, int y) {
		this.x = x;
		this.y = y;
		this.world = world;
		blocks = new Block[2][SIZE][SIZE];
		fill();
		
		
		blockEntities = new BlockEntity[SIZE][SIZE];
		blockRegistry = world.getBlockRegistry();
	}
	
	private void fill() {
		for (int layer = 0; layer < LAYERS; layer++) {
			for (int cx = 0; cx < SIZE; cx++) {
				for (int cy = 0; cy < SIZE; cy++) {
					blocks[layer][cx][cy] = new Block(0);
				}
			}
		}
	}
	
	public void place(int layer, int x, int y, int id) {
		blocks[layer][x][y] = new Block(id);
		if (blockEntities[x][y] != null) {
			blockEntities[x][y].remove();
		}
		
		
		if (blockRegistry.hasBlockEntity(id)) {
			blockEntities[x][y] = blockRegistry.getBlockEntity(id).create(x, y, world);
		} else {
			blockEntities[x][y] = null;
		}
	}
	
	public void place(int x, int y, int id) {
		place(FOREGROUND, x, y, id);
	}
	
	public Block blockAt(int layer, int x, int y) {
		try {
			return blocks[layer][x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return new Block(1);
		}
	}
	
	public Block blockAt(int x, int y) {
		return blockAt(FOREGROUND, x, y);
	}
	
	public boolean isSolid(int x, int y) {
		return blockRegistry.isCollidable(blockAt(x, y));
		// return Statics.COL[blockAt(x, y)];
	}
	
	public BlockEntity getEntityAt(int x, int y) {
		return blockRegistry.getBlockEntity(blockAt(x, y));
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
					blocks[FOREGROUND][x][y] = new Block(0);
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
				
				int b = blockAt(x, y).getID();
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
	
	public void draw() {
		ChunkRenderEngine.getInstance().drawChunk(x, y, backdrop, blocks);
	}
	
	public String toString() {
		return "Chunk: x=" + x + " y=" + y;
	}
}
