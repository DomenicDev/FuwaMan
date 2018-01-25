package de.fuwa.bomberman.game.appstates.sound;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class BackgroundMusicAppState extends BaseAppState {

    private AudioInputStream inputStream;
    private Clip musicClip;

    @Override
    public void initialize(AppStateManager stateManager) {
        // load music file
        try {
            SoundVolumeAppState soundVolume = stateManager.getState(SoundVolumeAppState.class);
            File musicFile = new File("assets/Sounds/FuwaMan_Main_Theme.wav");
            this.inputStream = AudioSystem.getAudioInputStream(musicFile);
            this.musicClip = AudioSystem.getClip();
            this.musicClip.open(inputStream);
            FloatControl gainControl =
                    (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
           gainControl.setValue(-(soundVolume.getVolume()));                        //change Volume
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
            musicClip.close();
            musicClip = null;
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
