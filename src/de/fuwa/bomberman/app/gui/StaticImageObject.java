package de.fuwa.bomberman.app.gui;

import java.awt.*;

/**
 * Used as visual representation for static entities
 */
public class StaticImageObject extends AbstractDrawableObject {

    /**
     * Creates a StaticImageObject with the specified parameters.
     * @param x logical x-position
     * @param y logical y-position
     * @param staticImage the image to draw
     */
    public StaticImageObject(float x, float y, Image staticImage) {
        setX(x);
        setY(y);
        setImageToDraw(staticImage);
    }
}
