package de.fuwa.bomberman.game.utils;

public class NamedGameField {

    private String name;
    private GameField gameField;

    public NamedGameField(String name, GameField gameField) {
        this.name = name;
        this.gameField = gameField;
    }

    public String getName() {
        return name;
    }

    public GameField getGameField() {
        return gameField;
    }

    @Override
    public String toString() {
        return name + " [" + gameField.getSizeX() + "x" + gameField.getSizeY() + "]";
    }
}
