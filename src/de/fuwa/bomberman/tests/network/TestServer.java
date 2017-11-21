package de.fuwa.bomberman.tests.network;

import de.fuwa.bomberman.app.AppSettings;
import de.fuwa.bomberman.app.GameApplication;
import de.fuwa.bomberman.game.appstates.MainGameAppState;
import de.fuwa.bomberman.game.appstates.multiplayer.GameServer;

public class TestServer extends GameApplication {

    TestServer() {
//        Server server = new Server(5555);
//        server.addMessageListener((source, m) -> {
//            System.out.println("Message received");
//        });
//        server.addConnectionListener(new ConnectionListener() {
//            @Override
//            public void onClientConnected(HostedConnection connection) {
//                System.out.println("uiih, a client has connected");
//            }
//
//            @Override
//            public void onClientDisconnected(HostedConnection connection) {
//                System.out.println("oooohh, a client disconnected");
//            }
//        });
//
//        server.start();




    /*    try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        server.close();
    */
    }

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
