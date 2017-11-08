package de.fuwa.bomberman.network;

import de.fuwa.bomberman.network.messages.AbstractMessage;
import de.fuwa.bomberman.network.messages.Command;
import de.fuwa.bomberman.network.messages.DefaultCommandMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * A server is the endpoint clients connect to.
 * A server listens for incoming connections (clients) and
 * handles each of them separately in its own thread.
 */
public class Server {

    private ServerSocket server;
    private List<HostedConnection> connections = new ArrayList<>();

    private List<MessageListener<HostedConnection>> messageListeners = new ArrayList<>();
    private List<ConnectionListener> connectionListeners = new ArrayList<>();

    private boolean active = true;

    private int port;


    /**
     * Creates a server with the specified port.
     * In a remote situation you need to open the port.
     * @param port the port of the server
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     * Will start the server and makes him listening for connections.
     * @return true if server could be started successfully
     */
    public boolean start() {
        try {
            this.server = new ServerSocket(port);
            Thread serverThread = new Thread(new ServerHandler());
            serverThread.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Closes the server and all connections.
     */
    public void close() {
        // before we close the server we inform all clients
        for (HostedConnection conn : connections) {
            conn.send(new DefaultCommandMessage(Command.Exit));
        }
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMessageListener(MessageListener<HostedConnection> messageListener) {
        this.messageListeners.add(messageListener);
    }

    public void addConnectionListener(ConnectionListener l) {
        this.connectionListeners.add(l);
    }

    public List<MessageListener<HostedConnection>> getMessageListeners() {
        return this.messageListeners;
    }

    public List<ConnectionListener> getConnectionListeners() {
        return this.connectionListeners;
    }

    void onConnectionRemoved(HostedConnection connection) {
        System.out.println(connections.size());
        this.connections.remove(connection);
        System.out.println(connections.size());
        for (ConnectionListener l : connectionListeners) {
            l.onClientDisconnected(connection);
        }
    }

    void onConnectionAdded(HostedConnection connection) {
        this.connections.add(connection);
        for (ConnectionListener l : connectionListeners) {
            l.onClientConnected(connection);
        }
    }

    void onMessageReceived(HostedConnection conn, AbstractMessage message) {
        for (MessageListener<HostedConnection> l : messageListeners) {
            l.onMessageReceived(conn, message);
        }
    }

    /**
     * Our ServerHandler listens for incoming messages.
     */
    private class ServerHandler implements Runnable {

        @Override
        public void run() {
            try {
                while (active) {

                    // we wait for a new connection (client)
                    Socket client = server.accept();

                    // new client has connected
                    new HostedConnection(Server.this, client);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (!server.isClosed()) {
                        server.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
