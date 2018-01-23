package de.fuwa.bomberman.game.gui.components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FuwaButton extends JButton implements MouseListener{

    private static BufferedImage buttonBackgroundNormal;
    private static BufferedImage buttonBackgroundPressed;

    static {
        try {
            buttonBackgroundNormal = ImageIO.read(new File("assets/Textures/btn_normal.png"));
            buttonBackgroundPressed = ImageIO.read(new File("assets/Textures/btn_press.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static final Color TRANSPARENT_COLOR = new Color(0,0,0,1);
    private static final Font TEXT_FONT = new Font("joystix monospace", Font.BOLD, 16);


    private BufferedImage currentBackgroundImage;

    public FuwaButton(String title) {
        super(title);
        this.setFont(TEXT_FONT);
        this.setForeground(Color.BLACK);
        this.addMouseListener(this);
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setBackground(TRANSPARENT_COLOR); // for transparent background
        this.setBorder(BorderFactory.createEmptyBorder());
        this.currentBackgroundImage = buttonBackgroundNormal;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(currentBackgroundImage, 0, 0, getWidth(), getHeight(), this);
        super.paintComponent(g);


    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.currentBackgroundImage = buttonBackgroundNormal;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.currentBackgroundImage = buttonBackgroundPressed;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.currentBackgroundImage = buttonBackgroundNormal;
    }
}
