package de.fuwa.bomberman.game.appstates.sound;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class BackgroundMusicAppState extends BaseAppState {

    private Clip musicClip;

    @Override
    public void initialize(AppStateManager stateManager) {
        // load music file

        try {
            File musicFile = new File("assets/Sounds/FuwaMan_Main_Theme.wav");
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(musicFile);
            this.musicClip = AudioSystem.getClip();
            this.musicClip.open(inputStream);
            this.musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            this.musicClip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cleanup() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip = null;
        }
    }
}
