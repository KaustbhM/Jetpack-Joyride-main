package Characters;

public abstract class Obstacle {
	//id used for testing (to identify obstacle type, might at one point be used in code for visuals)
	private final int id;
	private int playerCoordY;
	public Obstacle(final int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	abstract public boolean potentialCollison();
	abstract public boolean contains(int x, int y);
	abstract public void iterate();
	//this detects if the obstacle already passed the player and screen
	abstract public boolean invalidObstacle();
	abstract public int[] getDefinitions();
	public void updatePlayerCoordY(int playerCoordY) {
		this.playerCoordY = playerCoordY;
	}
	public int getPlayerCoordY() {
		return playerCoordY;
	}
	
}

