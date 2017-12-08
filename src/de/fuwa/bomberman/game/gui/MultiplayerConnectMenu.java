package de.fuwa.bomberman.game.gui;


import javax.swing.*;
import java.awt.*;

public class MultiplayerConnectMenu extends BasicFuwaManPanel {

    @Override
    protected void addComponents() {
        JPanel contentPanel = new TransparentPanel();
        BorderLayout l = new BorderLayout();
        contentPanel.setLayout(l);
        this.centerPanel.setLayout(new GridLayout(3, 1)); // we only fill the center element later
        this.centerPanel.add(new TransparentPanel());
        this.centerPanel.add(contentPanel);
        //   this.centerPanel.add(new TransparentPanel());

        JPanel p = new TransparentPanel();
        p.setLayout(new GridLayout(1, 1));
        contentPanel.add(p, BorderLayout.CENTER);

        JPanel centerContentPanel = new TransparentPanel();
        centerContentPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(centerContentPanel);

        centerContentPanel.add(new JLabel("Host IP Address:"));

        JTextField ipTextField = new JTextField();
        ipTextField.setText("localhost");
        ipTextField.setPreferredSize(new Dimension(100, 30));
        centerContentPanel.add(ipTextField);

        JPanel bottomPanel = new TransparentPanel();
        bottomPanel.setPreferredSize(new Dimension(1, 150));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        JButton connectButton = new JButton("Connect");
        connectButton.setPreferredSize(new Dimension(150, 35));
        connectButton.addActionListener(e -> listener.onClickConnectToGame(ipTextField.getText().trim()));
        bottomPanel.add(connectButton);

        JButton returnButton = new JButton("Return");
        returnButton.setPreferredSize(new Dimension(150, 35));
        returnButton.addActionListener(e -> listener.onClickReturnToMainMenu());
        bottomPanel.add(returnButton);

        centerPanel.add(bottomPanel);
    }
}
