package board;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class JPanelWithBackground extends JPanel {
    private static final long serialVersionUID = 1L;
    private final Image backgroundImage;

    public JPanelWithBackground(BufferedImage backgroundImage, int width, int height) {
        this.backgroundImage = backgroundImage;
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new GridLayout(8, 8, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
