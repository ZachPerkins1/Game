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

public class MapFile {
	public static final String MAP_NAME = "map";
	public static final String ENTITY_NAME = "entities";
	
	private Connection mapDB;
	private Connection entityDB;
	
	private ThreadPoolExecutor executor;
	
	private BlockTracker blockTracker;
	
	public MapFile(File save) {
		blockTracker = new BlockTracker();
		
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
	
	public void loadChunkData(int x, int y) throws SQLException {
		Logger.infov("Loading data for chunk at (" + x + ", " + y + ")");
		PreparedStatement s = mapDB.prepareStatement("SELECT * FROM blocks WHERE chunk_x=? AND chunk_y=?");
		s.setInt(1, x);
		s.setInt(2, y);
		ResultSet r = s.executeQuery();
		
		int n = 0;
		
		while (r.next()) {
			n++;
			blockTracker.changeBlock(new BlockInfo(x, y, r.getInt("block_x"), r.getInt("block_y"), r.getInt("block_layer"), new Block(r.getInt("id"), r.getInt("sub_id"))));
		}
		
		Logger.infov("Loaded " + n + " blocks");
				
		s.close();
	}
	
	private void updateBlock(BlockInfo b) throws SQLException {
		PreparedStatement s = mapDB.prepareStatement("UPDATE blocks SET id=?, sub_id=? WHERE chunk_x=? AND chunk_y=? AND block_x=? AND block_y=? AND block_layer=?");
		s.setInt(1, b.getBlock().getID());
		s.setInt(2, b.getBlock().getSubID());
		s.setInt(3, b.getChunkX());
		s.setInt(4, b.getChunkY());
		s.setInt(5, b.getX());
		s.setInt(6, b.getY());
		s.setInt(7, b.getLayer());
		s.executeUpdate();
	}
	
	private void deleteBlock(BlockInfo b) throws SQLException {
		PreparedStatement s = mapDB.prepareStatement("DELETE FROM blocks WHERE chunk_x=? AND chunk_y=? AND block_x = ? AND block_y = ? AND block_layer = ?");
		s.setInt(1, b.getChunkX());
		s.setInt(2, b.getChunkY());
		s.setInt(3, b.getX());
		s.setInt(4, b.getY());
		s.setInt(5, b.getLayer());
		s.executeUpdate();
	}
	
	private void addBlock(BlockInfo b) throws SQLException {
		PreparedStatement s = mapDB.prepareStatement("INSERT INTO blocks VALUES (?, ?, ?, ?, ?, ?, ?)");
		s.setInt(1, b.getChunkX());
		s.setInt(2, b.getChunkY());
		s.setInt(3, b.getX());
		s.setInt(4, b.getY());
		s.setInt(5, b.getLayer());
		s.setInt(6, b.getBlock().getID());
		s.setInt(7, b.getBlock().getSubID());
		s.executeUpdate();
	}
	
	public void saveChunk(Chunk c) throws SQLException {
		// Compare every block to that currently in the database and make changes as necessary
		for (int x = 0; x < Chunk.SIZE; x++) {
			for (int y = 0; y < Chunk.SIZE; y++) {
				for (int l = 0; l < Chunk.LAYERS; l++) {
					
					BlockInfo currBlock = c.blockInfoAt(l, x, y);
					BlockTracker.ChangeType change = blockTracker.changeBlock(currBlock);
					
					
					if (change.equals(BlockTracker.ChangeType.ADD)) {
						addBlock(currBlock);
					} else if (change.equals(BlockTracker.ChangeType.DELETE)) {
						deleteBlock(currBlock);
					} else if (change.equals(BlockTracker.ChangeType.UPDATE)) {
						updateBlock(currBlock);
					}
				}
			}
		}
	}
	
	public void loadChunk(Chunk c) throws SQLException {
		loadChunkData(c.getX(), c.getY());
		
		for (int x = 0; x < Chunk.SIZE; x++) {
			for (int y = 0; y < Chunk.SIZE; y++) {
				for (int l = 0; l < Chunk.LAYERS; l++) {
					BlockInfo b = blockTracker.blockAt(c.getX(), c.getY(), x, y, l);
					c.place(b);
					if (!b.getBlock().isAir()) {
						Logger.infov("Placing block ID " + b.getBlock().getID());
					}
				}
			}
		}
	}
}
