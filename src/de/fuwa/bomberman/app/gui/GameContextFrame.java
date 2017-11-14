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


        VisualGameField gameField = new VisualGameField(9,9);
        add(gameField);

        pack();

        ImageIcon block = null;
        //    block = ImageIO.read(new File("assets/Textures/player.gif"));
        block = new ImageIcon("assets/Textures/player.gif");


        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                GraphicalObject object = new GraphicalObject(x, y, block.getImage());
                gameField.addGameObject(object);
            }
        }

        setVisible(true);
    }

    private class VisualGameField extends JPanel {

        private int sizeX;
        private int sizeY;

        final int tileSizeX = 17;
        final int tileSizeY = 24;

        private List<GraphicalObject> graphicalObjects = new ArrayList<>();

        VisualGameField(int sizeX, int sizeY) {
            this.sizeX = sizeX;
            this.sizeY = sizeY;
        }

        private void addGameObject(GraphicalObject object) {
            this.graphicalObjects.add(object);
        }



        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D graphics2D = (Graphics2D) g;

            System.out.println(getWidth());

            float scaleX = (float) getWidth() / (tileSizeX * sizeX);
            float scaleY = (float) getHeight() / (tileSizeY * sizeY);

            System.out.println(scaleX);

            float scale = Math.min(scaleX, scaleY);
            graphics2D.scale(scale, scale);

            for (GraphicalObject object : graphicalObjects) {

                int screenPosX = (int) (object.getPosX() * tileSizeX);
                int screenPosY = (int) (object.getPosY() * tileSizeY);



                graphics2D.drawImage(object.getImage(), screenPosX, screenPosY, this);

            }

        }
    }

    @Override
    public void dispose() {
        super.dispose();
        this.listener.onExit();
    }
}
