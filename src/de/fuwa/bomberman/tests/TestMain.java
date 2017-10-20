package de.fuwa.bomberman.tests;

import de.fuwa.bomberman.app.AppSettings;
import de.fuwa.bomberman.app.GameApplication;

public class TestMain extends GameApplication {

    public static void main(String[] args) {
        TestMain test = new TestMain();
        AppSettings settings = new AppSettings(800, 600, false);
        test.start(settings);
    }

    @Override
    public void initGame() {

    }
}
