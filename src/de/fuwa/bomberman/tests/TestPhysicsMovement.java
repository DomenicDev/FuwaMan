package de.fuwa.bomberman.tests;

import de.fuwa.bomberman.app.AppSettings;
import de.fuwa.bomberman.app.GameApplication;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.es.base.DefaultEntityData;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.appstates.InputAppState;
import de.fuwa.bomberman.game.appstates.PhysicsCharacterMovementAppState;
import de.fuwa.bomberman.game.appstates.session.GameSessionAppState;
import de.fuwa.bomberman.game.appstates.session.GameSessionHandler;
import de.fuwa.bomberman.game.appstates.visual.VisualAppState;
import de.fuwa.bomberman.game.appstates.visual.VisualGameFieldAppState;
import de.fuwa.bomberman.game.session.GameSession;
import de.fuwa.bomberman.game.utils.EntityCreator;

public class TestPhysicsMovement extends GameApplication {

    public static void main(String[] args) {
        TestPhysicsMovement test = new TestPhysicsMovement();
        AppSettings settings = new AppSettings(800, 600, false);
        test.start(settings);
    }

    @Override
    public void initGame() {
        EntityData entityData = new DefaultEntityData();
        getStateManager().attachState(new EntityDataState(entityData));

        getStateManager().attachState(new PhysicsCharacterMovementAppState());

        EntityId player = EntityCreator.createPlayer(entityData, 1, 1);

        GameSessionHandler sessionHandler = new GameSessionHandler();
        getStateManager().attachState(sessionHandler);
        GameSession gameSession = sessionHandler.createGameSession(player);
        getStateManager().attachState(new GameSessionAppState(gameSession));

        getStateManager().attachState(new VisualGameFieldAppState(11, 9));
        getStateManager().attachState(new VisualAppState());
        getStateManager().attachState(new InputAppState());

        final boolean map[][] = {{true, true, true, true, true, true, true, true, true, true, true},
                {true, false, false, false, false, false, false, false, false, false, true},
                {true, false, true, false, true, false, true, false, true, false, true},
                {true, false, false, false, false, false, false, false, false, false, true},
                {true, false, true, false, true, false, true, false, true, false, true},
                {true, false, false, false, false, false, false, false, false, false, true},
                {true, false, true, false, true, false, true, false, true, false, true},
                {true, false, false, false, false, false, false, false, false, false, true},
                {true, true, true, true, true, true, true, true, true, true, true}};

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 11; x++) {
                if (map[y][x]) {
                    EntityCreator.createBlock(entityData, x, y);
                }
            }

        }
    }
}
