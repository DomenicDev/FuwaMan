package de.fuwa.bomberman.network;

import de.fuwa.bomberman.network.messages.AbstractMessage;
import de.fuwa.bomberman.network.messages.Command;
import de.fuwa.bomberman.network.messages.DefaultCommandMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A client is a part of our network architecture.
 * A client first has to connect to a server.
 * After that messages can be sent and received.
 */
public class Client {

    private static Logger logger = Logger.getLogger(Client.class.getName());

    private Socket client;
    private String ip;
    private int port;

    private ObjectOutputStream output;
    private ObjectInputStream input;

    private ClientHandler handler;

    private ConcurrentLinkedQueue<ClientStateListener> clientStateListeners = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<MessageListener<Client>> messageListeners = new ConcurrentLinkedQueue<>();

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
    public boolean connectToServer() {
        if (client != null) {
            return false;
        }
        try {
            this.client = new Socket(ip, port);

            this.output = new ObjectOutputStream(client.getOutputStream());
            this.input = new ObjectInputStream(client.getInputStream());

            this.handler = new ClientHandler();
            return true;
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not establish connection!");
        }
        return false;
    }

    public void start() {
        Thread clientThread = new Thread(handler);
        clientThread.start();
        handler.send(new DefaultCommandMessage(Command.Join));
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

    public void addMessageListener(MessageListener<Client> listener) {
        this.messageListeners.add(listener);
    }
    /**
     * Sends the specified message to the server.
     *
     * @param m the message to send to the server
     */
    public synchronized void send(AbstractMessage m) {
        if (m != null && handler != null) {
            handler.send(m);
        }
    }

    /**
     * The ClientHandler basically handles the messaging.
     */
    private class ClientHandler implements Runnable {

        ClientHandler() {

        }

        private synchronized void send(Serializable message) {
            try {
                output.writeObject(message);
                output.flush();
            } catch (IOException i) {
                logger.log(Level.WARNING, "Failed to send message: " + message);
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
                                logger.log(Level.FINE, "Received Exit Message. Client will be closed.");
                                break;
                            }
                        }

                        if (o instanceof AbstractMessage) {
                            AbstractMessage am = (AbstractMessage) o;
                            for (MessageListener<Client> listener : messageListeners) {
                                listener.onMessageReceived(Client.this, am);
                            }
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.WARNING, "Client is closing.");
            } finally {
                try {
                    input.close();
                    output.close();
                    client.close();

                    for (ClientStateListener l : clientStateListeners) {
                        l.onClientDisconnected();
                    }

                    messageListeners.clear();
                    clientStateListeners.clear();

                    logger.log(Level.INFO, "Client successfully closed.");
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Client could not close properly.");
                }
            }
        }
    }

}
