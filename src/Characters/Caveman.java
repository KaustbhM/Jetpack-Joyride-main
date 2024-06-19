
package Characters;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Caveman {
	int x;
	public int dyDown = -1 ;
	public int dyUp = 5;
	public int y;
	public int floor = 470;
	public int top = -20;
	public BufferedImage img;
	int width = 40;
	int height = 65;
	
	public boolean mouseReleased;
	public boolean mousePressed;
	public Caveman() {
		x = 20;
		y = 200;
	}
	
	public int count = 5;
	public void move() {
		y += dyUp-dyDown;
		if (mouseReleased == true) {
			count-=2;
			y-=count;
		}
		if (mousePressed == true) {
			count = 15;
			
		}
		if (this.y >= this.floor) {
	        //dont go beneath the floor
	        this.y = this.floor;
	    }
		if (this.y <= this.top) {
	        //dont go beneath the floor
	        this.y = this.top;
	    }
	}
	
//	public void move() {
//        // change velocity by the gravity
//        this.dyUp += dyDown;
//        //change position by the gravity
//        this.y -= this.dyUp;
//		
//		
//		
//
//        if (this.y >= this.floor) {
//            this.dyDown = -9;
//            this.dyUp = 20;
//            //dont go beneath the floor
//            this.y = this.floor;
//        }
//    }
	
	public ArrayList<Integer[]> getPoints() {
		ArrayList<Integer[]> points = new ArrayList<Integer[]>();
		//TO increase efficiency I determined the hitbox points through keypoints - corner points in addition to  other spaced out points
		// Imagine a connect the dots scenario
		//I use psuedo coords and dimensions so that the hitbox is a little bit smaller than the actual player
		for (int i = (int) x; i < x + width; i+= width / 5){
			for (int j = (int) y; j < y + height; j+= height / 5) {
				Integer[] coords = {i, j};
				points.add(coords);
			}
		}
		return points;
	}

	public int getX() {
		// TODO Auto-generated method stub
		return x;
	}
	public int getY() {
		// TODO Auto-generated method stub
		return y;
	}
}
