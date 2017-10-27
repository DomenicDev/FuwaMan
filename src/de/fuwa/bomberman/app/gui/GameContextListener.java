package de.fuwa.bomberman.app.gui;

/**
 * A collection of some important methods regarding frame control.
 */
public interface GameContextListener {

    /**
     * Called when the user clicks on the "X" symbol in the right top corner to close/stop the game.
     */
    void onExit();

}
