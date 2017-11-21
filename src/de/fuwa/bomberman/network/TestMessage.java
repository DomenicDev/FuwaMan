package de.fuwa.bomberman.network;

import de.fuwa.bomberman.network.messages.AbstractMessage;


public class TestMessage extends AbstractMessage {

    private String message;

    public TestMessage() {
    }

    public TestMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
