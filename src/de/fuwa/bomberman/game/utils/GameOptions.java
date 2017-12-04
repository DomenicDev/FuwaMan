package de.fuwa.bomberman.game.utils;

import de.fuwa.bomberman.game.enums.Setting;

public class GameOptions {

    private Setting setting;
    private int gameFieldSizeX;
    private int getGameFieldSizeY;
    private int numberOfKis;

    public GameOptions() {
    }

    public GameOptions(Setting setting, int gameFieldSizeX, int getGameFieldSizeY, int numberOfKis) {
        this.setting = setting;
        this.gameFieldSizeX = gameFieldSizeX;
        this.getGameFieldSizeY = getGameFieldSizeY;
        this.numberOfKis = numberOfKis;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public int getGameFieldSizeX() {
        return gameFieldSizeX;
    }

    public void setGameFieldSizeX(int gameFieldSizeX) {
        this.gameFieldSizeX = gameFieldSizeX;
    }

    public int getGetGameFieldSizeY() {
        return getGameFieldSizeY;
    }

    public void setGetGameFieldSizeY(int getGameFieldSizeY) {
        this.getGameFieldSizeY = getGameFieldSizeY;
    }

    public int getNumberOfKis() {
        return numberOfKis;
    }

    public void setNumberOfKis(int numberOfKis) {
        this.numberOfKis = numberOfKis;
    }
}
