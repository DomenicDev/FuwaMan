package de.fuwa.bomberman.game.gui;

import javax.swing.*;
import java.awt.*;

public class ClientWaitingLobbyMenu extends BasicFuwaManPanel {

    @Override
    protected void addComponents() {

        JPanel waitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(waitPanel);

        JLabel waitingTextLabel = new JLabel("Waiting for Host to start the game...");
        waitPanel.add(waitingTextLabel);

    }
}
