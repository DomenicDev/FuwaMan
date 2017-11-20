package de.fuwa.bomberman.network.messages;

import java.io.Serializable;

/**
 * Used internally for basic connection build.
 */
public final class DefaultCommandMessage implements Serializable {

    private Command command;

    public DefaultCommandMessage() {
    }

    public DefaultCommandMessage(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
