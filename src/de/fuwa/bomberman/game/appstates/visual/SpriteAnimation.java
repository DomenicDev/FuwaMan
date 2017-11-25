package de.fuwa.bomberman.game.appstates.visual;

import java.awt.image.BufferedImage;

public class SpriteAnimation {

    private static final float SWITCH_TIME = 0.2f;

    private boolean running;
    private float speed;
    private int currentFrameNumber;
    private float timer;
    private BufferedImage[] frames;

    public SpriteAnimation(BufferedImage[] frames) {
        this.frames = frames;
        this.currentFrameNumber = 0;
        this.speed = 1;
        this.timer = 0;
        this.running = false;
    }

    public void update(float tpf) {
        if (!running) {
            return;
        }
        timer += (tpf * speed);

        if (timer >= SWITCH_TIME) {
            if (currentFrameNumber < frames.length - 1) {
                currentFrameNumber++;
            } else {
                currentFrameNumber = 0;
            }
            timer = 0;
        }

    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public BufferedImage getCurrentFrame() {
        return frames[currentFrameNumber];
    }
}
