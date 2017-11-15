package de.fuwa.bomberman.tests;

import de.fuwa.bomberman.app.AppSettings;
import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.GameApplication;
import de.fuwa.bomberman.app.gui.AnimatedImageObject;
import de.fuwa.bomberman.app.gui.VisualGameField;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.es.base.DefaultEntityData;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.appstates.InputAppState;
import de.fuwa.bomberman.game.appstates.SimpleMovementAppState;
import de.fuwa.bomberman.game.appstates.session.GameSessionAppState;
import de.fuwa.bomberman.game.appstates.session.GameSessionHandler;
import de.fuwa.bomberman.game.appstates.visual.VisualAppState;
import de.fuwa.bomberman.game.appstates.visual.VisualGameFieldAppState;
import de.fuwa.bomberman.game.components.ModelComponent;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.components.WalkableComponent;
import de.fuwa.bomberman.game.enums.ModelType;
import de.fuwa.bomberman.game.enums.MoveDirection;
import de.fuwa.bomberman.game.session.GameSession;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TestMain extends GameApplication {

    public static void main(String[] args) {
        TestMain test = new TestMain();
        AppSettings settings = new AppSettings(800, 600, false);
        test.start(settings);
    }

    @Override
    public void initGame() {
  //      getStateManager().attachState(new TimerAppState());
  //      getStateManager().attachState(new DestroyerAppState());
 //   getStateManager().attachState(new GameFieldAppState());

        EntityData entityData = new DefaultEntityData();
        getStateManager().attachState(new EntityDataState(entityData));

        getStateManager().attachState(new SimpleMovementAppState());

        EntityId player = entityData.createEntity();
        entityData.setComponents(player, new PositionComponent(1,1), new WalkableComponent(MoveDirection.Idle, 1f), new ModelComponent(ModelType.Player, true));

        GameSessionHandler sessionHandler = new GameSessionHandler();
        getStateManager().attachState(sessionHandler);
        GameSession gameSession = sessionHandler.createGameSession(player);
        getStateManager().attachState(new GameSessionAppState(gameSession));

        getStateManager().attachState(new VisualGameFieldAppState(5,5));
        getStateManager().attachState(new VisualAppState());
        getStateManager().attachState(new InputAppState());
    }

    private class GameFieldAppState extends BaseAppState {

        List<AnimatedImageObject> objects = new ArrayList<>();
        float timer;

        @Override
        public void initialize(AppStateManager stateManager) {

            VisualGameField visualGameField = getGameContext().createAndDisplayGameField(10, 10);


            for (int y = 0; y < 10; y++) {
                for (int x = 0; x < 10; x++) {
                 //   Image testImage = stateManager.getGameApplication().getAssetLoader().loadSingleImage("assets/Textures/player.gif");
                    Image[] frames = stateManager.getGameApplication().getAssetLoader().loadAnimatedGif("assets/Textures/player.gif");
                    AnimatedImageObject o = new AnimatedImageObject(x, y, frames, true);
                    objects.add(o);
                    visualGameField.addGameObject(o);
                }
            }
        }

        @Override
        public void update(float tpf) {
            if (timer != -1 && (timer+=tpf) > 6) {
                timer = -1;
                for (AnimatedImageObject o : objects) {
                    o.setAnimated(false);
                }
            }
        }
    }

    private class TimerAppState extends BaseAppState {

        private float timer = 0;

        @Override
        public void update(float tpf) {
            timer += tpf;
            System.out.println(timer);
        }

        @Override
        public void cleanup() {
            System.out.println("bin in cleanup");
        }
    }

    private class DestroyerAppState extends BaseAppState {

        private AppStateManager stateManager;

        private float timer = 0;

        @Override
        public void initialize(AppStateManager stateManager) {
            this.stateManager = stateManager;
        }

        @Override
        public void update(float tpf) {
            timer += tpf;

            if (timer > 10) {
                stateManager.detachState(stateManager.getState(TimerAppState.class));
            }
        }
    }

}
