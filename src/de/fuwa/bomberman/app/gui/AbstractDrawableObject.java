package de.fuwa.bomberman.app.gui;

import java.awt.*;

public class AbstractDrawableObject implements DrawableObject {

    private float x, y; // the logical x and y position
    private Image imageToDraw;

    @Override
    public final void setImageToDraw(Image image) {
        this.imageToDraw = image;
    }

    @Override
    public Image getImageToDraw() {
        return this.imageToDraw;
    }

    @Override
    public void set(float x, float y) {
        setX(x);
        setY(y);
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void update(float tpf) {

    }
}
