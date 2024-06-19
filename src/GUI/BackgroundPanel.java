
package GUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.util.ArrayList;

public class BackgroundPanel extends JPanel implements ActionListener{

	private JButton startBtn = new JButton();
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage img = null;
	       try{
	         img = ImageIO.read(new File("/Caveman-Joyride/src/Images/pixel-art-game-background-underground-cave-vector-49391631.jpg"));
	       }catch(IOException e){
	         e.printStackTrace();
	       }

	      g.drawImage(img, 0, 0, null);
	      createComponents();
	}

	public void createComponents() {
		this.setBackground(Color.gray);
		setLayout(null);
		startBtn.setBounds(750, 450, 400, 100);
		this.add(startBtn);
	}
}
