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
    private static BufferedImage[] snowFrames;
    private static int snowFrameCounter;
    private static BufferedImage titleImage;
    protected static GameMenuListener listener;
    private static long lastTime = System.nanoTime();

    static {
        try {
            backgroundImage = ImageIO.read(new File(PATH + "Background_menu.gif"));
            titleImage = ImageIO.read(new File(PATH + "title.png"));

            // load snow
            BufferedImage snowSpriteSheet = ImageIO.read(new File(PATH + "snow_sprite.png"));
            snowFrames = new BufferedImage[4];
            snowFrames[0] = snowSpriteSheet.getSubimage(0, 0, 800, 800);
            snowFrames[1] = snowSpriteSheet.getSubimage(800, 0, 800, 800);
            snowFrames[2] = snowSpriteSheet.getSubimage(0, 800, 800, 800);
            snowFrames[3] = snowSpriteSheet.getSubimage(800, 800, 800, 800);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected JPanel centerPanel; // used for buttons
    private JPanel topPanel; // used for the title

    public BasicFuwaManPanel() {
        // create layout
        setLayout(new BorderLayout());

        this.topPanel = new JPanel();
        this.topPanel.setLayout(new GridLayout(1, 1));
        this.topPanel.setPreferredSize(new Dimension(800, 300));
        this.topPanel.setOpaque(false);
        this.topPanel.setBackground(Color.CYAN);
        add(topPanel, BorderLayout.NORTH);

        // add title
        JPanel titlePanel = new BackgroundImagePanel(titleImage, true);
        titlePanel.setPreferredSize(new Dimension());
        titlePanel.setOpaque(false);
        topPanel.add(titlePanel);

        this.centerPanel = new TransparentPanel();
        add(centerPanel, BorderLayout.CENTER);

        // add components
        addComponents();
    }

    public static void setListener(GameMenuListener listener) {
        BasicFuwaManPanel.listener = listener;
    }

    protected void addComponents() {
        // insert your gui code here (in subclasses)
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        long now = System.nanoTime();
        if ((float) (now - lastTime) / 1000000000L > 0.4f) {
            lastTime = now;
            if (snowFrameCounter < 3) {
                snowFrameCounter++;
            } else {
                snowFrameCounter = 0;
            }
        }
        g.drawImage(snowFrames[snowFrameCounter], 0, 0, getWidth(), getHeight(), this);
    }
}
