package de.fuwa.bomberman.tests;

import de.fuwa.bomberman.app.AppSettings;
import de.fuwa.bomberman.app.GameApplication;
import de.fuwa.bomberman.game.gui.LevelEditorMenu;

public class LevelEditorTest extends GameApplication {

    public static void main(String[] args) {
        LevelEditorTest test = new LevelEditorTest();
        AppSettings settings = new AppSettings(800, 800, false);
        test.start(settings);
    }

    @Override
    public void initGame() {
        getGameContext().setScreen(new LevelEditorMenu(12, 11));
    }
}
