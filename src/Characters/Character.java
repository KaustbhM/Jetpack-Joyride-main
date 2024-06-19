/**
 * 
 */
package Characters;

/**
 * 
 */
public abstract class Character {

	private int x;
	private int y;
	
	private int height;
	private int width;
	
	private int xVelocity;
	
	abstract public boolean potentialCollison();
	abstract public boolean contains(int x, int y);
	abstract public void iterate();
	
	
}
