package de.fuwa.bomberman.tests.network;

import java.io.Serializable;

public class TestMessage implements Serializable {

    private String message;

    public TestMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
