package de.fuwa.bomberman.tests.network;

import de.fuwa.bomberman.app.AppSettings;
import de.fuwa.bomberman.app.GameApplication;
import de.fuwa.bomberman.game.appstates.multiplayer.GameClient;
import de.fuwa.bomberman.game.appstates.state.GameStateHandler;

public class TestClient extends GameApplication {


    public static void main(String[] args) {
        new TestClient().start(new AppSettings(800, 600, false));
    }

    @Override
    public void initGame() {

        GameStateHandler stateHandler = new GameStateHandler();
        getStateManager().attachState(stateHandler);

        GameClient client = new GameClient("localhost", 5555);
        if (client.connect("localhost", 5555)) {
            System.out.println("hier");
            client.setGameStateListener(stateHandler);
            client.start();
            getStateManager().attachState(client);
        }


        //      client.connect("localhost", 5555);

    }
}
