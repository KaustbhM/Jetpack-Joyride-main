package Characters;



public class Coin extends Obstacle{
	private int x;
	private int y;
	private int width;
	private int height;
	//valid keeps track of whether the player hit the obstacle or not
	//it is able to change by sending special values to contains method
	private boolean valid = true;
	
	public Coin(int id, int x, int y, int diameter) {
		super(id);
		this.x = x;
		this.width = diameter;
		this.y = y;
		this.height = diameter;
	}

	@Override
	public boolean potentialCollison() {
		if (x > 550)
			return false;
		return true;
	}
	
	private float dist(int x, int y, int x1, int y1) {
		float distance = (float) (Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2)));
		return distance;

	}

	@Override
	public boolean contains(int x, int y) {
		// doesn't matter if height or width
		int radius = (width/2);
		int centerX = this.x + (width/2);
		int centerY = this.y + (width/2);
		if (dist(centerX, centerY, x, y) <= radius) {
			return true;
		}
		return false;
	}

	@Override
	public void iterate() {
		x -= 10;
		
	}

	@Override
	public boolean invalidObstacle() {
		if (x + width < 0 || !valid) {
			return true;
		}
		return false;
	}

	@Override
	public int[] getDefinitions() {
		int[] definition = {x, y ,width, height};
		return definition;
	}

}



