package de.fuwa.bomberman.game.session;

import de.fuwa.bomberman.game.enums.MoveDirection;

/**
 * Each player has its own GameSession object which is
 * the interface between the users input and the game logic.
 *
 * This class collects all methods which let the player interact with the game.
 */
public interface GameSession {

    /**
     * Will place a bomb at the current position of the player.
     */
    void placeBomb();

    /**
     * Will apply the current move direction of the player.
     * @param direction the direction the player is moving in
     */
    void applyMoveDirection(MoveDirection direction);

}
