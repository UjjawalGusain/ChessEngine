package board;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class JPanelWithBackground extends JPanel {
  private static final long serialVersionUID = 1L;
  private Image backgroundImage;

  public JPanelWithBackground(String fileName, int width, int height) throws IOException {
    super.setBounds(700, 0, width, height);
    this.setLayout(new GridLayout(8,8,0,0));
    backgroundImage = ImageIO.read(new File(fileName));
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(backgroundImage, 0, 0, this);
  }
}
