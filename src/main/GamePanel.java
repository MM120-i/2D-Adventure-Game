package main;

import java.awt.*;
import javax.swing.*;
import entity.Player;
import object.SuperObject;
import tiles.TileManager;

public class GamePanel extends JPanel implements Runnable{
	
	// screen settings
	private static final long serialVersionUID = 1L;
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
	KeyHandler keyH = new KeyHandler(this);
	Sound se = new Sound();
	Sound music = new Sound();
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI userInterface = new UI(this); 
	Thread gameThread;
	
	//entity and objects
	public Player player = new Player(this, keyH);
	public SuperObject obj[] = new SuperObject[10];
	
	// game state
	public int gameState;
	public final int playState = 1;
	public final int pauseState = 2;
	
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
		stopMusic();		// Stopped cuz the music gets annoying while debugging 
		
		gameState = playState;
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override	
	public void run() {
		
		double drawInterval = 1000000000.0 / FPS;
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
				drawCount = 0;
				timer = 0;
			}
		
		}
	}
	
	public void update() {
		
		if(gameState == playState) {
			player.update();	
		}		
		
		if(gameState == pauseState) {
			// nothing for now
			return;
		}
	}


	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;		
		
		// DEBUG
		long drawStart = 0l;
		
		if(keyH.checkDrawTime) {
			drawStart = System.nanoTime();
		}
		
		
		tileM.draw(g2);
		
		for(int i = 0; i < obj.length; i++) {

			if(obj[i] != null) {
				obj[i].draw(g2, this);
			}
		}
		
		// PLAYER
		player.draw(g2);
		
		// UI
		userInterface.draw(g2);
		
		// debug
		if(keyH.checkDrawTime) {
			long drawEnd = System.nanoTime();
			long passed = drawEnd - drawStart;
			g2.setColor(Color.white);
			g2.drawString("Draw Time: " + passed, 10, 400);
		}
		
		g2.dispose();
	}
	
	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}
	
	public void stopMusic() {
		music.stop();
	}
	
	public void playSE(int i) {
		se.setFile(i);
		se.play();
	}
}
