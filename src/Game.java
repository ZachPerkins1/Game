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
	private World world;
	private Entity player;
	
	private int currBlock = 1;
	
	public Game() {
		world = new World(50, 50);
		world.open("test.map");
		
		player = new Entity(world, 5, 5);
		world.addEntity(player);
	}
	
	public void update() {
		world.update();
		player.update();
	}
	
	public void draw(Graphics2D g) {
		world.draw(g);
		player.draw(g);
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
		case KeyEvent.VK_S:
			player.dy = 0;
			break;
		case KeyEvent.VK_D:
		case KeyEvent.VK_A:
			player.dx = 0;
			break;
		}
	}
	
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			player.dy = -2;
			break;
		case KeyEvent.VK_S:
			player.dy = 2;
			break;
		case KeyEvent.VK_D:
			player.dx = 2;
			break;
		case KeyEvent.VK_A:
			player.dx = -2;
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
