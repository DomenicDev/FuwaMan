package de.fuwa.bomberman.game.gui;

import de.fuwa.bomberman.app.gui.VisualGameField;

import javax.swing.*;
import java.awt.*;

public class InGameGui extends JPanel {

    private VisualGameField visualGameField;
    private JLabel timerPanel;
    private JLabel statusLabel;

    public InGameGui() {

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(1, 100));
        add(topPanel, BorderLayout.NORTH);

        this.timerPanel = new JLabel("TIMER: ");
        this.timerPanel.setFont(new Font("Broadway", Font.BOLD, 20));
        topPanel.add(timerPanel);

        this.visualGameField = new VisualGameField();
        add(visualGameField, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(1, 75));
        bottomPanel.setLayout(new GridLayout(1, 2));
        add(bottomPanel, BorderLayout.SOUTH);

        this.statusLabel = new JLabel("STATUS: ");
        this.statusLabel.setFont(new Font("Broadway", Font.BOLD, 20));
        bottomPanel.add(statusLabel);

        JButton exitGame = new JButton("Exit Game");
        exitGame.addActionListener(e -> BasicFuwaManPanel.listener.onClickCloseGame());
        bottomPanel.add(exitGame);
    }

    public void setStatusText(String statusText) {
        this.statusLabel.setText("STATUS:   " + statusText);
    }

    public void setRemainingTime(float remainingTime) {
        int minutes = (int) (remainingTime) / 60;
        int seconds = (int) (remainingTime) % 60;
        String secondsText = "";
        if (seconds >= 10) {
            secondsText += seconds;
        } else {
            secondsText += "0" + seconds;
        }
        this.timerPanel.setText("TIMER: " + minutes + ":" + secondsText);
    }

    public VisualGameField getVisualGameField() {
        return visualGameField;
    }

}