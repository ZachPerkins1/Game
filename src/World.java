import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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
		for (Entity e : entities) {
			double sx = 0;
			double sy = 0;
			double x = e.x;
			double y = e.y;
			
			double ty = y + e.dy;
			double tx = x + e.dx;
			
			if (Math.abs(e.dx) > Math.abs(e.dy)) {
				sx = relativeMin(e.dx, blockSize);
				sy = (e.dy/e.dx) * sx;
			} else if (e.dy != 0) {
				sy = relativeMin(e.dy, blockSize);
				sx = (e.dx/e.dy) * sy;
			}
									
			while (sx != 0 || sy != 0) {		
				x += relativeMin(sx, Math.abs(x - (e.x + e.dx)));
				y += relativeMin(sy, Math.abs(y - (e.y + e.dy)));
				
				if (isColliding(x, y, e.getW(), e.getH())) {
					x -= sx;
					y -= sy;
					
					int fx = (int) relativeMin(sx, 1);
					int fy = (int) relativeMin(sy, 1);
					
					boolean success = false;
					while (!success) {
						x += fx;
						if (isColliding(x, y, e.getW(), e.getH())) {
							sx = 0;
							x -= fx;
							success = true;
						} else {
							y += fy;
							if (isColliding(x, y, e.getW(), e.getH())) {
								sy = 0;
								y -= fy;
								success = true;
							}
						}
					}
				}
				
				if ((x == tx || sx == 0) && (y == ty || sy == 0))
					break;
			}
			
			e.x = (int) x;
			e.y = (int) y;
		}
	}
	
	// TODO: create another version of this that takes a collision event
	private boolean isColliding(double x, double y, int width, int height) {
		x = (x - (width / 2));
		y = (y - (height / 2));
				
		int bx = (int) x / blockSize;
		int by = (int) y / blockSize;
		int bw = (int) (width + (x % blockSize)) / blockSize + 1;
		int bh = (int) (height + (y % blockSize)) / blockSize + 1;
						
		for (int i = bx; i < bx + bw; i++) {
			for (int j = by; j < by + bh; j++) {
				if (Statics.COL[map[i][j]]) {
					int px = i * blockSize;
					int py = j * blockSize;
					
					if (new Rectangle(px, py, blockSize, blockSize).intersects(new Rectangle((int)x, (int)y, width, height))) {
						return true;
					}
				}	
			}
		}
		
		return false;
	}
	
	private double relativeMax(double a, double b) {
		if (a < 0) {
			b = -b;
			return Math.min(a, b);
		}
		
		return Math.max(a, b);
	}
	
	private double relativeMin(double a, double b) {
		if (a < 0) {
			b = -b;
			return Math.max(a, b);
		}
		
		return Math.min(a, b);
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
		
		processCollisions();
	}
}
