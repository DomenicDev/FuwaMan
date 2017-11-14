package de.fuwa.bomberman.app.gui;

import java.awt.*;

public class GraphicalObject {

    private float posX; // logical x-position of this game object
    private float posY; // logical y-position of this game object
    private Image image;

    public GraphicalObject(float posX, float posY, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.image = image;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
