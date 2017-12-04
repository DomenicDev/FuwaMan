package de.fuwa.bomberman.tests;

import de.fuwa.bomberman.game.gui.SingleplayerMenu;

import javax.swing.*;
import java.awt.*;

public class TestGui {

    public TestGui() {

        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(1, 1));
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //    frame.add(new MainMenu());
        frame.add(new SingleplayerMenu());
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new TestGui();
    }

}
