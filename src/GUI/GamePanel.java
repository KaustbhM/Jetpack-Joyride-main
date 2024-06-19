
package GUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.Iterator;

import Characters.Caveman;
import Characters.Character;
import Characters.Coin;
import Characters.Obstacle;
import Characters.Bat;
import Characters.Block;
import util.FirebaseHelper;

public class GamePanel extends JPanel implements ActionListener {

	private JButton startBtn = new JButton();

	// Sprite
	Caveman caveman = new Caveman();
	
	//Obstacles
	private BufferedImage block;
	private BufferedImage coin;
	private BufferedImage bat;
	ArrayList<BufferedImage> batAnimations = new ArrayList<BufferedImage>();
	private Image floorImg;
	
	private Timer time;

	private Image backgroundImage;
	private int bgX = 0;
	
	private final int BSPEED = 2;
	private final int SPEED = 10;
	private int flX = 0;
	public boolean gameOver = false;

	// Obstacle Related Variables
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public final int maxWidth = (int) screenSize.getWidth(); // 1980
	public final int maxHeight = (int) screenSize.getHeight() - 210; // 1000
	private ArrayList<Obstacle> obstacles;
	private int obstacleCheckpoint = (int) screenSize.getWidth() / 2;
	
	private int difficulty = Constants.INIT_DIFFICULTY;
	// private int[] obstacleDimensions = { (int) (maxHeight * .1), (int) (maxHeight
	// * .125), (int) (maxHeight * .2),
	// (int) (maxHeight * .25), (int) (maxHeight * .325) };
	private int[] obstacleDimensions = { (int) (maxHeight * .1), (int) (maxHeight * .125), (int) (maxHeight * .2),
			(int) (maxHeight * .25) };

	// Scores Related Variable
	private int coins;
	private int score;
	private int scoreTimer = Constants.SCORE_TIMER;

	private static final int BTN_SIZE = 50;
	private Random random = new Random();
	
	// DAtabase
	// FirebaseHelper firebaseHelper = new FirebaseHelper();

	FirebaseHelper firebaseHelper;
	
	public GamePanel() {
	    
		firebaseHelper =  FirebaseHelper.getInstance();
		addEventHandlers();
		backgroundImage = new ImageIcon(getClass().getResource("cave.jpg")).getImage();
		floorImg = new ImageIcon(getClass().getResource("floor.jpg")).getImage();
		if (backgroundImage.getWidth(null) == -1) {
			System.out.println("Image not loaded properly");
		}

		coins = 0;
		score = 0;

		// Load all images
		loadImages();

		System.out.println("Screen width " + maxWidth + " Screen height " + maxHeight);

		obstacles = new ArrayList<Obstacle>();
		obstacles.add(new Block(1, 200, 50, 50, 800));

		time = new Timer(5, this);
		time.start();
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setPreferredSize(new Dimension(800, 600));

	}
	
