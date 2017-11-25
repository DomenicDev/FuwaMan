package de.fuwa.bomberman.app.gui;

import java.awt.*;

/**
 * Used as visual representation for static entities
 */
public class StaticImageObject extends AbstractDrawableObject {

    /**
     * Creates a single static image.
     * @param staticImage the single image to set
     */
    public StaticImageObject(Image staticImage) {
        setImageToDraw(staticImage);
    }
}
