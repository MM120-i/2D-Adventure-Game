package entity;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import main.*;

public class Player extends Entity{
	
	KeyHandler KeyH;
	
	public final int screenX, screenY;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		
		super(gp);
		
		this.KeyH = keyH;
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		solidArea = new Rectangle(8, 16, 32, 32); // player hitbox can be modified
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		setDefaultValues();
		getPlayerImage();
	}
	
	public void setDefaultValues() {
		
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		speed = 4;
		direction = "down";
	}
	
	public void getPlayerImage() {
		
		up1 = setup("boy_up_1");
		up2 = setup("boy_up_2");
		
		down1 = setup("boy_down_1");
		down2 = setup("boy_down_2");
		
		left1 = setup("boy_left_1");
		left2 = setup("boy_left_2");
		
		right1 = setup("boy_right_1");
		right2 = setup("boy_right_2");
	}
	
	public BufferedImage setup(String imageName) {
		
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		
		try {
			
			image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
			image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}
	
	public void update() {
		
		if(KeyH.upPressed == true || KeyH.downPressed == true || 
				KeyH.leftPressed == true || KeyH.rightPressed == true) {
			
			if(KeyH.upPressed == true) {
				
				direction = "up";
			}
			else if(KeyH.downPressed == true) {
				
				direction = "down";
			}
			else if(KeyH.leftPressed == true) {
				
				direction = "left";
			}
			else if(KeyH.rightPressed == true) {
				
				direction = "right";
			}
			
			//check tile collision
			collisionOn = false;
			gp.cChecker.checkTile(this);
			
			//check object collision
			int objIndex = gp.cChecker.checkObject(this, true);
			pickUpObject(objIndex);
			
			if(collisionOn == false) {
				
				switch(direction) {
					
					case "up":
						worldY -= speed;
						break;
						
					case "down":
						worldY += speed;
						break;
						
					case "left":
						worldX -= speed;
						break;
						
					case "right":
						worldX += speed;
						break;
				}
			}
			
			spriteCounter++;
			
			if(spriteCounter > 10) {
				
				if(spriteNum == 1) spriteNum = 2;
				else if(spriteNum == 2) spriteNum = 1;
				spriteCounter = 0;
			}
		}
		
	}
	
	public void pickUpObject(int i) {
		
		if(i != 999) {}

	}
	
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		
		switch(direction) {
		
			case "up":
				
				if(spriteNum == 1) {
					image = up1;
				}
				
				if(spriteNum == 2) {
					image = up2;
				}
				
				break;
				
			case "down":
				
				if(spriteNum == 1) {
					image = down1;
				}
				
				if(spriteNum == 2) {
					image = down2;
				}
				
				break;
				
			case "left":
				
				if(spriteNum == 1) {
					image = left1;
				}
				
				if(spriteNum == 2) {
					image = left2;
				}
				
				break;
				
			case "right":
				
				if(spriteNum == 1) {
					image = right1;
				}
				
				if(spriteNum == 2) {
					image = right2;
				}
				
				break;
			
			default: 
				break;
		}
		
		g2.drawImage(image, screenX, screenY, null);
	}

}
