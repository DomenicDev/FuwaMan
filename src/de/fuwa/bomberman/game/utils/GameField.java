package de.fuwa.bomberman.game.utils;

import de.fuwa.bomberman.game.enums.BlockType;

/**
 * The GameField is a convenient class for representing the current game field
 * within a 2D array.
 * <p>
 * This type can also be used as a wrapper for the level editor.
 */
public class GameField {

    private BlockType[][] gameField;
    private int sizeX;
    private int sizeY;

    public GameField(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        gameField = new BlockType[sizeY][sizeX];
    }

    public void setBlock(int x, int y, BlockType type) {
        this.gameField[y][x] = type;
    }

    public BlockType getBlockType(int x, int y) {
        return gameField[y][x];
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

}
