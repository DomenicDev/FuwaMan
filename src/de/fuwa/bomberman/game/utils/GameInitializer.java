package de.fuwa.bomberman.game.utils;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.game.appstates.*;
import de.fuwa.bomberman.game.appstates.Ki.KiAppstate;
import de.fuwa.bomberman.game.appstates.multiplayer.ClientSideInterpolationAppState;
import de.fuwa.bomberman.game.appstates.sound.ExplosionSoundAppState;
import de.fuwa.bomberman.game.appstates.visual.VisualAppState;
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
        stateManager.detachState(stateManager.getState(BombAppState.class));
        stateManager.detachState(stateManager.getState(PowerUpAppState.class));
        stateManager.detachState(stateManager.getState(KiAppstate.class));
    }

    /**
     * Here all visual and auditive sound app states are initialized.
     * @param stateManager
     */
    public static void initVisualAndSoundAppStates(AppStateManager stateManager, int sizeX, int sizeY) {
        stateManager.attachState(new VisualGameFieldAppState(sizeX, sizeY));
        stateManager.attachState(new VisualAppState());
        stateManager.attachState(new VisualPlayerAppState());

        // Sound App States
        stateManager.attachState(new ExplosionSoundAppState());
    }

    public static void initInputAppStates(AppStateManager stateManager) {
        stateManager.attachState(new InputAppState());
    }

    public static void initClientAppStates(AppStateManager stateManager) {
        stateManager.attachState(new ClientSideInterpolationAppState());
    }

}
