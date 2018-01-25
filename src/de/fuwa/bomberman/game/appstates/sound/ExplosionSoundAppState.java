package de.fuwa.bomberman.game.appstates.sound;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.components.ExplosionComponent;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExplosionSoundAppState extends BaseAppState {

    private static final String SOUND_PATH = "assets/Sounds/";
    private EntitySet explosions;

    private SoundVolumeAppState soundVolume;

    private Map<AudioClip, Float> activeAudios = new HashMap<>();
    private List<AudioClip> clipsToRemove = new ArrayList<>();
    private File soundFile;

    @Override
    public void initialize(AppStateManager stateManager) {
        EntityData entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.explosions = entityData.getEntities(ExplosionComponent.class);
        this.soundVolume = stateManager.getState(SoundVolumeAppState.class);

        // Open an audio input stream.
        // from a wave File
        this.soundFile = new File(SOUND_PATH + "bomb.wav");

    }

    @Override
    public void update(float tpf) {
        if (explosions.applyChanges()) {

            if (explosions.getAddedEntities().size() > 0) {
                createSound();
            }

        }

        // count down timer
        for (Map.Entry<AudioClip, Float> e : activeAudios.entrySet()) {
            float newTime = e.getValue() - tpf;
            if (newTime <= 0) {
                // time is over we want to remove this clip
                clipsToRemove.add(e.getKey());
            } else {
                // we apply the new remaining time
                activeAudios.put(e.getKey(), newTime);
            }
        }

        // now we want to remove any clips listed in our remove list
        for (AudioClip c : clipsToRemove) {
            c.close();
            activeAudios.remove(c);
        }
        // we want to clear the list, otherwise we would close a clip all the time
        clipsToRemove.clear();
    }

    private void createSound() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-(soundVolume.getVolume())); //change Volume
            clip.start();
            AudioClip audio = new AudioClip(clip, audioIn);
            this.activeAudios.put(audio, 3f);
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cleanup() {
        // clear entity set
        this.explosions.close();
        this.explosions.clear();
        this.explosions = null;

        // close every still active audios
        for (AudioClip c : activeAudios.keySet()) {
            c.close();
        }

        // clear maps and lists
        activeAudios.clear();
        clipsToRemove.clear();

        // delete reference to sound file
        this.soundFile = null;
    }

    private class AudioClip {

        private Clip clip;
        private AudioInputStream inputStream;

        public AudioClip(Clip clip, AudioInputStream inputStream) {
            this.clip = clip;
            this.inputStream = inputStream;
        }

        private void close() {
            try {
                this.clip.close();
                this.inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
