package de.fuwa.bomberman.game.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;

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

        JLabel volumeLabel = new JLabel("Volume", SwingConstants.CENTER);
        buttonPanel.add(volumeLabel);
        //Boxgridlayer hinzufügen für groesseren layer

        JSlider volume = new JSlider();
        JButton bntvolume = new JButton("Volumne: "+ volume.getValue()+" %");
        bntvolume.setToolTipText("Mute");
        bntvolume.addActionListener(e -> listener.onVolumeChange(0));
        bntvolume.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                volume.setValue(0);
            }
        });
        buttonPanel.add(bntvolume);
        volume.setValue(40);volume.setMaximum(80);volume.setMinimum(0);volume.setToolTipText("Volume");
        volume.addChangeListener(e -> listener.onVolumeChange(volume.getValue()));
        volume.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                bntvolume.setText("Volumne: "+ volume.getValue()+" %");
            }
        });
        buttonPanel.add(volume);

        JButton fullscreen = new JButton("Fullscreen");
        fullscreen.addActionListener(e-> listener.onClickFullscreen());
        buttonPanel.add(fullscreen);

        JButton window = new JButton("Window");
        window.addActionListener(e->listener.onClickWindow());
        buttonPanel.add(window);

        JButton saveChanges = GuiUtils.createButton("Save Changes");
        saveChanges.addActionListener(e->listener.onClickSaveChanges());
        buttonPanel.add(saveChanges);

        JButton returnButton = GuiUtils.createButton("Return");
        returnButton.addActionListener(e -> listener.onClickReturnToMainMenu());
        buttonPanel.add(returnButton);
    }

}
