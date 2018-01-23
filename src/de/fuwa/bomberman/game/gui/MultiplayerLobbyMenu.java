package de.fuwa.bomberman.game.gui;

import de.fuwa.bomberman.game.enums.Setting;
import de.fuwa.bomberman.game.utils.GameOptions;
import de.fuwa.bomberman.game.utils.GameUtils;

import javax.swing.*;
import java.awt.*;

public class MultiplayerLobbyMenu extends BasicFuwaManPanel {

    private DefaultListModel<String> playerListModel;

    @Override
    protected void addComponents() {
        JPanel contentPanel = new TransparentPanel();
        GridLayout l = new GridLayout(5, 2);
        l.setVgap(50);
        l.setHgap(25);
        contentPanel.setLayout(l);
        this.centerPanel.setLayout(new GridLayout(1, 3)); // we only fill the center element later
        this.centerPanel.add(new TransparentPanel());
        this.centerPanel.add(contentPanel);
        this.centerPanel.add(new TransparentPanel());

        // 1. row
        contentPanel.add(new TransparentPanel());
        contentPanel.add(new TransparentPanel());

        // 2. row
        contentPanel.add(GuiUtils.createLabel("Connected Players"));
        this.playerListModel = new DefaultListModel<>();
        JList<String> connectedPlayers = new JList<>(playerListModel);
        contentPanel.add(connectedPlayers);

        // 3. row
        JLabel settingsLabel = GuiUtils.createLabel("Choose Setting");
        contentPanel.add(settingsLabel);

        JComboBox<Setting> settingsBox = new JComboBox<>();
        for (Setting s : Setting.values()) {
            settingsBox.addItem(s);
        }
        contentPanel.add(settingsBox);

        // 4. row
        JButton startButton = GuiUtils.createButton("Start Game");
        startButton.addActionListener(e -> listener.onClickStartGame(new GameOptions(settingsBox.getItemAt(settingsBox.getSelectedIndex()), GameUtils.createComplexGameField(), 0)));
        contentPanel.add(startButton);

        JButton returnButton = GuiUtils.createButton("Return");
        returnButton.addActionListener(e -> listener.onClickReturnToMainMenu());
        contentPanel.add(returnButton);

        // 5. row
        contentPanel.add(new TransparentPanel());
        contentPanel.add(new TransparentPanel());
    }

    public void clearListModel() {
        playerListModel.removeAllElements();
    }

    public void addPlayerName(String playerName) {
        playerListModel.addElement(playerName);
    }

    public void removePlayerName(String playerName) {
        playerListModel.removeElement(playerName);
    }
}
