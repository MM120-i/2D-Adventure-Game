package main;

import java.awt.*;
import javax.swing.*;
import entity.Player;
import object.SuperObject;
import tiles.TileManager;

public class GamePanel extends JPanel implements Runnable{
	
	// screen settings
	
	final int originalTileSize = 16; // 16 x 16
	final int scale = 3;
	public final int tileSize = originalTileSize * scale; // 48 x 48 tile
	public final int  maxScreenCol = 16; //can be changed
	public final int maxScreenRow = 12; // can be changed
	public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
	public final int screenHeight = tileSize * maxScreenRow; // 576 pixels
	
	// world settings:
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	
	//fps
	int FPS = 60;
	
	//system
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler();
	Sound sound = new Sound();
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	Thread gameThread;
	
	//entity and objects
	public Player player = new Player(this, keyH);
	public SuperObject obj[] = new SuperObject[10];
	
	public GamePanel() {
		
		Dimension dim = new Dimension(screenWidth, screenHeight);
		this.setPreferredSize(dim);
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void setupGame() {
		
		aSetter.setObject();
		
		playMusic(0);
	}
	
	public void startGameThread() {
		
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override	
	public void run() {
		
		double drawInterval = 1000000000.0/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0; 
		
		for(;gameThread != null;) {
			
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			 switch ((int) delta) {
	            case 0:
	                // No action needed
	                break;
	            case 1:
	            	
	                update();
	                repaint();
	                delta = delta - 1;
	                drawCount = drawCount + 1;
	                break;
	                
	            default:
	                // Handle other cases if necessary
	                break;
	        }
			
			if(timer >= 1000000000) {
				
				//System.out.println("FPS: " + drawCount); Displays the FPS in the console while game is running
				drawCount = 0;
				timer = 0;
			}
		
		}
	}
	
	public void update() {
		
		player.update();
	}


	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		tileM.draw(g2);
		
		for(int i = 0; i < obj.length; i++) {
			
			if(obj[i] != null) {
				obj[i].draw(g2, this);
			}
		}
		
		player.draw(g2);
		
		g2.dispose();
	}
	
	public void playMusic(int i) {
		
		sound.setFile(i);
		sound.play();
		sound.loop();
	}
	
	public void stopMusic() {
		
		sound.stop();
	}
	
	public void playSE(int i) {
		
		sound.setFile(i);
		sound.play();
	}

}
