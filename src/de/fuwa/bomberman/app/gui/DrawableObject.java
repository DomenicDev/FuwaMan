package de.fuwa.bomberman.app.gui;

import java.awt.*;

public interface DrawableObject {

    void setImageToDraw(Image image);

    Image getImageToDraw();

    void set(float x, float y);

    void setX(float x);

    void setY(float y);

    float getX();

    float getY();

    void update(float tpf);

}
