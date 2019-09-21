package com.koowalk.shop.world.chunk;
import com.koowalk.shop.graphics.ChunkRenderEngine;
import com.koowalk.shop.util.SortedPoint2D;
import com.koowalk.shop.world.World;

public class Chunk extends SortedPoint2D {	
	public int x = 0;
	public int y = 0;
	
	public static int SIZE = 64;
	public static int F_SIZE = (int) Math.pow((SIZE), 2) / Byte.SIZE;
	public static int P_SIZE = SIZE * World.BLOCK_SIZE;
	
	public static final int LAYERS = 2;
	public static final int FOREGROUND = 1;
	public static final int BACKGROUND = 0;
	
	Block[][][] blocks;
	BlockEntity[][] blockEntities;
	private int backdrop;
	
	private World world;
	private BlockRegistry blockRegistry;
	
		
	public Chunk(World world, int x, int y) {
		this.x = x;
		this.y = y;
		this.backdrop = 0;
		this.world = world;
		blocks = new Block[LAYERS][SIZE][SIZE];
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
	
	public void place(int layer, int x, int y, Block b) {
		blocks[layer][x][y] = b.clone();
		if (blockEntities[x][y] != null) {
			blockEntities[x][y].remove();
		}
		
		
		if (blockRegistry.hasBlockEntity(b.getID())) {
			blockEntities[x][y] = blockRegistry.getBlockEntity(b.getID()).create(x, y, world);
		} else {
			blockEntities[x][y] = null;
		}
	}
	
	public void place(int x, int y, Block b) {
		place(FOREGROUND, x, y, b);
	}
	
	public void place(int x, int y, int id) {
		place(FOREGROUND, x, y, id);
	}
	
	public void place(int layer, int x, int y, int id) {
		place(layer, x, y, new Block(id));
	}
	
	public void place(BlockInfo b) {
		place(b.getLayer(), b.getX(), b.getY(), b.getBlock());
	}
	
	public Block blockAt(int layer, int x, int y) {
		try {
			return blocks[layer][x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return new Block(1);
		}
	}
	
	public BlockInfo blockInfoAt(int layer, int x, int y) {
		return new BlockInfo(this.x, this.y, x, y, layer, blockAt(layer, x, y));
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

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}
}