	public void reset() {
		
		coins = 0;
		score = 0;
		scoreTimer = Constants.SCORE_TIMER;
		gameOver = false;
		obstacles = new ArrayList<Obstacle>();
		obstacles.add(new Block(1, 200, 50, 50, 800));
		
		time.restart();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(scoreTimer == 0) {
		   score++;
		   scoreTimer = Constants.SCORE_TIMER;
		}
		scoreTimer--;
		
		moveBackground();
		caveman.move();
		moveFloor();
		updateObstacles();
		moveObstacles();
		detectCollison();
		repaint();

	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D twoD = (Graphics2D) g;
		super.paintComponent(twoD);

		// Call the coin drawing method

		// Draw the continuous looping background
		twoD.drawImage(backgroundImage, bgX, 0, null);

		// g.drawImage(backgroundImage, bgX, 0, 1920, 1080, null);
		if (bgX < 0) {
			twoD.drawImage(backgroundImage, bgX + backgroundImage.getWidth(null), 0, null);

		}

		BufferedImage man = null;
		BufferedImage fire = null;
		try {
			man = ImageIO.read(getClass().getResourceAsStream("caveman.png"));
			fire = ImageIO.read(getClass().getResourceAsStream("fire.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		twoD.drawImage(man, caveman.getX(), caveman.getY(), 175, 100, null);
		// only draws fire image when caveman is moving up
		if (caveman.dyUp < caveman.dyDown) {
			twoD.drawImage(fire, caveman.getX() + 37, caveman.getY() + 62, 40, 65, null);
		}

		// Draw obstacles
		for (Obstacle obstacle : obstacles) {

			String obstacleType = obstacle.getClass().getName();

			g.setColor(Color.BLUE);
			int[] definitions = obstacle.getDefinitions();
			if (obstacleType.equals("Characters.Block")) {
				g.drawImage(block, definitions[0], definitions[1], definitions[2], definitions[3], null);
			}
			if (obstacleType.equals("Characters.Coin")) {
				g.drawImage(coin, definitions[0], definitions[1], definitions[2], definitions[3], null);
			}
			if (obstacleType.equals("Characters.Bat")) {
				int animatedId = ((Bat)obstacle).getAnimatedId();
				// g.drawImage(bat, definitions[0], definitions[1], definitions[2], definitions[3], null);
				g.drawImage(batAnimations.get(animatedId), definitions[0], definitions[1], definitions[2], definitions[3], null);
			}
			// g.drawImage(block, 400, 400, 50, 50, null);
			twoD.drawImage(floorImg, flX, 548, null);
	        twoD.drawImage(floorImg, flX +floorImg.getWidth(null), 548, null);
		}

		// Update the coin score
		drawCoinScore(g);
		
		// draw Score
		drawScore(g);

		// createComponents();
	}

	private void drawCoinScore(Graphics g) {
		// Update the coin score
		g.drawImage(coin, maxWidth - 500, 30, 30, 30, null);
		g.setColor(Color.black);

		Font font = new Font("Arial", Font.BOLD, 15);
		g.setFont(font);
		g.drawString(String.valueOf(coins), maxWidth - 500 + 10, 49);
	}
	
	private void drawScore(Graphics g) {
		// Update the coin score
		// g.drawImage(coin, maxWidth - 500, 30, 30, 30, null);
		g.setColor(Color.WHITE);

		Font font = new Font("Arial", Font.BOLD, 20);
		g.setFont(font);
		g.drawString(String.valueOf(score), maxWidth - 560, 49);
	}

	public void createComponents() {
		startBtn.setBackground(Color.black);
		startBtn.setBorderPainted(false);
		setLayout(null);
		startBtn.setBounds(370, 450, 250, 100);
		this.add(startBtn);
		try {
			Image img = ImageIO.read(getClass().getResourceAsStream("play.png"));
			startBtn.setIcon(new ImageIcon(img));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	private void addEventHandlers() {
		// a mouse listener requires a full interface with lots of methods.
		// to get around having implement all, we use the MouseAdapter class
		// and override just the one method we're interested in.

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();

				if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) {
					caveman.dyUp = -20;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				caveman.dyUp = 5;
			}
		});

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				caveman.dyUp = -10;
				caveman.mousePressed = true;
				caveman.mouseReleased = false;

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				caveman.dyUp = 5;
				caveman.mouseReleased = true;
				caveman.mousePressed = false;
			}
		});
	}

	public void updateAnimation() {

	}

	// New Code
	private void moveBackground() {
		bgX -= BSPEED;
		if (bgX == -backgroundImage.getWidth(null)) {
			bgX = 0;
		}
	}

	public void drawCoin(Graphics g) {

	}

	private void loadImages() {
		// block = new ImageIcon(getClass().getResource("block.png")).getImage();
		try {
			block = ImageIO.read(getClass().getResourceAsStream("rock.png"));
			coin = ImageIO.read(getClass().getResourceAsStream("coin.png"));
			// bat = ImageIO.read(getClass().getResourceAsStream("bat.png"));
			batAnimations.add(ImageIO.read(getClass().getResourceAsStream("bat.png")));
			batAnimations.add(ImageIO.read(getClass().getResourceAsStream("bat2.png")));
			
		} catch (Exception ex) {
			ex.printStackTrace();

		}

	}

	private void moveObstacles() {
		for (Obstacle obstacle : obstacles) {
			obstacle.iterate();
		}
	}

	/**
	 * private void updateObstacles() { int[] lastObstacleDefinitions =
	 * obstacles.get(obstacles.size() - 1).getDefinitions();
	 * 
	 * if (2 * lastObstacleDefinitions[0] < obstacleCheckpoint + (difficulty * 0.2 *
	 * obstacleCheckpoint)) {
	 * 
	 * int numOfObstacles =
	 * Constants.OBSTACLE_NUMBERS[random.nextInt(Constants.OBSTACLE_NUMBERS.length)];
	 * 
	 * int obstacleDimension =
	 * obstacleDimensions[random.nextInt(obstacleDimensions.length)];
	 * 
	 * if (numOfObstacles == 1) { obstacles.add(new Block(1, maxHeight/2,
	 * obstacleDimension, obstacleDimension, maxWidth));
	 * 
	 * }
	 * 
	 * if (numOfObstacles == 2) {
	 * 
	 * obstacles.add(new Block(1, 0, obstacleDimension, obstacleDimension,
	 * maxWidth)); obstacles.add(new Block(1, maxHeight - obstacleDimension,
	 * obstacleDimension, obstacleDimension, maxWidth)); }
	 * 
	 * if (numOfObstacles == 3) {
	 * 
	 * }
	 * 
	 * if (numOfObstacles == 4) {
	 * 
	 * }
	 * 
	 * } }
	 */

	private void updateObstacles_backup3() {
		// Increase the speed and frequency of obstacles as time progresses
		if (time.getDelay() > 1) {
			time.setDelay(time.getDelay() - 1); // Decrease delay to increase speed
		}

		// Check if we need to add new obstacles
		int[] lastObstacleDefinitions = obstacles.get(obstacles.size() - 1).getDefinitions();
		if (lastObstacleDefinitions[0] < maxWidth - obstacleCheckpoint) {
			int numGroups = random.nextInt(2) + 1; // Random number between 1 and 2 for groups
			int totalObstacles = random.nextInt(4) + 1; // Random number between 1 and 4 for obstacles per group

			if (numGroups == 1) {
				// One group centered vertically
				int width = obstacleDimensions[random.nextInt(obstacleDimensions.length)];
				int height = obstacleDimensions[random.nextInt(obstacleDimensions.length)];
				int x = maxWidth;
				int y = (maxHeight - height) / 2; // Centered vertically

				obstacles.add(new Block(1, y, width, height, maxWidth));
				

				// Add coins following the obstacle
				int numCoins = random.nextInt(10) + 1; // Random number of coins between 1 and 10
				int coinDiameter = 30; // Diameter of each coin
				for (int i = 0; i < numCoins; i++) {
					int coinX = x + width + (i * coinDiameter * 2);
					int coinY = y + (i % 2 == 0 ? -coinDiameter : coinDiameter); // Arrange in two rows
					obstacles.add(new Coin(2, coinX, coinY, coinDiameter));
				}
			} else {
				// Two groups, one at the top and one at the bottom
				int gapSize = 200; // Space for the sprite to pass through
				int obstacleHeight = (maxHeight - gapSize) / 2;

				for (int i = 0; i < totalObstacles; i++) {
					int width = obstacleDimensions[random.nextInt(obstacleDimensions.length)];
					// int height = obstacleDimensions[random.nextInt(obstacleDimensions.length)];
					int height = width;
					int x = maxWidth;

					int topBound = obstacleHeight - height;
					int yTop = random.nextInt(Math.max(1, topBound)); // Top group

					int bottomBound = obstacleHeight - height;
					int yBottom = maxHeight - obstacleHeight + random.nextInt(Math.max(1, bottomBound)); // Bottom group

					obstacles.add(new Block(1, yTop, width, height, maxWidth));
					obstacles.add(new Block(1, yBottom, width, height, maxWidth));
				}

				// Add coins in the center when there are two groups of obstacles
				int numCoins = random.nextInt(10) + 1; // Random number of coins between 1 and 10
				int coinDiameter = 30; // Diameter of each coin
				for (int i = 0; i < numCoins; i++) {
					int coinX = maxWidth + (i * coinDiameter * 2);
					int coinY = (maxHeight / 2) - (coinDiameter / 2) + (i % 2 == 0 ? -coinDiameter : coinDiameter); // Arrange
																													// in
																													// two
																													// rows
					obstacles.add(new Coin(2, coinX, coinY, coinDiameter));
				}
			}

			obstacleCheckpoint = maxWidth + random.nextInt(maxWidth / 2); // Set a new checkpoint for adding obstacles
		}
	}

	private void updateObstacles() {
		// Increase the speed and frequency of obstacles as time progresses
		if (time.getDelay() > 1) {
			time.setDelay(time.getDelay() - 1); // Decrease delay to increase speed
		}

		// Check if we need to add new obstacles
		int[] lastObstacleDefinitions = obstacles.get(obstacles.size() - 1).getDefinitions();
		if (lastObstacleDefinitions[0] < maxWidth - obstacleCheckpoint) {
			int numGroups = random.nextInt(2) + 1; // Random number between 1 and 2 for groups
			int totalObstacles = random.nextInt(4) + 1; // Random number between 1 and 4 for obstacles per group

			int squareSize = obstacleDimensions[random.nextInt(obstacleDimensions.length)];
			int gapBetweenObstacles = 10; // Gap between obstacles in a group
			int x = maxWidth;

			if (numGroups == 1) {
				// One group centered vertically
				int y = (maxHeight / 2) - (squareSize / 2); // Centered vertically

				System.out.println("Group = 1" + " No of Obstacles " + totalObstacles);
				int obstacleID = getObstacleID();
				for (int i = 0; i < totalObstacles; i++) {
					int yOffset = (i % 2 == 0) ? -squareSize - gapBetweenObstacles : squareSize + gapBetweenObstacles;
					// obstacles.add(new Block(1, y + yOffset, squareSize, squareSize, x));
					obstacles.add(getObstacle(obstacleID,y + yOffset, squareSize, squareSize, x));
					x += squareSize + gapBetweenObstacles; // Move x right for the next square
				}

				// Add coins following the obstacle
				int numCoins = random.nextInt(10) + 1; // Random number of coins between 1 and 10
				int coinDiameter = 30; // Diameter of each coin
				for (int i = 0; i < numCoins; i++) {
					int coinX = x + (i * coinDiameter * 2);
					int coinY = y + (i % 2 == 0 ? -coinDiameter : coinDiameter); // Arrange in two rows
					obstacles.add(new Coin(2, coinX, coinY, coinDiameter));
				}
			} else {
				System.out.println("Group = 2" + " No of Obstacles " + totalObstacles);
				// Two groups, one at the top and one at the bottom
				int gapSize = 200; // Space for the sprite to pass through
				int topY = (maxHeight / 2) - gapSize / 2 - squareSize - gapBetweenObstacles;
				int bottomY = (maxHeight / 2) + gapSize / 2 + gapBetweenObstacles;

				for (int i = 0; i < totalObstacles; i++) {

					obstacles.add(new Block(1, topY, squareSize, squareSize, x));
					obstacles.add(new Block(1, bottomY, squareSize, squareSize, x));
					x += squareSize + gapBetweenObstacles; // Move x right for the next square
				}

				// Add coins in the center when there are two groups of obstacles
				int numCoins = random.nextInt(10) + 1; // Random number of coins between 1 and 10
				int coinDiameter = 30; // Diameter of each coin
				for (int i = 0; i < numCoins; i++) {
					int coinX = maxWidth + (i * coinDiameter * 2);
					int coinY = (maxHeight / 2) - (coinDiameter / 2) + (i % 2 == 0 ? -coinDiameter : coinDiameter); // Arrange
																													// in
																													// two
																													// rows
					obstacles.add(new Coin(2, coinX, coinY, coinDiameter));
				}
			}

			obstacleCheckpoint = maxWidth + random.nextInt(maxWidth / 2); // Set a new checkpoint for adding obstacles
		}
	}

	private boolean detectCollison() {
		List<Obstacle> obstaclesToRemove = new ArrayList<>();

		Iterator<Obstacle> it = obstacles.iterator();
		ArrayList<Integer[]> points = caveman.getPoints();
//		for (int i = 0; i < obstacles.size(); i++) {
		while (it.hasNext()) {
//			if (obstacles.get(i).potentialCollison()) { 
			Obstacle obstacle = it.next();
			if (obstacle.potentialCollison()) {
				// this line makes it so it won't check for collisions when the object is too
				// far away
				for (Integer[] coord : points) {
					if (obstacle.contains(coord[0], coord[1])) {
						if (obstacle.getId() == 2) {
							coins += 1;
							// it.remove();
							obstaclesToRemove.add(obstacle); // Mark for removal
							// i-- insures nothing is skipped in array when there is a deletion
//							i--;
						} else {
							// game ends here
							time.stop();
							
							//Persist Score
							firebaseHelper.updateScore("Kaustubh", score);
							gameOver = true;
							return true;
						}
					}
				}
			}
		}

		// Remove all marked obstacles
		obstacles.removeAll(obstaclesToRemove);

		return false;

	}
	
	private int getObstacleID() {
		int numObstacles = Constants.OBSTACLE_NAMES.length;
		int index = random.nextInt(numObstacles);
		System.out.println("Index is " + index + "numObstacles " +numObstacles);
		
		String obstacleName = Constants.OBSTACLE_NAMES[index];
		
		if("Block".equals(obstacleName)) {
			return 1;
		}else {
			return 3;
		}
	}
	
	private Obstacle getObstacle(int id,int y, int width, int height, int x) {
		
		if(id == 1) {
			return new Block(1,y,width,height,x);
		}else {
			return new Bat(3,y,width,height,x);
		}
			
	}

	public boolean getGameStatus() {
		return gameOver;
	}
	
	private void moveFloor() {
	      flX -= SPEED;
	      if (flX == -floorImg.getWidth(null)) {
	        flX = 0;
	      }
	}

}
