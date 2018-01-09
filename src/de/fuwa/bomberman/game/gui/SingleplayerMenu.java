package de.fuwa.bomberman.game.gui;

import de.fuwa.bomberman.game.enums.Setting;
import de.fuwa.bomberman.game.utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SingleplayerMenu extends BasicFuwaManPanel {

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

        // empty panel

        // setting
        JLabel settingsLabel = new JLabel("Choose Setting");
        contentPanel.add(settingsLabel);

        JComboBox<Setting> settingsBox = new JComboBox<>();
        for (Setting s : Setting.values()) {
            settingsBox.addItem(s);
        }
        contentPanel.add(settingsBox);

        JLabel mapLabel = new JLabel("Choose Map");
        contentPanel.add(mapLabel);
        JComboBox<NamedGameField> mapBox = new JComboBox<>();
        // add predefined maps
        mapBox.addItem(new NamedGameField("Small", GameUtils.createComplexGameField(GameConstants.MIN_GAME_FIELD_SIZE, GameConstants.MIN_GAME_FIELD_SIZE)));
        mapBox.addItem(new NamedGameField("Medium", GameUtils.createComplexGameField((GameConstants.MIN_GAME_FIELD_SIZE + GameConstants.MAX_GAME_FIELD_SIZE) / 2, (GameConstants.MIN_GAME_FIELD_SIZE + GameConstants.MAX_GAME_FIELD_SIZE) / 2)));
        mapBox.addItem(new NamedGameField("Large", GameUtils.createComplexGameField(GameConstants.MAX_GAME_FIELD_SIZE, GameConstants.MAX_GAME_FIELD_SIZE)));
        // add custom maps
        List<NamedGameField> customMaps = ExternalDataManager.readAllCustomMaps();
        if (customMaps != null) {
            for (NamedGameField customMap : customMaps) {
                mapBox.addItem(customMap);
            }
        }

        mapBox.setSelectedIndex(0);
        contentPanel.add(mapBox);

        // number of enemies
        JLabel numberEnemiesLabel = new JLabel("Select Number of Enemies");
        contentPanel.add(numberEnemiesLabel);

        JComboBox<Integer> numberOfEnemiesBox = new JComboBox<>();
        for (int i = 1; i <= 3; i++) {
            numberOfEnemiesBox.addItem(i);
        }
        contentPanel.add(numberOfEnemiesBox);


        // start and return button
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> listener.onClickStartGame(new GameOptions(settingsBox.getItemAt(settingsBox.getSelectedIndex()), mapBox.getItemAt(mapBox.getSelectedIndex()).getGameField(), numberOfEnemiesBox.getItemAt(numberOfEnemiesBox.getSelectedIndex()))));
        contentPanel.add(startButton);

        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> listener.onClickReturnToMainMenu());
        contentPanel.add(returnButton);


        // empty panel
        contentPanel.add(new TransparentPanel());
        contentPanel.add(new TransparentPanel());
    }
}
