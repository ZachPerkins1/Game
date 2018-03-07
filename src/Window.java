import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JPanel {
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 1000;
	
	public static void main(String[] args) {
		Window w = new Window();
		w.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(w);
		frame.pack();
		frame.setVisible(true);
		
		w.game = new Game();
		
		frame.addWindowListener(w.game);
		w.addMouseListener(w.game);
		w.addMouseMotionListener(w.game);
		frame.addKeyListener(w.game);
				
		w.start();
	}
	
	private World world = null;
	private Entity c;
	private Game game;
	
	public void start() {
		while (true) {
			game.update();
			this.repaint();
			
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		game.draw(g2d);
	}
}
