package de.fuwa.bomberman.game.utils;

import de.fuwa.bomberman.game.components.PositionComponent;

public class GameUtils {

    /**
     * Returns true if the specified positions are logically in the same cell.
     *
     * @param p1 the first position
     * @param p2 the second position
     * @return true if the positions are refering to the same logical cell
     */
    public static boolean inSameCell(PositionComponent p1, PositionComponent p2) {
        int x1 = Math.round(p1.getX());
        int x2 = Math.round(p2.getX());
        int y1 = Math.round(p1.getY());
        int y2 = Math.round(p2.getY());
        return x1 == x2 && y1 == y2;
    }
}
