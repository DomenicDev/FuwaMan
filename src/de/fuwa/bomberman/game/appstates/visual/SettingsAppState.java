package de.fuwa.bomberman.game.appstates.visual;

import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.game.enums.Setting;

/**
 * This app state just holds the current set game-setting for other app states.
 * Especially, the visual app states will need to decide which texture to draw
 * depending on the setting.
 */
public class SettingsAppState extends BaseAppState {

    private Setting currentSetting;

    public SettingsAppState(Setting currentSetting) {
        this.currentSetting = currentSetting;
    }

    /**
     * @return the current setting for this game session
     */
    public Setting getCurrentSetting() {
        return currentSetting;
    }

    @Override
    public void cleanup() {
        this.currentSetting = null;
    }
}
