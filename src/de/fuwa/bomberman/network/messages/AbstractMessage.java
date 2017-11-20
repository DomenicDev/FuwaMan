package de.fuwa.bomberman.network.messages;

import java.io.Serializable;

/**
 * Make your messages extend this class for messaging.
 * You need to implements the {@link Serializable} interface
 * for your custom objects if they are contained in the messages.
 */
public class AbstractMessage implements Serializable {

    public AbstractMessage() {
    }

}
