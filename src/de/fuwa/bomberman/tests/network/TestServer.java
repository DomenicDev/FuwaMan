package de.fuwa.bomberman.tests.network;

import de.fuwa.bomberman.network.ConnectionListener;
import de.fuwa.bomberman.network.HostedConnection;
import de.fuwa.bomberman.network.Server;

public class TestServer {

    TestServer() {
        Server server = new Server(5555);
        server.addMessageListener((source, m) -> {
            System.out.println("Message received");
        });
        server.addConnectionListener(new ConnectionListener() {
            @Override
            public void onClientConnected(HostedConnection connection) {
                System.out.println("uiih, a client has connected");
            }

            @Override
            public void onClientDisconnected(HostedConnection connection) {
                System.out.println("oooohh, a client disconnected");
            }
        });

        server.start();


    /*    try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        server.close();
    */
    }

    public static void main(String[] args) {
        new TestServer();
    }
}
