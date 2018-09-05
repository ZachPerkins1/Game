package com.koowalk.shop.world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

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
	
	private SortedArrayList<SortedPoint2D> chunkInfo;
	
	public MapFile(File save) {
		chunkInfo = new SortedArrayList<SortedPoint2D>();
		
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
	
	// Ugh
	public void saveChunk(Chunk c) throws SQLException {
		ChunkInfo ci = (ChunkInfo) chunkInfo.find(new Point2D(c.x, c.y)); 
		if (ci == null) {
			ci = new ChunkInfo(c.x, c.y);
		}
				
		for (int x = 0; x < Chunk.SIZE; x++) {
			for (int y = 0; y < Chunk.SIZE; y++) {
				for (int l = 0; l < Chunk.LAYERS; l++) {
					Block b = c.blockAt(l, x, y);
					
					// If we need to do anything to the block
					if (!b.isAir()) {
						BlockInfo noo = new BlockInfo(b, x, y, l); // The block info for the new block data affectionately named noo
						BlockInfo bi = ci.getBlock(x, y, l);
						
						if (bi != null) {
							if (bi.getBlock().equals(b)) {
								// We need to update a pre-existing block
								updateBlock(c.x, c.y, noo);
							}
							
							ci.remove(bi);
						} else {
							// We need to add a new block to the database
							addBlock(c.x, c.y, noo);
						}
					}
				}
			}
		}
		
		// We want to delete all the left-over blocks in the list
		for (int i = 0; i < ci.getBlockCount(); i++) {
			deleteBlock(ci.getX(), ci.getY(), ci.get(i));
		}
		
		chunkInfo.remove(ci);
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
