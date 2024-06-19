
package GUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.util.ArrayList;

public class Enviornment extends JFrame {
	BufferedImage backgroundImage;

	private GamePanel panel;

	private StartPanel startPanel;

	// private BackgroundPanel backgroundPanel;

	private static volatile boolean done = false;
	private static int delay = 20;

	Timer progress;

	public static void startGUI() throws InterruptedException {
		Enviornment theGUI = new Enviornment();
		SwingUtilities.invokeLater(() -> theGUI.createFrame(theGUI));
		synchronized (theGUI) {
			theGUI.wait();
		}
	}

	public void createFrame(Object semaphore) {

		try {
			backgroundImage = ImageIO.read(getClass().getResourceAsStream("cave.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.setSize(backgroundImage.getWidth(), backgroundImage.getHeight());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		buildStartScreen();

		// Add startPanel to the frame
		this.setContentPane(startPanel);

		// Set this JFrame to be visible
		this.setVisible(true);

		System.out.println("All done creating our frame");
		// tell the main thread that we are done creating our dialogs.
		// This allows the main thread to stop wait()'ing.
		synchronized (semaphore) {
			semaphore.notify();
		}

		/** My Changes - End ***/

	}

	private void buildStartScreen() {

		startPanel = new StartPanel();

		// Add buttons to start panel
		JButton startButton = new JButton();
		startButton.setBackground(Color.black);
		startButton.setBorderPainted(false);
		setLayout(null);
		startButton.setBounds(370, 450, 250, 100);
		startPanel.add(startButton);

		try {
			Image img = ImageIO.read(getClass().getResourceAsStream("play.png"));
			startButton.setIcon(new ImageIcon(img));
		} catch (Exception ex) {
			System.out.println(ex);
		}

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startGame();
				revalidate();
			}
		});
		
		JLabel label = new JLabel();
		label.setText("Last Score: " + String.valueOf(startPanel.getPreviousScore()));
		label.setBounds(200, 200, 250, 100);
		label.setOpaque(true);
		label.setBackground(Color.GREEN);
		label.setForeground(Color.BLACK);
		label.setFont(new Font("Arial", Font.BOLD, 25));
		startPanel.add(label);
	}

	/**
	 * 
	 */
	private void startGame() {

		if (panel == null) {
			panel = new GamePanel();

			panel.setBounds(0, 0, 1920, 1080);
			add(panel);
		}
		panel.setVisible(false);

		// Set the current panel and make it visible

		panel.setVisible(true);

		this.setContentPane(panel);

		revalidate();

		progress = new javax.swing.Timer(5000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (panel.getGameStatus()) {

					// EndGamePanel endPanel = new EndGamePanel();

					/*
					JButton endButton = new JButton();
					endButton.setBackground(Color.black);
					endButton.setBorderPainted(false);
					setLayout(null);
					endButton.setBounds(370, 450, 250, 100);
					panel.add(endButton);
					try {
						Image img = ImageIO.read(getClass().getResourceAsStream("GameOver.jpeg"));
						endButton.setIcon(new ImageIcon(img));
					} catch (Exception ex) {
						System.out.println(ex);
					}
                   */
					// setContentPane(endPanel);

					revalidate();
					showGameOverScreen();
					
					/*

					// Restart Button
					JButton restartButton = new JButton();
					restartButton.setBackground(Color.black);
					restartButton.setBorderPainted(false);
					setLayout(null);
					restartButton.setBounds(100, 100, 500, 500);
					panel.add(restartButton);
					try {
						Image img = ImageIO.read(getClass().getResourceAsStream("play.png"));
						restartButton.setIcon(new ImageIcon(img));
					} catch (Exception ex) {
						System.out.println(ex);
					}

					restartButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							System.out.print("Im in action listener ");
							panel.reset();
							panel.removeAll();
							progress.stop();
							startGame();
							revalidate();

						}
					}); */

				}
			}
		});

		progress.start();

	}

	private void showGameOverScreen() {
		JButton endButton = new JButton();
		endButton.setBackground(Color.black);
		endButton.setBorderPainted(false);
		setLayout(null);
		endButton.setBounds(350, 100, 250, 100);
		panel.add(endButton);
		try {
			Image img = ImageIO.read(getClass().getResourceAsStream("GameOver.jpeg"));
			endButton.setIcon(new ImageIcon(img));
		} catch (Exception ex) {
			System.out.println(ex);
		}

		JButton restartButton = new JButton();
		restartButton.setBackground(Color.black);
		restartButton.setBorderPainted(false);
		setLayout(null);
		restartButton.setBounds(220, 200, 500, 500);
		panel.add(restartButton);
		try {
			Image img = ImageIO.read(getClass().getResourceAsStream("play.png"));
			restartButton.setIcon(new ImageIcon(img));
		} catch (Exception ex) {
			System.out.println(ex);
		}

		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.reset();
				panel.removeAll();
				startGame();
				revalidate();
			}
		});

		revalidate();
	}

	/**
	 * 
	 * public void startAnimation() { // We set done to false and allow the UI
	 * thread to change the value // to true when menu options are selected.
	 * Enviornment.done = false; try { while (!Enviornment.done) {
	 * panel.updateAnimation(); // This informs the UI Thread to repaint this
	 * component repaint(); // This causes our main thread to wait... to sleep...
	 * for a bit. Thread.sleep(Enviornment.delay); } } catch (InterruptedException
	 * e) { e.printStackTrace(); } }
	 **/
}
