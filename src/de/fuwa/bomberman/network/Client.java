package de.fuwa.bomberman.network;

import de.fuwa.bomberman.network.messages.AbstractMessage;
import de.fuwa.bomberman.network.messages.Command;
import de.fuwa.bomberman.network.messages.DefaultCommandMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * A client is a part of our network architecture.
 * A client first has to connect to a server.
 * After that messages can be sent and received.
 */
public class Client {

    private Socket client;
    private String ip;
    private int port;

    private ObjectOutputStream output;
    private ObjectInputStream input;

    private ClientHandler handler;

    private List<ClientStateListener> clientStateListeners = new ArrayList<>();

    /**
     * Creates a new client object with the specified parameters.
     * This will NOT start instantly! Use start() instead!
     *
     * @param ip   the ip address of the server
     * @param port the port of the server
     */
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Starts building the connection to the server.
     * Use {@link ClientStateListener} if you want to know when
     * the client has connected to the server
     */
    public boolean start() {
        if (client != null) {
            return false;
        }
        try {
            this.client = new Socket(ip, port);

            this.output = new ObjectOutputStream(client.getOutputStream());
            this.input = new ObjectInputStream(client.getInputStream());

            this.handler = new ClientHandler();

            Thread clientThread = new Thread(handler);
            clientThread.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Closes the connection to the server.
     * Note that this method will send a command message to the server
     * which aks for proper disconnect.
     * Use {@link ClientStateListener} if you want to know exactly
     * when the client has disconnected from the server.
     */
    public void close() {
        if (handler != null && client != null && !client.isClosed()) {
            handler.send(new DefaultCommandMessage(Command.Exit));
        }
    }

    /**
     * Adds the specified listener to that client list.
     * Note: Add listeners before starting the client.
     * Otherwise you could be to late and you would not receive the <code>onClientConnected()</code> event
     *
     * @param listener the listener to add
     */
    public void addClientStateListener(ClientStateListener listener) {
        this.clientStateListeners.add(listener);
    }

    /**
     * Sends the specified message to the server.
     *
     * @param m the message to send to the server
     */
    public void send(AbstractMessage m) {
        if (m != null && handler != null) {
            handler.send(m);
        }
    }

    /**
     * The ClientHandler basically handles the messaging.
     */
    private class ClientHandler implements Runnable {

        ClientHandler() {
            send(new DefaultCommandMessage(Command.Join));
        }

        private void send(Serializable message) {
            try {
                output.writeObject(message);
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Object o;
                    if ((o = input.readObject()) != null) {

                        if (o instanceof DefaultCommandMessage) {
                            DefaultCommandMessage dm = (DefaultCommandMessage) o;
                            Command cmd = dm.getCommand();
                            if (cmd == Command.Join) {
                                for (ClientStateListener l : clientStateListeners) {
                                    l.onClientConnected(Client.this);
                                }
                            } else if (cmd == Command.Exit) {
                                System.out.println("received exit message");
                                break;
                            }
                        }

                        if (o instanceof TestMessage) {
                            if (((TestMessage) o).getMessage().equals("JOIN")) {
                                System.out.println("i am joined");
                            }
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                    output.close();
                    client.close();

                    for (ClientStateListener l : clientStateListeners) {
                        l.onClientDisconected();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
