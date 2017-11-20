package de.fuwa.bomberman.network;

import de.fuwa.bomberman.network.messages.AbstractMessage;

/**
 * A MessageListener is used from both: Client and Server.
 * That's thy we use a generic type T here.
 *
 * @param <T> it is either {@link HostedConnection} or {@link Client}
 */
public interface MessageListener<T> {

    void onMessageReceived(T source, AbstractMessage m);

}
