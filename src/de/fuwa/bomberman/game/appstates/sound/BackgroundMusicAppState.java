package de.fuwa.bomberman.game.appstates.sound;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.game.appstates.FuwaManAppState;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class BackgroundMusicAppState extends BaseAppState {


    private Clip musicClip;
    private SoundVolumeAppState soundVolume;

    @Override
    public void initialize(AppStateManager stateManager) {
        // load music file
        try {
            //soundVolume = new SoundVolumeAppState();
            File musicFile = new File("assets/Sounds/FuwaMan_Main_Theme.wav");
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(musicFile);
            this.musicClip = AudioSystem.getClip();
            this.musicClip.open(inputStream);
            FloatControl gainControl =
                    (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            System.out.println();
           // gainControl.setValue(-(soundVolume.getVolume()));                        //change Volume
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
