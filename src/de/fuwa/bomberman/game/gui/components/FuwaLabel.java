package de.fuwa.bomberman.game.gui.components;

import javax.swing.*;
import java.awt.*;

public class FuwaLabel extends JLabel {

    private static final Font TEXT_FONT = new Font("joystix monospace", Font.BOLD, 12);

    public FuwaLabel(String text) {
        super(text);
        setFont(TEXT_FONT);
    }
}
