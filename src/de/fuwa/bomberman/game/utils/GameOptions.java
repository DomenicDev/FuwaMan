package de.fuwa.bomberman.game.utils;

import de.fuwa.bomberman.game.enums.Setting;

public class GameOptions {

    private Setting setting;
    private GameField gameField;
    private int numberOfKis;
    private float matchDuration;

    public GameOptions() {
    }

    public GameOptions(Setting setting, GameField gameField, int numberOfKis) {
        this.setting = setting;
        this.gameField = gameField;
        this.numberOfKis = numberOfKis;
        this.matchDuration = GameConstants.DEFAULT_MATCH_DURATION;
    }

    public float getMatchDuration() {
        return matchDuration;
    }

    public void setMatchDuration(float matchDuration) {
        this.matchDuration = matchDuration;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public GameField getGameField() {
        return gameField;
    }

    public void setGameField(GameField gameField) {
        this.gameField = gameField;
    }

    public int getNumberOfKis() {
        return numberOfKis;
    }

    public void setNumberOfKis(int numberOfKis) {
        this.numberOfKis = numberOfKis;
    }
}
