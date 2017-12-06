package de.fuwa.bomberman.game.gui;

import de.fuwa.bomberman.app.gui.VisualGameField;

import javax.swing.*;
import java.awt.*;

public class InGameGui extends JPanel {

    private VisualGameField visualGameField;

    public InGameGui(int sizeX, int sizeY) {

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(1, 100));
        add(topPanel, BorderLayout.NORTH);

        this.visualGameField = new VisualGameField(sizeX, sizeY);
        add(visualGameField, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(1, 75));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        add(bottomPanel, BorderLayout.SOUTH);

        JButton exitGame = new JButton("Exit Game");
        exitGame.addActionListener(e -> BasicFuwaManPanel.listener.onClickCloseGame());
        bottomPanel.add(exitGame);
    }

    public VisualGameField getVisualGameField() {
        return visualGameField;
    }
}