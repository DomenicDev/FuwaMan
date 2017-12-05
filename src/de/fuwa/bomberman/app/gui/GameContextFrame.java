package de.fuwa.bomberman.app.gui;

import de.fuwa.bomberman.app.AppSettings;

import javax.swing.*;
import java.awt.*;

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

    private VisualGameField visualGameField; // the current game field

    public GameContextFrame(GameContextListener listener) {
        this.listener = listener;
    }

    public void setAppSettings(AppSettings settings) {
        this.settings = settings;
    }

    public void buildContext() {
        setSize(settings.getWidth(), settings.getHeight());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null); // to place it in the middle of the screen
        setMinimumSize(new Dimension(600, 600));
        setResizable(true);
        setLayout(new GridLayout(1,1));

        this.mainContentPanel = new JPanel();
        this.mainContentPanel.setLayout(new GridLayout(1,1));
        add(mainContentPanel);

        this.gamePanel = new JPanel();
        this.gamePanel.setLayout(new GridLayout(1,1));
        this.mainContentPanel.add(gamePanel);

        pack();

        setFocusable(true);

        setVisible(true);
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

    public VisualGameField createAndDisplayGameField(int sizeX, int sizeY) {
        VisualGameField visualGameField = new VisualGameField(sizeX, sizeY);
        visualGameField.setBackground(Color.CYAN);
        clearContent();
        this.gamePanel.add(visualGameField);
        this.visualGameField = visualGameField;
        return visualGameField;
    }

    public void removeCurrentGameField() {
        if (this.visualGameField != null) {
            this.gamePanel.remove(visualGameField);
            this.visualGameField = null;
        }
    }


    @Override
    public void dispose() {
        super.dispose();
        this.listener.onExit();
    }
}
