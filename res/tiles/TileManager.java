package tiles;

import java.awt.*;
import java.io.*;
import javax.imageio.*;
import main.*;
import tile.Tile;

public class TileManager{
	
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][];
	
	public TileManager(GamePanel gp) {
		
		this.gp = gp;
		tile = new Tile[10]; // can change this number for more tiles
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
		
		getTileImage("/tiles/grass.png", "/tiles/wall.png", "/tiles/water.png", "/tiles/earth.png", "/tiles/tree.png", "/tiles/sand.png"); // change map obstacle 
		loadMap("/maps/world01.txt"); // change map
	}
	
	public void getTileImage(String str0, String str1, String str2, String str3, String str4, String str5) {
		
		//String []arr = {str1, str2, str3, str4, str5, str6};
		
		/** Every odd number has collision.
		 * str1 = grass i=0 
		 * str2 = wall i=1
		 * str3 = sand i=2
		 * str4 = tree i=3
		 * str5 = earth i=4
		 * str6 = water i=5
		 */
		
		try {
			
			tile[0] = new Tile();
			tile[0].image = ImageIO.read(getClass().getResourceAsStream(str0)); // grass
			
			tile[1] = new Tile();
			tile[1].image = ImageIO.read(getClass().getResourceAsStream(str1)); //wall
			tile[1].collision = true;
			
			tile[2] = new Tile();
			tile[2].image = ImageIO.read(getClass().getResourceAsStream(str2)); //water
			tile[2].collision = true;
			
			tile[3] = new Tile();
			tile[3].image = ImageIO.read(getClass().getResourceAsStream(str3)); // earth
			
			tile[4] = new Tile();
			tile[4].image = ImageIO.read(getClass().getResourceAsStream(str4)); // tree
			tile[4].collision = true;
			
			tile[5] = new Tile();
			tile[5].image = ImageIO.read(getClass().getResourceAsStream(str5)); // sand
			

			 
		}
		catch(IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void loadMap(String filePath) {
		
		try {
			
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0; 
			
			while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
				
				String line = br.readLine();
				
				while(col < gp.maxWorldCol) {
					
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNum[col][row] = num;
					col++;
				}
				
				if(col == gp.maxWorldCol) {
					
					col = 0;
					row++;
				}
			}
			
			br.close();
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		
		int worldCol = 0;
		int worldRow = 0;
		
		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			
			int tileNum = mapTileNum[worldCol][worldRow];
			
			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;

			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
					worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
					worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
					worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
				
				g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
			}
			worldCol++;
			
			if(worldCol == gp.maxWorldCol) {
				
				worldCol = 0;
				worldRow++;
			}
			
		}
	}
}
