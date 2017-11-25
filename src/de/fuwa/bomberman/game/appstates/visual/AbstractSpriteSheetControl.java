package de.fuwa.bomberman.game.appstates.visual;

import de.fuwa.bomberman.app.gui.SpriteSheetImageObject;

public abstract class AbstractSpriteSheetControl {

    protected SpriteSheetImageObject spriteSheet;
    private boolean running;

    /**
     * Called from {@link VisualAppState}.
     *
     * @param spriteSheet
     */
    protected void setupControl(SpriteSheetImageObject spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * Tells the control to stop / resume an animation
     *
     * @param running
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Do not call manually.
     * Callback from {@link VisualAppState}.
     * <p>
     * Insert your update code in here.
     *
     * @param tpf time per frame
     */
    public abstract void update(float tpf);

}
