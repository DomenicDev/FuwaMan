package de.fuwa.bomberman.app.gui;

import java.awt.*;
import java.util.List;

public class AnimatedImageObject extends AbstractDrawableObject {

    private List<Image> frames;
    private boolean animated;
    private float timer;
    private float switchTime; // the time it needs to switch to the next image
    private int currentImageIndex;

    public AnimatedImageObject(int x, int y, List<Image> frames, boolean animated) {
        this.frames = frames;
        this.timer = 0;
        this.switchTime = 0.2f;
        this.currentImageIndex = 0;
        setX(x);
        setY(y);
        setAnimated(animated);
        selectImage(currentImageIndex);

        System.out.println("SIZE: " + frames.size());
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    private void selectImage(int index) {
        setImageToDraw(frames.get(currentImageIndex));
    }

    @Override
    public void update(float tpf) {
    //    System.out.println(currentImageIndex);
        if ((timer += tpf) >= switchTime) {
            // switch to next image

            timer = 0;

            // first we need to find the next index
            if (currentImageIndex >= frames.size()-1) {
                currentImageIndex = 0;
            } else {
                currentImageIndex++;
            }

            // now we set the new image to draw
            selectImage(currentImageIndex);
        }
    }
}

