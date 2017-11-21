package de.fuwa.bomberman.game.appstates.multiplayer.messages;

import de.fuwa.bomberman.game.enums.Setting;
import de.fuwa.bomberman.network.messages.AbstractMessage;

public class OnSetupGameMessage extends AbstractMessage {

    private Setting setting;
    private int sizeX, sizeY;

    public OnSetupGameMessage(Setting setting, int sizeX, int sizeY) {
        this.setting = setting;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public Setting getSetting() {
        return setting;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}
