package de.fuwa.bomberman.game.utils;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.game.appstates.BombAppState;
import de.fuwa.bomberman.game.appstates.InputAppState;
import de.fuwa.bomberman.game.appstates.PhysicsCharacterMovementAppState;
import de.fuwa.bomberman.game.appstates.visual.VisualAppState;
import de.fuwa.bomberman.game.appstates.visual.VisualGameFieldAppState;

public class GameInitializer {

    /**
     * Here all game logic app states are initialized
     * @param stateManager
     */
    public static void initGameLogicAppStates(AppStateManager stateManager) {
        stateManager.attachState(new PhysicsCharacterMovementAppState());
        stateManager.attachState(new BombAppState());
    }

    /**
     * Here all visual and auditive sound app states are initialized.
     * @param stateManager
     */
    public static void initVisualAndSoundAppStates(AppStateManager stateManager, int sizeX, int sizeY) {
        stateManager.attachState(new VisualGameFieldAppState(sizeX, sizeY));
        stateManager.attachState(new VisualAppState());
    }

    public static void initInputAppStates(AppStateManager stateManager) {
        stateManager.attachState(new InputAppState());
    }

}
