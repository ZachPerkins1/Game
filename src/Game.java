import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Game implements WindowListener, MouseListener, MouseMotionListener, KeyListener {
	private static final int RIGHT = 0;
	private static final int DOWN = 1;
	private static final int LEFT = 2;
	private static final int UP = 3;
	
	private boolean[] movement;
	
	private World world;
	private Entity player;
	
	private int currBlock = 1;
	
	
	public Game() {
		movement = new boolean[4];
		
		world = new World(50, 50);
		world.open("test.map");
		
		player = new Entity(world, 200, 200);
		world.addEntity(player);
		world.getCamera().setFollowing(player);
	}
	
	public void update() {
		if (!player.hasTarget())
			player.zero();
		
		for (int i = 0; i < 4; i++) {
			if (movement[i]) {
				player.cancelTarget();
				
				double m = 3;
				
				if (movement[(i+3) % 4] || movement[(i+1) % 4])
					m = 2.1;
				
				if (i > 1)
					m = -m;
				
				if (i % 2 == 0) 
					player.dx += m;
				else
					player.dy += m;	
			}
		}
						
		world.update();
		player.update();
	}
	
	public void draw(Graphics2D g) {
		world.draw(g);
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		//world.saveToFile("save.map");
		world.save("test.map");
	}
	
	@Override
	public void mouseClicked(MouseEvent m) {
		Point pos = world.pixel2coord(m.getX(), m.getY());
		
		if (m.getButton() == MouseEvent.BUTTON3) {
			try {
				player.setTarget(new Point(m.getX(), m.getY()));
			} catch (InvalidPathException e) {
				e.printStackTrace();
			}
		} else {
			int id = world.getBlockAt(pos.x, pos.y);
			if (id == 0) {
				world.placeBlock(pos.x, pos.y, currBlock);
			} else {
				world.placeBlock(pos.x, pos.y, 0);
			}
		}
	}
	
	
	@Override
	public void keyReleased(KeyEvent e) {		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (++currBlock > 2) {
				currBlock = 1;
			}
		}
		
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			movement[UP] = false;
			break;
		case KeyEvent.VK_S:
			movement[DOWN] = false;
			break;
		case KeyEvent.VK_D:
			movement[RIGHT] = false;
			break;
		case KeyEvent.VK_A:
			movement[LEFT] = false;
			break;
		}
	}
	
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			movement[UP] = true;
			break;
		case KeyEvent.VK_S:
			movement[DOWN] = true;
			break;
		case KeyEvent.VK_D:
			movement[RIGHT] = true;
			break;
		case KeyEvent.VK_A:
			movement[LEFT] = true;
			break;
		}
	}

	// Fuck u java
	public void windowActivated(WindowEvent e)   {}
	public void windowClosed(WindowEvent e)      {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e)   {}
	public void windowOpened(WindowEvent e)      {}
	public void mouseEntered(MouseEvent arg0)    {}
	public void mouseExited(MouseEvent arg0)     {}
	public void mousePressed(MouseEvent arg0)    {}
	public void mouseReleased(MouseEvent arg0)   {}
	public void keyTyped(KeyEvent e)             {}
	public void mouseDragged(MouseEvent arg0)    {}
	public void mouseMoved(MouseEvent arg0)      {}

}
