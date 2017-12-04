package de.fuwa.bomberman.game.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This panels draws the image specified in the constructor
 * over the area of the panel.
 */
public class BackgroundImagePanel extends JPanel {

    private BufferedImage image;
    private boolean lockAspectRatio;
    private boolean centerImage = true;

    public BackgroundImagePanel(BufferedImage image) {
        this.image = image;
    }

    public BackgroundImagePanel(BufferedImage image, boolean lockAspectRatio) {
        this.image = image;
        this.lockAspectRatio = lockAspectRatio;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        if (lockAspectRatio) {
            float scaleX = (float) getWidth() / image.getWidth();
            float scaleY = (float) getHeight() / image.getHeight();
            float scale = Math.min(scaleX, scaleY);
            graphics2D.scale(scale, scale);
        }

        if (centerImage) {
            int x = (getWidth() / 2) - (image.getWidth() / 2);
            int y = (getHeight() / 2) - (image.getHeight() / 2);
            graphics2D.drawImage(image, x, y, image.getWidth(), image.getHeight(), this);
        } else {
            graphics2D.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
        }
    }
}
