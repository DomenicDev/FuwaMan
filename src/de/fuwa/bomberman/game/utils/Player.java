package de.fuwa.bomberman.game.utils;

import java.io.Serializable;

/**
 * The player class stores the most relevant information
 * of a real player. But it also can be used for NPCs.
 * <p>
 * The Player class is also used as a kind of link to the in-game
 * players entity object.
 */
public class Player implements Serializable {

    private String name;
    private boolean ki;

    public Player(String name) {
        this.name = name;
        this.ki = false;
    }

    public Player(String name, boolean ki) {
        this.name = name;
        this.ki = ki;
    }

    public boolean isKi() {
        return ki;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
