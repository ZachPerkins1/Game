import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class World {
	public int blockSize;
	private int width;
	private int height;
	
	private byte[][] map;
	private BlockEntity[][] eMap;
	
	private ArrayList<Entity> entities;
	
	public World(int width, int height) {
		this.width = width;
		this.height = height;
		
		createMap();
	}
	
	// Make the arrays calculate block size based on screen size
	private void createMap() {
		blockSize = Math.min(Window.WIDTH, Window.HEIGHT) / Math.max(width, height);
		map = new byte[width][height];
		eMap = new BlockEntity[width][height];
		
		entities = new ArrayList<Entity>();
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	public void processCollisions() {
		
	}

	public void placeBlock(int x, int y, int id) {
		map[x][y] = (byte)id;
		if (eMap[x][y] != null) {
			eMap[x][y].remove();
		}
		
		
		if (Statics.BEN[id] != null) {
			eMap[x][y] = Statics.BEN[id].create(x, y, blockSize);
		} else {
			eMap[x][y] = null;
		}
	}
	
	public int getBlockAt(int x, int y) {
		try {
			return (int) map[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return 1;
		}
	}
	
	public boolean isSolid(int x, int y) {
		return Statics.COL[getBlockAt(x, y)];
	}
	
	public BlockEntity getEntityAt(int x, int y) {
		return eMap[x][y];
	}
	
	public int getBlockAt(Point p) {
		return getBlockAt(p.x, p.y);
	}
	
	public Point pixel2coord(int px, int py) {
		return new Point(px/blockSize, py/blockSize);
	}
	
	// Load from a file where the top 8 bytes specify map size and the remaining individual bytes
	// specify the blocks themselves. Currently sets a hard range on block ids from 0-255
	public void open(String filename) {
		try {
			byte[] map = Files.readAllBytes(Paths.get(filename));
			
			width = arrToInt(map, 0);
			height = arrToInt(map, 4);
			
			createMap();
			
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					this.placeBlock(x, y, map[x*width + y + 8]);
				}
			}
			
		} catch (IOException e) {
			System.out.println("Unable to open map " + filename);
		}
	}
	
	// Saves using the same process
	public void save(String filename) {
		byte[] raw = new byte[width*height + 8];
		intToArr(width, raw, 0);
		intToArr(height, raw, 4);
				
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				raw[x*width + y + 8] = map[x][y];
			}
		}
		
		try {
			Files.write(Paths.get(filename), raw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Take bytes out of an arr from start and convert them to int
	private int arrToInt(byte[] arr, int start) {
		int x = arr[start + 0] << 24 |
				arr[start + 1] << 16 |
				arr[start + 2] << 8  |
				arr[start + 3];
		
		return x;
	}
	
	// Take individual bytes out of an int and shove them into an arr at
	// start
	private void intToArr(int n, byte[] arr, int start) {
		arr[start + 3] = (byte) n;
		arr[start + 2] = (byte) (n >> 8);
		arr[start + 1] = (byte) (n >> 16);
		arr[start] = (byte) (n >> 24);
	}
	
	// Called by window
	public void draw(Graphics2D g) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int id = map[x][y];
				BlockEntity e = eMap[x][y];
				Color c = Statics.TEX[id];
				if (c != null) {
					g.setColor(c);
					g.fillRect(x*blockSize, y*blockSize, blockSize, blockSize);
				}
				if (e != null) {
					e.draw(g);
				}
			}
		}
	}
	
	public void update() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (eMap[x][y] != null)
					eMap[x][y].update();
			}
		}
	}
}
