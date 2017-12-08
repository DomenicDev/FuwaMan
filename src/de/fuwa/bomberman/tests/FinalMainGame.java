package de.fuwa.bomberman.tests;

import de.fuwa.bomberman.app.AppSettings;
import de.fuwa.bomberman.app.GameApplication;
import de.fuwa.bomberman.game.appstates.FuwaManAppState;

public class FinalMainGame extends GameApplication {

    public static void main(String[] args) {
        FinalMainGame game = new FinalMainGame();
        AppSettings settings = new AppSettings(800, 800, false);
        game.start(settings);
    }

    @Override
    public void initGame() {
        getStateManager().attachState(new FuwaManAppState());
    }
}
