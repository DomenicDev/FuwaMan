package de.fuwa.bomberman.game.gui;

import de.fuwa.bomberman.app.gui.VisualGameField;
import de.fuwa.bomberman.es.EntityId;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class InGameGui extends JPanel {

    private JPanel playerInfoPanel;

    private VisualGameField visualGameField;
    private JLabel timerLabel;
    private JLabel statusLabel;

    private Map<EntityId, PlayerInfo> players = new HashMap<>();

    public InGameGui() {

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(1, 100));
        topPanel.setLayout(new GridLayout(1, 2));
        add(topPanel, BorderLayout.NORTH);

        JPanel timerPanel = new JPanel();
        timerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.timerLabel = new JLabel("TIMER: ");
        this.timerLabel.setFont(new Font("Broadway", Font.BOLD, 20));
        timerPanel.add(timerLabel);
        topPanel.add(timerPanel);

        this.playerInfoPanel = new JPanel();
        this.playerInfoPanel.setLayout(new GridLayout(1, 1));
        this.playerInfoPanel.setBackground(Color.BLACK);
        topPanel.add(playerInfoPanel);

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

    public void addPlayerInfo(EntityId playerId, String name) {
        if (this.players.containsKey(playerId)) {
            return;
        }

        PlayerInfo playerInfo = new PlayerInfo(name);
        playerInfoPanel.add(playerInfo);

        this.players.put(playerId, playerInfo);

        // create new layout for player info panel
        playerInfoPanel.setLayout(new GridLayout(1, this.players.size()));


    }

    public void refreshScoreForPlayer(EntityId playerId, int score) {
        PlayerInfo playerInfo = this.players.get(playerId);
        if (playerInfo != null) {
            playerInfo.setScore(score);
        }
    }

    public void removePlayerInfo(EntityId playerId) {

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
        this.timerLabel.setText("TIMER: " + minutes + ":" + secondsText);
    }

    public VisualGameField getVisualGameField() {
        return visualGameField;
    }


    private class PlayerInfo extends JPanel {

        private JLabel scoreLabel;
        private Font f = new Font("Helvita", Font.BOLD, 16);

        private PlayerInfo(String name) {
            JLabel nameLabel = new JLabel(name);
            nameLabel.setFont(f);
            scoreLabel = new JLabel("SCORE: ");
            scoreLabel.setFont(f);
            setLayout(new GridLayout(2, 1));
            add(nameLabel);
            add(scoreLabel);
        }

        private void setScore(int score) {
            this.scoreLabel.setText("SCORE: " + score);
        }

    }
}