package de.fuwa.bomberman.app.gui;

import de.fuwa.bomberman.game.utils.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class VisualGameField extends JPanel {

    private int sizeX;
    private int sizeY;

    private Image background;

    private ConcurrentLinkedQueue<DrawableObject> backgroundObjects, middleObjects, foregroundObjects;


    private ConcurrentLinkedQueue<DrawableObject> graphicalObjects = new ConcurrentLinkedQueue<>();

    public VisualGameField() {
        this(1, 1);
    }

    public VisualGameField(int sizeX, int sizeY) {
        this.backgroundObjects = new ConcurrentLinkedQueue<>();
        this.middleObjects = new ConcurrentLinkedQueue<>();
        this.foregroundObjects = new ConcurrentLinkedQueue<>();

        setSize(sizeX, sizeY);
        /*try {
            background = ImageIO.read(new File("assets/Textures/ground.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public void setBackground(DrawableObject object) {
        this.background = object.getImageToDraw();
    }

    public final void setSize(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        repaint();
    }

    public void addGameObject(DrawableObject object) {
        this.graphicalObjects.add(object);
        switch (object.getLayer()) {
            case Middle:
                middleObjects.add(object);
                break;
            case Background:
                backgroundObjects.add(object);
                break;
            case Foreground:
                foregroundObjects.add(object);
                break;
        }
    }

    public void removeGameObject(DrawableObject object) {
        this.graphicalObjects.remove(object);
        this.backgroundObjects.remove(object);
        this.middleObjects.remove(object);
        this.foregroundObjects.remove(object);
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
        if (background != null) {
            graphics2D.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }

        graphics2D.scale(scale, scale);

        drawImages(backgroundObjects, graphics2D);
        drawImages(middleObjects, graphics2D);
        drawImages(foregroundObjects, graphics2D);

        // we call this to remove flicker effects when screen is redrawn to often
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawImages(ConcurrentLinkedQueue<DrawableObject> drawables, Graphics2D graphics2D) {
        for (DrawableObject object : drawables) {
            int screenPosX = (int) (object.getX() * GameConstants.TILE_SIZE);
            int screenPosY = (int) (object.getY() * GameConstants.TILE_SIZE);
            graphics2D.drawImage(object.getImageToDraw(), screenPosX, screenPosY, this);
        }
    }
}