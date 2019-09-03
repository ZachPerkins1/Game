package com.koowalk.shop.world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.koowalk.shop.util.Logger;
import com.koowalk.shop.util.Point2D;
import com.koowalk.shop.util.SortedArrayList;
import com.koowalk.shop.util.SortedPoint2D;
import com.koowalk.shop.world.chunk.Block;
import com.koowalk.shop.world.chunk.BlockInfo;
import com.koowalk.shop.world.chunk.Chunk;
import com.koowalk.shop.world.chunk.ChunkInfo;

public class MapFile {
	public static final String MAP_NAME = "map";
	public static final String ENTITY_NAME = "entities";
	
	private Connection mapDB;
	private Connection entityDB;
	
	private ThreadPoolExecutor executor;
	
	private Set<ChunkInfo> chunkInfo;
	
	public MapFile(File save) {
		chunkInfo = new HashSet<ChunkInfo>();
		
		try {
			mapDB = DriverManager.getConnection("jdbc:sqlite:" + save.toString() + "/" + MAP_NAME);
			entityDB = DriverManager.getConnection("jdbc:sqlite:" + save.toString() + "/" + ENTITY_NAME);
		} catch (SQLException e) {
			Logger.error("Failed to open map db");
			Logger.exception(e);
		}
		
		setDefaults(mapDB);
		setDefaults(entityDB);
		
		try {
			createTables(mapDB, "map.sql");
			createTables(entityDB, "entity.sql");
		} catch (SQLException | IOException e) {
			Logger.error("Failed to open map db");
			Logger.exception(e);
		}
		
		// executor = (ThreadPoolExecutor) Executors.newSingleThreadExecutor();
	}
	
	private void setDefaults(Connection conn) {
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createTables(Connection conn, String string) throws SQLException, IOException {
		StringBuilder query = new StringBuilder();
		
		Statement statement = conn.createStatement();
		BufferedReader reader = new BufferedReader(new FileReader(string));
		
		String s;
		while ((s = reader.readLine()) != null) {
			query.append(s.trim());
		}
		
		reader.close();
		statement.executeUpdate(query.toString());
		statement.close();
	}
	
	public ChunkInfo getChunkData(int x, int y) throws SQLException {
		PreparedStatement s = mapDB.prepareStatement("SELECT * FROM blocks WHERE chunk_x=? AND chunk_y=?");
		s.setInt(1, x);
		s.setInt(2, y);
		ResultSet r = s.executeQuery();
		
		ChunkInfo info = new ChunkInfo(x, y);
		
		while (r.next()) {
			info.add(new BlockInfo(new Block(r.getInt("id"), r.getInt("sub_id")), r.getInt("block_x"), r.getInt("block_y"), r.getInt("block_layer")));
		}
				
		s.close();
		return info;
	}
	
	private void updateBlock(int cx, int cy, BlockInfo b) throws SQLException {
		PreparedStatement s = mapDB.prepareStatement("UPDATE blocks SET id=?, sub_id=? WHERE chunk_x=? AND chunk_y=? AND block_x=? AND block_y=? AND block_layer=?");
		s.setInt(1, b.getBlock().getID());
		s.setInt(2, b.getBlock().getSubID());
		s.setInt(3, cx);
		s.setInt(4, cy);
		s.setInt(5, b.getX());
		s.setInt(6, b.getY());
		s.setInt(7, b.getLayer());
		s.executeUpdate();
	}
	
	private void deleteBlock(int cx, int cy, BlockInfo b) throws SQLException {
		PreparedStatement s = mapDB.prepareStatement("DELETE FROM blocks WHERE chunk_x=? AND chunk_y=? AND block_x = ? AND block_y = ? AND block_layer = ?");
		s.setInt(1, cx);
		s.setInt(2, cy);
		s.setInt(3, b.getX());
		s.setInt(4, b.getY());
		s.setInt(5, b.getLayer());
		s.executeUpdate();
	}
	
	private void addBlock(int cx, int cy, BlockInfo b) throws SQLException {
		PreparedStatement s = mapDB.prepareStatement("INSERT INTO blocks VALUES (?, ?, ?, ?, ?, ?, ?)");
		s.setInt(1, cx);
		s.setInt(2, cy);
		s.setInt(3, b.getX());
		s.setInt(4, b.getY());
		s.setInt(5, b.getLayer());
		s.setInt(6, b.getBlock().getID());
		s.setInt(7, b.getBlock().getSubID());
		s.executeUpdate();
	}
	
	public void saveChunk(Chunk c) throws SQLException {
		ChunkInfo ci = (ChunkInfo) chunkInfo.(new Point2D(c.x, c.y)); 
		if (ci == null) {
			ci = new ChunkInfo(c.x, c.y);
		}
		
		ChunkInfo newCi = new ChunkInfo(c.x, c.y);
				
		// Compare every block to that currently in the database and make changes as necessary
		for (int x = 0; x < Chunk.SIZE; x++) {
			for (int y = 0; y < Chunk.SIZE; y++) {
				for (int l = 0; l < Chunk.LAYERS; l++) {
					BlockInfo oldBlock = ci.getBlock(x, y, l);		
					BlockInfo newBlock = new BlockInfo(c.blockAt(l, x, y), x, y, l);
										
					if (!newBlock.getBlock().isAir()) {
						newCi.add(newBlock);
					}

					handleBlock(c.x, c.y, oldBlock, newBlock);
				}
			}
		}
		
		chunkInfo.remove(ci);
	}
	
	private void handleBlock(int cx, int cy, BlockInfo oldBlock, BlockInfo newBlock) throws SQLException {		
		// If we need to do anything to the block	
		
		if ((oldBlock != null && !newBlock.equals(oldBlock)) 
				|| (!newBlock.getBlock().isAir() && oldBlock == null)) {
			System.out.println(oldBlock + " | " + newBlock);
			if (newBlock.getBlock().isAir()) 
				deleteBlock(cx, cy, oldBlock);
			else if (oldBlock == null || oldBlock.getBlock().isAir())
				addBlock(cx, cy, newBlock);
			else
				updateBlock(cx, cy, newBlock);
		}
	}
	
	public void loadChunk(Chunk c) throws SQLException {
		ChunkInfo ci = getChunkData(c.x, c.y);
		chunkInfo.addSorted(ci);
				
		for (int i = 0; i < ci.getBlockCount(); i++) {
			BlockInfo bi = ci.get(i);
			c.place(bi.getLayer(), bi.getX(), bi.getY(), bi.getBlock());
		}
	}
}
