package de.fuwa.bomberman.game.appstates.sound;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.components.ExplosionComponent;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class ExplosionSoundAppState extends BaseAppState {

    private static final String SOUND_PATH = "assets/Sounds/";
    private EntitySet explosions;

    @Override
    public void initialize(AppStateManager stateManager) {
        EntityData entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.explosions = entityData.getEntities(ExplosionComponent.class);
    }

    @Override
    public void update(float tpf) {
        if (explosions.applyChanges()) {

            for (Entity entity : explosions.getAddedEntities()) {
                createSound();
            }

        }
    }

    private void createSound() {
        try {
            // Open an audio input stream.
            // from a wave File
            File soundFile = new File(SOUND_PATH + "bomb.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
