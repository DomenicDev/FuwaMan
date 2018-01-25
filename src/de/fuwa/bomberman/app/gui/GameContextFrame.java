package de.fuwa.bomberman.app.gui;

import de.fuwa.bomberman.app.AppSettings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * We created our own JFrame subclass to extend JFrame
 * and to make it fit our needs.
 * This class will provide methods to manipulate the visual
 * representation of the game.
 */
public class GameContextFrame extends JFrame {

    private AppSettings settings;
    private GameContextListener listener;

    private JPanel mainContentPanel;
    private JPanel gamePanel; // this panel is added if we are in the running game
    private JPanel mainMenuPanel; // this panel is added if we are in the main menu

    private final Dimension DEFAULT_WINDOW_RESOLUTION = new Dimension(1200, 720);

    public GameContextFrame(GameContextListener listener) {
        this.listener = listener;
    }

    public void setAppSettings(AppSettings settings) {
        this.settings = settings;
    }

    public void buildContext() {
        setTitle(settings.getTitle());
        setSize(settings.getWidth(), settings.getHeight());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null); // to place it in the middle of the screen
        setMinimumSize(DEFAULT_WINDOW_RESOLUTION);
        setResizable(true);
        setFocusable(true);
        setIcon(settings.getImageIconPath());
        setPreferredSize(new Dimension(settings.getWidth(), settings.getHeight()));
        setLayout(new GridLayout(1, 1));

        this.mainContentPanel = new JPanel();
        this.mainContentPanel.setLayout(new GridLayout(1, 1));
        add(mainContentPanel);

        this.gamePanel = new JPanel();
        this.gamePanel.setLayout(new GridLayout(1, 1));
        this.mainContentPanel.add(gamePanel);

        setUndecorated(true);
        setExtendedState(MAXIMIZED_BOTH);

        setVisible(true);
    }

    private void setIcon(String imagePath) {
        if (imagePath == null) return;
        try {
            this.setIconImage(ImageIO.read(new File(imagePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFullscreen(boolean fullscreen) {
        if (fullscreen) {
            super.dispose();
            setUndecorated(true);
            setExtendedState(MAXIMIZED_BOTH);
            setVisible(true);
        } else {
            super.dispose();
            setUndecorated(false);
            setExtendedState(NORMAL);
            setSize(DEFAULT_WINDOW_RESOLUTION);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }

    public void updateGui() {
        validate();
        repaint();
    }

    public void setScreen(JPanel screen) {
        clearContent();
        gamePanel.add(screen);
        gamePanel.setFocusable(true);
    }

    private void clearContent() {
        this.gamePanel.removeAll();
    }

    @Override
    public void dispose() {
        super.dispose();
        this.listener.onExit();
    }
}
