package com.koowalk.shop.world;

import java.util.HashMap;
import java.util.Map;

import com.koowalk.shop.util.*;
import com.koowalk.shop.world.chunk.Block;
import com.koowalk.shop.world.chunk.BlockInfo;

/**
 * Designed to keep track of all the blocks currently loaded in from the database.
 * This allows changes between in-game blocks and stored blocks to be monitored and
 * the appropriate changes made when the database is written.
 */

public class BlockTracker {
	public enum ChangeType {
		NONE,
		DELETE,
		ADD,
		UPDATE;
	}
	
	/**
	 * A class which contains the information in a single chunk, i.e. which blocks
	 * are where. Only used internally in BlockTracker to optimize performance.
	 */
	private class ChunkInfo {
		private Map<Point3D, Block> blocks; // Map of location to block id
		
		public ChunkInfo() {
			blocks = new HashMap<>();
		}
		
		public Block blockAt(int x, int y, int layer) {
			// If no block is in the hash-map, that is air by default
			return blocks.getOrDefault(new Point3D(x, y, layer), new Block(0));
		}
		
		public void setBlock(BlockInfo b) {
			if (b.getBlock().isAir()) {
				blocks.remove(new Point3D(b.getX(), b.getY(), b.getLayer()));
			} else {
				blocks.put(new Point3D(b.getX(), b.getY(), b.getLayer()), b.getBlock());
			}
		}
		
		public boolean isEmpty() {
			return blocks.isEmpty();
		}
	}
	
	private Map<Point2D, ChunkInfo> chunks; // A list of all chunks that are stored
	private ChunkInfo mostRecentChunk; // The most recently accessed chunk
	private Point2D mostRecentCoordinates; // The most recently accessed coordinates
	
	public BlockTracker() {
		chunks = new HashMap<>();
	}
	
	public void clear() {
		chunks.clear();
	}
	
	public ChunkInfo addChunk(int x, int y) {
		ChunkInfo ci = new ChunkInfo();
		final Point2D p = new Point2D(x, y);
		chunks.put(p, ci);
		return ci;
	}
	
	public ChunkInfo removeChunk(int x, int y) {
		return chunks.remove(new Point2D(x, y));
	}
	
	public ChunkInfo getChunk(int x, int y) {
		return chunks.get(new Point2D(x, y));
	}
	
	public boolean hasChunk(int x, int y) {
		return chunks.containsKey(new Point2D(x, y));
	}
	
	/**
	 * Update the given block or add it to the tracker
	 * @param b The BlockInfo object of the block to be added
	 */
	public void addBlock(BlockInfo b) {
		ChunkInfo chunk = null;
		
		int cx = b.getChunkX();
		int cy = b.getChunkY();
		
		if (mostRecentCoordinates != null && new Point2D(cx, cy).equals(mostRecentCoordinates)) {
			chunk = mostRecentChunk;
		} else if (hasChunk(cx, cy)) {
			chunk = getChunk(cx, cy);
		} else {
			chunk = addChunk(cx, cy);
		}
			
		chunk.setBlock(b);
				
		if (chunk.isEmpty()) {
			removeChunk(cx, cy);
		}
	}
	
	/**
	 * Get the block at a given location in this tracker
	 * @param cx The x value of the chunk
	 * @param cy The y value of the chunk
	 * @param bx The x value of the block
	 * @param by The y value of the block
	 * @param layer The layer of the block
	 * @return The BlockInfo object corresponding to this block
	 */
	public BlockInfo blockAt(int cx, int cy, int bx, int by, int layer) {
		ChunkInfo chunk = null;
		
		if (mostRecentCoordinates != null && new Point2D(cx, cy).equals(mostRecentCoordinates)) {
			chunk = mostRecentChunk;
		} else if (hasChunk(cx, cy)) {
			chunk = getChunk(cx, cy);
		} else {
			return new BlockInfo(cx, cy, bx, by, layer, new Block(0));
		}
				
		return new BlockInfo(cx, cy, bx, by, layer, chunk.blockAt(bx, by, layer));
	}
	
	/**
	 * An override of blockAt that allows for another BlockInfo object to be given instead
	 * of direct coordinates.
	 * @param b The BlockInfo object to get coordinates from
	 * @return The BlockInfo of the given block
	 */
	public BlockInfo blockAt(BlockInfo b) {
		return blockAt(b.getChunkX(), b.getChunkY(), b.getX(), b.getY(), b.getLayer());
	}
	
	public ChangeType changeBlock(BlockInfo b) {
		BlockInfo oldB = blockAt(b);
		
		
		if (oldB.getBlock().equals(b.getBlock())) {
			return ChangeType.NONE;
		} else {
			addBlock(b);
			if (oldB.getBlock().isAir())
				return ChangeType.ADD;
			else if (b.getBlock().isAir())
				return ChangeType.DELETE;
			
			return ChangeType.UPDATE;
		}
	}
	
	public static void main(String[] args) {
		BlockTracker b = new BlockTracker();
		b.addBlock(new BlockInfo(0, 0, 1, 1, 0, new Block(1)));
	}
}

