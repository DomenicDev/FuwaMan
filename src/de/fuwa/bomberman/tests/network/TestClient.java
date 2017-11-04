package de.fuwa.bomberman.tests.network;

import de.fuwa.bomberman.network.Client;
import de.fuwa.bomberman.network.ClientStateListener;
import de.fuwa.bomberman.network.TestMessage;

public class TestClient {

    public static void main(String[] args) {
        new TestClient();
    }

    TestClient() {

        Client c = new Client("localhost", 5555);
        c.addClientStateListener(new ClientStateListener() {

            @Override
            public void onClientConnected(Client c) {
                System.out.println("client has connected");
                c.send(new TestMessage("Hello server"));
            }

            @Override
            public void onClientDisconected() {

            }
        });
        c.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        c.close();


    }
}
