package de.fuwa.bomberman.app.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class VisualGameField extends JPanel {

    private int sizeX;
    private int sizeY;

    private BufferedImage background;


    private ConcurrentLinkedQueue<DrawableObject> graphicalObjects = new ConcurrentLinkedQueue<>();

    public VisualGameField(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        try {
            background = ImageIO.read(new File("assets/Textures/ground.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addGameObject(DrawableObject object) {
        this.graphicalObjects.add(object);
    }

    public void removeGameObject(DrawableObject object) {
        this.graphicalObjects.remove(object);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics2D = (Graphics2D) g;

        int tileSizeX = 200;
        int tileSizeY = 200;

        float scaleX = (float) getWidth() / (tileSizeX * sizeX);
        float scaleY = (float) getHeight() / (tileSizeY * sizeY);
        float scale = Math.min(scaleX, scaleY);

        // draw background first
        graphics2D.drawImage(background, 0, 0, (int) (getWidth()), (int) (getHeight()), this);

        graphics2D.scale(scale, scale);

        int centerOffsetX = (getWidth() - (tileSizeX * sizeX)) / 2;
        int centerOffsetY = (getHeight() - (tileSizeY * sizeY)) / 2;




        for (DrawableObject object : graphicalObjects) {
            int screenPosX = (int) (object.getX() * tileSizeX);
            int screenPosY = (int) (object.getY() * tileSizeY);

            graphics2D.drawImage(object.getImageToDraw(), screenPosX, screenPosY, this);
        }

        // we call this to remove flicker effects when screen is redrawn to often
        Toolkit.getDefaultToolkit().sync();
    }
}