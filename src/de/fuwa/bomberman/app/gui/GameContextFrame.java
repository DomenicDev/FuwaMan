package de.fuwa.bomberman.app.gui;

import de.fuwa.bomberman.app.AppSettings;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

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
        setMinimumSize(new Dimension(800, 600));
        setLayout(new GridLayout(1,1));

        this.mainContentPanel = new JPanel();
        this.mainContentPanel.setLayout(new GridLayout(1,1));
        add(mainContentPanel);

        this.gamePanel = new JPanel();
        this.gamePanel.setLayout(new GridLayout(1,1));
        this.mainContentPanel.add(gamePanel);

   //     VisualGameField gameField = new VisualGameField(9,9);
   //     mainContentPanel.add(gameField);

        pack();
//
//        ImageIcon block = null;
//        //    block = ImageIO.read(new File("assets/Textures/player.gif"));
//        block = new ImageIcon("assets/Textures/player.gif");
//
//
//        for (int y = 0; y < 9; y++) {
//            for (int x = 0; x < 9; x++) {
//                GraphicalObject object = new GraphicalObject(x, y, block.getImage());
//                gameField.addGameObject(object);
//            }
//        }

        setVisible(true);
    }

    public void updateGui() {
        validate();
        repaint();
    }

    public VisualGameField createAndDisplayGameField(int sizeX, int sizeY) {
        VisualGameField visualGameField = new VisualGameField(sizeX, sizeY);
        visualGameField.setBackground(Color.CYAN);
        this.gamePanel.add(visualGameField);
        this.visualGameField = visualGameField;
        return visualGameField;
    }


    @Override
    public void dispose() {
        super.dispose();
        this.listener.onExit();
    }
}
