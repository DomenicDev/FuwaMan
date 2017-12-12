package de.fuwa.bomberman.game.utils;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.game.appstates.*;
import de.fuwa.bomberman.game.appstates.Ki.KiAppstate;
import de.fuwa.bomberman.game.appstates.multiplayer.ClientSideInterpolationAppState;
import de.fuwa.bomberman.game.appstates.sound.BackgroundMusicAppState;
import de.fuwa.bomberman.game.appstates.sound.ExplosionSoundAppState;
import de.fuwa.bomberman.game.appstates.visual.VisualAppState;
import de.fuwa.bomberman.game.appstates.visual.VisualBackgroundAppState;
import de.fuwa.bomberman.game.appstates.visual.VisualGameFieldAppState;
import de.fuwa.bomberman.game.appstates.visual.VisualPlayerAppState;

public class GameInitializer {

    /**
     * Here all game logic app states are initialized
     * @param stateManager
     */
    public static void initGameLogicAppStates(AppStateManager stateManager) {
        stateManager.attachState(new PhysicsCharacterMovementAppState());
        stateManager.attachState(new PowerUpAppState());
        stateManager.attachState(new KiAppstate());
        stateManager.attachState(new ExplosionAppState());
        stateManager.attachState(new BombAppState());
    }

    public static void removeGameLogicAppStates(AppStateManager stateManager) {
        stateManager.detachState(stateManager.getState(PhysicsCharacterMovementAppState.class));
        stateManager.detachState(stateManager.getState(PowerUpAppState.class));
        stateManager.detachState(stateManager.getState(KiAppstate.class));
        stateManager.detachState(stateManager.getState(ExplosionAppState.class));
        stateManager.detachState(stateManager.getState(BombAppState.class));
    }

    /**
     * Here all visual and auditive sound app states are initialized.
     * @param stateManager
     */
    public static void initVisualAndSoundAppStates(AppStateManager stateManager, int sizeX, int sizeY) {
        stateManager.attachState(new VisualGameFieldAppState(sizeX, sizeY));
        stateManager.attachState(new VisualAppState());
        stateManager.attachState(new VisualPlayerAppState());
        stateManager.attachState(new VisualBackgroundAppState());

        // Sound App States
        stateManager.attachState(new ExplosionSoundAppState());
        stateManager.attachState(new BackgroundMusicAppState());
    }

    public static void removeVisualAndSoundAppStates(AppStateManager stateManager) {
        stateManager.detachState(stateManager.getState(VisualGameFieldAppState.class));
        stateManager.detachState(stateManager.getState(VisualAppState.class));
        stateManager.detachState(stateManager.getState(VisualPlayerAppState.class));
        stateManager.detachState(stateManager.getState(VisualBackgroundAppState.class));

        stateManager.detachState(stateManager.getState(ExplosionSoundAppState.class));
        stateManager.detachState(stateManager.getState(BackgroundMusicAppState.class));
    }

    public static void initInputAppStates(AppStateManager stateManager) {
        stateManager.attachState(new InputAppState());
    }

    public static void removeInputAppStates(AppStateManager stateManager) {
        stateManager.detachState(stateManager.getState(InputAppState.class));
    }

    public static void initClientAppStates(AppStateManager stateManager) {
        stateManager.attachState(new ClientSideInterpolationAppState());
    }

}
