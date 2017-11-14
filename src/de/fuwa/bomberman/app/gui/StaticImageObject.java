package de.fuwa.bomberman.app.gui;

import java.awt.*;

public class StaticImageObject extends AbstractDrawableObject {

    public StaticImageObject(float x, float y, Image staticImage) {
        setX(x);
        setY(y);
        setImageToDraw(staticImage);
    }
}
