package de.fuwa.bomberman.network;

import de.fuwa.bomberman.network.messages.AbstractMessage;
import de.fuwa.bomberman.network.messages.Command;
import de.fuwa.bomberman.network.messages.DefaultCommandMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HostedConnections are used by the server for communication (messaging).
 * Messages from the client are received here.
 * Also it is possible to sent messages to that client
 */
public class HostedConnection {

    private static Logger logger = Logger.getLogger(HostedConnection.class.getName());

    private Socket client;
    private Server server;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    /**
     * Creates a new HostedConnection.
     *
     * @param server the server (used as reference)
     * @param client the client (to extract the streams from)
     */
    HostedConnection(Server server, Socket client) {
        this.server = server;
        this.client = client;

        try {
            this.input = new ObjectInputStream(client.getInputStream());
            this.output = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread clientThread = new Thread(new ConnectionHandler());
        clientThread.start();
    }

    /**
     * Sends the specified message to the client
     *
     * @param message the message to sent to the client
     */
    public synchronized void send(AbstractMessage message) {
        if (output == null) return;
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Only used internally. DO NOT CALL this!
     *
     * @param commandMessage the command message
     */
    void send(DefaultCommandMessage commandMessage) {
        if (output == null) return;
        try {
            output.writeObject(commandMessage);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The ConnectionHandler handles the messaging between server and client.
     */
    private class ConnectionHandler implements Runnable {

        private ConnectionHandler() {
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
                        // abstract messages are custom messages
                        if (o instanceof AbstractMessage) {
                            AbstractMessage message = (AbstractMessage) o;
                            server.onMessageReceived(HostedConnection.this, message);
                        } else if (o instanceof DefaultCommandMessage) {
                            DefaultCommandMessage dm = (DefaultCommandMessage) o;
                            Command cmd = dm.getCommand();
                            if (cmd == Command.Exit) {
                                send(dm);
                                break;
                            } else if (cmd == Command.Join) {
                                // client asks to "accept" our connection, we do so...
                                // to apply the connection we send back the command
                                send(dm);

                                // now we inform the listeners about the added connection
                                server.onConnectionAdded(HostedConnection.this);
                            }
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.INFO, "HostedConnection has been disconnected.");
            } finally {
                try {
                    input.close();
                    output.close();
                    client.close();
                    logger.log(Level.INFO, "Closed HostedConnection successfully.");
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Could not close Hosted Connection properly.");
                }

                // we inform our listeners about the closed connection
                server.onConnectionRemoved(HostedConnection.this);
            }
        }
    }

}
