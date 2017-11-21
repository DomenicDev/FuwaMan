package de.fuwa.bomberman.tests.network;

import de.fuwa.bomberman.app.AppSettings;
import de.fuwa.bomberman.app.GameApplication;
import de.fuwa.bomberman.game.appstates.MainGameAppState;
import de.fuwa.bomberman.game.appstates.multiplayer.GameServer;

public class TestServer extends GameApplication {

    public static void main(String[] args) {
        new TestServer().start(new AppSettings(800, 600, false));
    }

    @Override
    public void initGame() {
        MainGameAppState mainGameAppState = new MainGameAppState();
        getStateManager().attachState(mainGameAppState);

        GameServer gameServer = new GameServer(5555);
        getStateManager().attachState(gameServer);
    }
}
