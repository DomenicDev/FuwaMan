package de.fuwa.bomberman.app.gui;

import de.fuwa.bomberman.app.AppSettings;

import javax.swing.*;

/**
 * We created our own JFrame subclass to extend JFrame
 * and to make it fit our needs.
 * This class will provide methods to manipulate the visual
 * representation of the game.
 */
public class GameContextFrame extends JFrame {

    private AppSettings settings;

    private GameContextListener listener;

    public GameContextFrame(GameContextListener listener) {
        this.listener = listener;
    }

    public void setAppSettings(AppSettings settings) {
        this.settings = settings;
    }

    public void buildContext() {
        setSize(settings.getWidth(), settings.getHeight());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null); // to place it in the middle of the screen
        setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.listener.onExit();
    }
}
