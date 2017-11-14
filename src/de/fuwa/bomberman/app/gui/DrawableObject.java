package de.fuwa.bomberman.app.gui;

import java.awt.*;

/**
 * This interface is used by {@link VisualGameField} to draw the game objects onto the screen.
 */
public interface DrawableObject {

    /**
     * Sets the image which shall be drawn onto the screen.
     * @param image the image to draw onto the screen
     */
    void setImageToDraw(Image image);

    /**
     * @return the image which is currently set to be drawn on the screen
     */
    Image getImageToDraw();

    /**
     * Sets the logical x- and y-position of that game object
     * @param x the logical x position
     * @param y the logical y position
     */
    void set(float x, float y);

    /**
     * Sets the logical x position
     * @param x the logical x-position to set
     */
    void setX(float x);

    /**
     * Sets the logical y-position
     * @param y the logical y-position to set
     */
    void setY(float y);

    /**
     * @return the logical x-position
     */
    float getX();

    /**
     * @return the logical y-position
     */
    float getY();

}
