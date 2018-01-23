package de.fuwa.bomberman.game.gui;

import de.fuwa.bomberman.game.gui.components.FuwaButton;
import de.fuwa.bomberman.game.gui.components.FuwaLabel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GuiUtils {

    static {

        try {
            Font f = Font.createFont(Font.TRUETYPE_FONT, new File("assets/Fonts/joystix monospace.ttf"));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public static JButton createButton(String text) {
        return new FuwaButton(text);
    }

    public static JLabel createLabel(String text) {
        return new FuwaLabel(text);
    }
}
