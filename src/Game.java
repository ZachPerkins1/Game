import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

public class Game implements WindowListener, MouseListener, MouseMotionListener, KeyListener {
	private static final int RIGHT = 0;
	private static final int DOWN = 1;
	private static final int LEFT = 2;
	private static final int UP = 3;
	
	private boolean[] movement;
	
	private World world;
	private Camera c;
	private Entity player;
	
	private int currBlock = 1;
	
	
	public Game() {
		movement = new boolean[4];
		
		world = new World();
		try {
			world.open("a.map");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		player = new Player(world, 100, 300);
		world.addEntity(player);
		c = world.getCamera();
		//c.setFollowing(player);
	}
	
	public void update() {
		// Zero the speeds before we adjust them
		if (!player.hasTarget())
			player.zero();
		
		// Determine the new movement speed of the player
		for (int i = 0; i < 4; i++) {
			if (movement[i]) {
				player.cancelTarget();
				
				double m = 3;
				
				//If there is movement in the x 
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
	
	public void draw() {
		world.draw();
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		try {
			world.save("a.map");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent m) {
		Point pos = world.pixel2coord(c.getX(m.getX()), c.getY(m.getY()));
		
		if (m.getButton() == MouseEvent.BUTTON3) {
			try {
				player.setTarget(new Point(c.getX(m.getX()), c.getY(m.getY())));
			} catch (InvalidPathException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(pos);
			int id = world.blockAt(pos.x, pos.y).getID();
			System.out.println(id);
			if (id == 0) {
				world.place(pos.x, pos.y, currBlock);
			} else {
				world.place(pos.x, pos.y, 0);
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
