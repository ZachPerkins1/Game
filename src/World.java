import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class World {
	public static double EPSILON = 0.5;
	
	public static int BLOCK_SIZE = 32;
	
	private Camera cam;
	private ChunkList chunks;
	private ArrayList<Entity> entities;
	
	public World() {
		cam = new Camera();
		chunks = new ChunkList();
		entities = new ArrayList<Entity>();
	}
	
	private void create() {
		// int minChunkX = cam.getX() / Chunk.P_SIZE;
		// int maxChunkX = cam.getMaxX() / Chunk.P_SIZE;
		// int minChunkY = cam.getY() / Chunk.P_SIZE;
		// int maxChunkY = cam.getMaxY() / Chunk.P_SIZE;	
	}
	
	private Chunk getChunk(int x, int y) {
		Chunk c = chunks.at(x, y);
		
		if (c != null)
			return c; // Oh cool we don't need to do any work
		
		c = new Chunk(this, x, y);
		chunks.add(c);
		return c;
	}
	
	// Places block at absolute positions of x and y
	public void place(int x, int y, int id) {
		Point c = coord2chunk(x, y);
		
		int ox = Math.abs((Chunk.SIZE) + (x % Chunk.SIZE)) % Chunk.SIZE;
		int oy = Math.abs((Chunk.SIZE) + (y % Chunk.SIZE)) % Chunk.SIZE;
				
		Chunk chunk = getChunk(c.x, c.y);
		chunk.place(ox, oy, id);
		
		System.out.println(c);
		System.out.println(x);
	}
	
	public int blockAt(int x, int y) {
		Point coords = coord2chunk(x, y);
		Chunk chunk = getChunk(coords.x, coords.y);
		
		int ox = Math.abs((Chunk.SIZE) + (x % Chunk.SIZE)) % Chunk.SIZE;
		int oy = Math.abs((Chunk.SIZE) + (y % Chunk.SIZE)) % Chunk.SIZE;
		
		return chunk.blockAt(ox, oy);
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	// Process collision
	public void processCollisions() {
		for (Entity e : entities) {
			double sx = 0;
			double sy = 0;
			double x = e.x;
			double y = e.y;
			
			double ty = y + e.dy;
			double tx = x + e.dx;
						
			if (Math.abs(e.dx) > Math.abs(e.dy)) {
				sx = relativeMin(e.dx, BLOCK_SIZE);
				sy = (e.dy/e.dx) * sx;
			} else if (e.dy != 0) {
				sy = relativeMin(e.dy, BLOCK_SIZE);
				sx = (e.dx/e.dy) * sy;
			}
			
			
			while (!((equals(x, tx) || sx == 0) && (equals(y, ty) || sy == 0))) {		
				x += relativeMin(sx, Math.abs(x - (e.x + e.dx)));
				y += relativeMin(sy, Math.abs(y - (e.y + e.dy)));
				
				if (isColliding(x, y, e.getW(), e.getH())) {
					x -= sx;
					y -= sy;
					
					double divisor = Math.ceil(Math.max(Math.abs(sx), Math.abs(sy)));
					double fx = sx / divisor;
					double fy = sy / divisor;
										
					boolean success = false;
					while (!success) {
						x += fx;
						if (isColliding(x, y, e.getW(), e.getH())) {
							sx = 0;
							x -= fx;
							success = true;
						}
						else {
							y += fy;
							if (isColliding(x, y, e.getW(), e.getH())) {
								sy = 0;
								y -= fy;
								success = true;
							}
						}
					}
				}
			}
						
			e.x = (int) Math.round(x);
			e.y = (int) Math.round(y);
		}
	}
	
	// TODO: create another version of this that takes a collision event
	// TODO: MAKE BETTER
	private boolean isColliding(double x, double y, int width, int height) {
		double ax = (x - (width / 2));
		double ay = (y - (height / 2));
		
		Point coords = pixel2coord((int)ax, (int)ay);
		int bx = coords.x;
		int by = coords.y;
		
		int bw = (int) Math.ceil((width + Math.abs(ax % BLOCK_SIZE)) / BLOCK_SIZE);
		int bh = (int) Math.ceil((height + Math.abs(ay % BLOCK_SIZE)) / BLOCK_SIZE);
		
		for (int i = bx; i <= bx + bw; i++) {
			for (int j = by; j <= by + bh; j++) {
				if (isSolid(i, j)) {
					if (new Rectangle(i*BLOCK_SIZE, j*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE).intersects(new Rectangle((int)Math.round(ax), (int)Math.round(ay), width, height))) {
						return true;
					}
				}	
			}
		}
				
		return false;
	}
	
	// Utility method for collisions
	private double relativeMin(double a, double b) {
		if (a < 0) {
			b = -b;
			return Math.max(a, b);
		}
		
		return Math.min(a, b);
	}
	
	private boolean equals(double a, double b) {
		return a > b - EPSILON && a < b + EPSILON;
	}
	
	public boolean isSolid(int x, int y) {
		return Statics.COL[blockAt(x, y)];
		//return Statics.COL[getBlockAt(x, y)];
	}
	
	public BlockEntity getEntityAt(int x, int y) {
		// TODO: Just fill this in
		return null;
	}
	
	public Point pixel2coord(int px, int py) {
		int bx = px / BLOCK_SIZE;
		int by = py / BLOCK_SIZE;
		
		if (px < 0) bx--;
		if (py < 0) by--;
		
		return new Point(bx, by);
	}
	
	public Point coord2pixel(int bx, int by) {
		if (bx < 0) bx--;
		if (by < 0) by--;
		
		return new Point(bx*BLOCK_SIZE, by*BLOCK_SIZE);
	}
	
	public Point coord2chunk(int bx, int by) {
		int cx = bx / Chunk.SIZE;
		int cy = by / Chunk.SIZE;
		
		if (bx < 0) cx--;
		if (by < 0) cy--;
		
		return new Point(cx, cy);
	}
	
	// Load from a file where the top 8 bytes specify map size and the remaining individual bytes
	// specify the blocks themselves. Currently sets a hard range on block ids from 0-255
	public void open(String filename) throws IOException {
		// BufferedInputStream stream = new BufferedInputStream(new FileInputStream(filename));
		byte[] map = Files.readAllBytes(Paths.get(filename));
		
		
		if (map.length < 4 ||
			map[0] != (byte) 0xba ||
			map[1] != (byte) 0xd0 ||
			map[2] != (byte) 0xda ||
			map[3] != (byte) 0xd0) {
			System.out.println(map[0]);
			throw new IOException("File format not recognized");
		}
		
		int i = 4;
		while (i < map.length) {
			int x = arrToInt(map, i);
			int y = arrToInt(map, i + Integer.BYTES);
			Chunk c = new Chunk(this, x, y);
			i = c.load(map, i + Integer.BYTES*2);
			chunks.add(c);
		}
		
//		try {
//			
//			
//			System.out.
//			createMap();
//			
//			for (int x = 0; x < width; x++) {
//				for (int y = 0; y < height; y++) {
//					this.placeBlock(x, y, map[x*width + y + 8]);
//				}
//			}
//			
//			
//			
//		} catch (IOException e) {
//			System.out.println("Unable to open map " + filename);
//		}
		
	}
	
	// Saves using the same process
	public void save(String filename) throws IOException {
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(filename));
		
		byte[] header = new byte[] {(byte) 0xba, (byte) 0xd0, (byte) 0xda, (byte) 0xd0};
		stream.write(header);
		for (Chunk c : chunks) {
			byte[] coords = new byte[Integer.BYTES * 2];
			intToArr(c.x, coords, 0);
			intToArr(c.y, coords, Integer.BYTES);
			stream.write(coords);
			c.save(stream);
		}
		
		stream.close();
	
//		byte[] raw = new byte[width*height + 8];
//		intToArr(width, raw, 0);
//		intToArr(height, raw, 4);
//				
//		for (int x = 0; x < width; x++) {
//			for (int y = 0; y < height; y++) {
//				raw[x*width + y + 8] = map[x][y];
//			}
//		}
//		
//		try {
//			Files.write(Paths.get(filename), raw);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	// Take bytes out of an arr from start and convert them to int
	private int arrToInt(byte[] arr, int start) {
		int x = arr[start + 0] << 24 |
				arr[start + 1] << 16 |
				arr[start + 2] << 8  |
				arr[start + 3];
		
		return x;
	}
	
	public Camera getCamera() {
		return cam;
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
		Point min = coord2chunk(cam.getX() / BLOCK_SIZE, cam.getY() / BLOCK_SIZE);
		Point max = coord2chunk(cam.getMaxX() / BLOCK_SIZE, cam.getMaxY() / BLOCK_SIZE);
		
		for (int x = min.x; x <= max.x; x++) {
			for (int y = min.y; y <= max.y; y++) {
				getChunk(x, y).draw(g, cam);
			}
		}
			
		for (Entity e : entities) {
			e.draw(g, cam);
		}
	}
	
	public void update() {
		for (Chunk c : chunks) {
			c.update();
		}
		
		processCollisions();
		cam.update();
	}
}
