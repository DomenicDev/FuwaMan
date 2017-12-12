package de.fuwa.bomberman.game.appstates.sound;

import de.fuwa.bomberman.app.BaseAppState;

public class SoundVolumeAppState extends BaseAppState {

    private final float max = 80.0f;
    private float volume = 0.0f;
    private float unchanged = 0.0f;

    public float getMax() {
        return max;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getUnchanged() {
        return unchanged;
    }

    public void setUnchanged(float unchanged) {
        this.unchanged = unchanged;
    }



}
