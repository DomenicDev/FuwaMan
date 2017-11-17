package de.fuwa.bomberman.app.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VisualGameField extends JPanel {

    private int sizeX;
    private int sizeY;

    final int tileSizeX = 200;
    final int tileSizeY = 200;

    private List<DrawableObject> graphicalObjects = new ArrayList<>();

    public VisualGameField(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public void addGameObject(DrawableObject object) {
        this.graphicalObjects.add(object);
    }

    public void removeGameObject(DrawableObject object) {
        this.graphicalObjects.add(object);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics2D = (Graphics2D) g;

        float scaleX = (float) getWidth() / (tileSizeX * sizeX);
        float scaleY = (float) getHeight() / (tileSizeY * sizeY);
        float scale = Math.min(scaleX, scaleY);

        graphics2D.scale(scale, scale);

        for (DrawableObject object : graphicalObjects) {
            int screenPosX = (int) (object.getX() * tileSizeX);
            int screenPosY = (int) (object.getY() * tileSizeY);

            graphics2D.drawImage(object.getImageToDraw(), screenPosX, screenPosY, this);
        }

        // we call this to remove flicker effects when screen is redrawn to often
        Toolkit.getDefaultToolkit().sync();
    }
}