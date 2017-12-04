package de.fuwa.bomberman.game.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BasicFuwaManPanel extends JPanel {

    private static final String PATH = "assets/Textures/";
    private static BufferedImage backgroundImage;
    private static BufferedImage titleImage;

    static {
        try {
            backgroundImage = ImageIO.read(new File(PATH + "MENU_001.png"));
            titleImage = ImageIO.read(new File(PATH + "title.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected GameMenuListener listener;
    protected JPanel centerPanel; // used for buttons
    private JPanel topPanel; // used for the title

    public BasicFuwaManPanel() {
        // create layout
        setLayout(new BorderLayout());

        this.topPanel = new JPanel();
        this.topPanel.setLayout(new GridLayout(1, 1));
        this.topPanel.setPreferredSize(new Dimension(800, 200));
        this.topPanel.setOpaque(false);
        this.topPanel.setBackground(Color.CYAN);
        add(topPanel, BorderLayout.NORTH);

        // add title
        JPanel titlePanel = new BackgroundImagePanel(titleImage, false);
        titlePanel.setOpaque(false);
        topPanel.add(titlePanel);

        this.centerPanel = new TransparentPanel();
        this.centerPanel.setLayout(new GridLayout(1, 3)); // we only fill the center element later
        add(centerPanel, BorderLayout.CENTER);

        // add components
        addComponents();
    }

    public void setListener(GameMenuListener listener) {
        this.listener = listener;
    }

    protected void addComponents() {
        // insert your gui code here (in subclasses)
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
