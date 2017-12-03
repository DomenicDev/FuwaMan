package de.fuwa.bomberman.game.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel {

    private BufferedImage leftImage;
    private BufferedImage mainImage;
    private BufferedImage rightImage;

    private JPanel mainPanel, leftPanel, rightPanel;

    public MainMenu() {
        // first we want to load the images
        loadImages();

        // after that we create our panels
        createPanels();

        // create layout
        setLayout(new BorderLayout());

        // add components
        addComponents();
    }

    private void loadImages() {
        String path = "assets/Textures/";
        try {
            this.leftImage = ImageIO.read(new File(path + "Menu_left_001.png"));
            this.mainImage = ImageIO.read(new File(path + "MENU_001.png"));
            this.rightImage = ImageIO.read(new File(path + "Menu_right_001.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createPanels() {
        this.mainPanel = new MainMenuPanel(mainImage);
        this.leftPanel = new MainMenuPanel(leftImage);
        this.rightPanel = new MainMenuPanel(rightImage);
    }

    private void addComponents() {
        add(mainPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.WEST);
        add(leftPanel, BorderLayout.EAST);
    }

    private class MainMenuPanel extends JPanel {

        private BufferedImage image;

        private MainMenuPanel(BufferedImage image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }
}
