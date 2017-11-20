package de.fuwa.bomberman.network;

public interface ClientStateListener {

    /**
     * Called after the client has connected to the server successfully.
     *
     * @param c the client object
     */
    void onClientConnected(Client c);

    /**
     * Called after the client has disconnected from the server.
     */
    void onClientDisconected();

}
