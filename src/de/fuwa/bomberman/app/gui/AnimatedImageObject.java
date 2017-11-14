package de.fuwa.bomberman.app.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class AnimatedImageObject extends AbstractDrawableObject {

    private Image staticImage;
    private Image animatedImage;
    private boolean animated;

    /**
     * Creates a new AnimatedImageObject with the specified parameters.
     * @param x the logical x-position
     * @param y the logical y-position
     * @param images the static at [0] and the animated image at [1]
     * @param animated true if gif shall be played, otherwise set it to false
     */
    public AnimatedImageObject(float x, float y, Image[] images, boolean animated) {
        this(x, y, images[0], images[1], animated);
    }

    /**
     * Creates a new AnimatedImageObject with the specified parameters.
     * @param x the logical x-position
     * @param y the logical y-position
     * @param staticImage the static image (usually the first frame of the gif)
     * @param animatedImage the animated gif
     * @param animated true if gif shall be played, otherwise set it to false
     */
    public AnimatedImageObject(float x, float y, Image staticImage, Image animatedImage, boolean animated) {
        set(x, y);
        setImages(staticImage, animatedImage);
        setAnimated(animated);
    }

    /**
     * Set the mode to animated or not animated.
     * @param animated true if you want to play the gif
     */
    public void setAnimated(boolean animated) {
        this.animated = animated;
        if (animated) {
            setImageToDraw(animatedImage);
        } else {
            setImageToDraw(staticImage);
        }
    }

    /**
     * Sets the specified images for this drawable object.
     * @param staticImage the static image
     * @param animatedImage the animated image
     */
    public void setImages(Image staticImage, Image animatedImage) {
        this.staticImage = staticImage;
        this.animatedImage = animatedImage;
    }

}

