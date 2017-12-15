package de.fuwa.bomberman.game.gui;

import de.fuwa.bomberman.game.appstates.sound.SoundVolumeAppState;

import javax.swing.*;
import java.awt.*;

public class OptionsMenu extends BasicFuwaManPanel {


    @Override
    protected void addComponents() {
        JPanel buttonPanel = new TransparentPanel();
        GridLayout l = new GridLayout(7, 1);
        l.setVgap(25);
        buttonPanel.setLayout(l);
        this.centerPanel.setLayout(new GridLayout(1, 2)); // we only fill the center element later
        this.centerPanel.add(new TransparentPanel());
        this.centerPanel.add(buttonPanel);
        this.centerPanel.add(new TransparentPanel());

        buttonPanel.add(new TransparentPanel()); // empty space

        //Boxgridlayer hinzufügen für groesseren layer
        JSlider volume = new JSlider();
        volume.setValue(40);volume.setMaximum(80);volume.setMinimum(0);volume.setToolTipText("Volume");
        volume.addChangeListener(e -> listener.onVolumeChange(volume.getValue()));
        buttonPanel.add(volume);

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
