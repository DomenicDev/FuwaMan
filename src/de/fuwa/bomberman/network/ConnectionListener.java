package de.fuwa.bomberman.network;

/**
 * Provides methods for events regarding the connection state of a client.
 */
public interface ConnectionListener {

    /**
     * Called after a client has successfully connected to a server.
     *
     * @param connection
     */
    void onClientConnected(HostedConnection connection);

    /**
     * Called after a client has successfully disconnected from a server.
     *
     * @param connection
     */
    void onClientDisconnected(HostedConnection connection);

}
