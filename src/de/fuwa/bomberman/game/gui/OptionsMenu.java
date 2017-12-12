package de.fuwa.bomberman.game.gui;

import javax.swing.*;
import java.awt.*;

public class OptionsMenu extends BasicFuwaManPanel {


    @Override
    protected void addComponents() {
        JPanel buttonPanel = new TransparentPanel();
        GridLayout l = new GridLayout(6, 1);
        l.setVgap(25);
        buttonPanel.setLayout(l);
        this.centerPanel.setLayout(new GridLayout(1, 2)); // we only fill the center element later
        this.centerPanel.add(new TransparentPanel());
        this.centerPanel.add(buttonPanel);
        this.centerPanel.add(new TransparentPanel());

        buttonPanel.add(new TransparentPanel()); // empty space

        JLabel text = new JLabel("Volume");
        buttonPanel.add(text);

        //Boxgridlayer hinzufügen für groesseren layer
        JButton volumeUp = new JButton("+");
        volumeUp.addActionListener(e->listener.onClickVolumeUp());
        buttonPanel.add(volumeUp);

        JButton volumeDown = new JButton("-");
        volumeDown.addActionListener(e-> listener.onClickVolumeDown());
        buttonPanel.add(volumeDown);

        JButton fullscreen = new JButton("Fullscreen");
        fullscreen.addActionListener(e-> listener.onClickFullscreen());
        buttonPanel.add(fullscreen);

        JButton window = new JButton("Window");
        window.addActionListener(e->listener.onClickWindow());
        buttonPanel.add(window);

        JButton saveChanges = new JButton("Save Changes");
        saveChanges.addActionListener(e->listener.onClickSaveChanges());
        buttonPanel.add(saveChanges);

        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> listener.onClickReturnToMainMenu());
        buttonPanel.add(returnButton);
    }

}
