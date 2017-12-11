package de.fuwa.bomberman.game.gui;

import de.fuwa.bomberman.game.enums.Setting;
import de.fuwa.bomberman.game.utils.GameOptions;
import de.fuwa.bomberman.game.utils.GameUtils;

import javax.swing.*;
import java.awt.*;

public class Credits extends BasicFuwaManPanel {

    @Override
    protected void addComponents() {
        JPanel contentPanel;
        contentPanel = new TransparentPanel();
        GridLayout l = new GridLayout(2, 1);
        l.setVgap(50);
        l.setHgap(250);
        contentPanel.setLayout(l);
        this.centerPanel.setLayout(new GridLayout(1, 1)); // we only fill the center element later
        this.centerPanel.add(new TransparentPanel());
        this.centerPanel.add(contentPanel);
        this.centerPanel.add(new TransparentPanel());
        //Scrollpane & JPanle Implementieren
        JScrollPane scrollPane;
        JPanel panel;

        //create JLabel
        JLabel text = new JLabel();
        //add Text
        text.setText("" +
                 "<html>Domenic Cassisi<br />Projektleiter, Programmierung & MusikDesign<br />" +
                 "<br />Thomas Fuhr<br />Graphik & GameDesign" +
                 "<br /><br />Niels Schneider<br />Graphik & Programmierung <br />" +
                 "<br />Tim Scheifler<br />GameDesign<br />" +
                 "<br />Ilja Mascharov<br />Graphik & Programmierung<br />" +
                 "<br />Thank you all for working so good!!</html>"
        );
        // add scrollPane and set panel transparent
        scrollPane = new JScrollPane(panel = new TransparentPanel());
        //add Text from JLabel to JPanel
        panel.add(text);
        // set scrollPane transparent
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false); //transparent
        contentPanel.add(scrollPane);

        // add Returnbutton to get into MainMenu
        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> listener.onClickReturnToMainMenu());
        contentPanel.add(returnButton);
}
}
