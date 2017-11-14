package de.fuwa.bomberman.tests;

import de.fuwa.bomberman.app.AppSettings;
import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.GameApplication;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.appstates.visual.VisualAppState;
import de.fuwa.bomberman.game.appstates.visual.VisualGameFieldAppState;
import de.fuwa.bomberman.game.components.ModelComponent;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.enums.ModelType;

public class TestVisuals extends GameApplication {

    public static void main(String[] args) {
        TestVisuals testVisuals = new TestVisuals();
        AppSettings settings = new AppSettings(800, 600, false);
        testVisuals.start(settings);
    }

    @Override
    public void initGame() {

        getStateManager().attachState(new EntityDataState());
        getStateManager().attachState(new VisualGameFieldAppState(10,10));
        getStateManager().attachState(new VisualAppState());
        getStateManager().attachState(new Initializer());

    }

    private class Initializer extends BaseAppState {

        private EntityId player;
        private EntityData entityData;

        private float xPos, yPos;

        private float timer = 0;

        @Override
        public void initialize(AppStateManager stateManager) {
            entityData = getStateManager().getState(EntityDataState.class).getEntityData();

            player = entityData.createEntity();
            entityData.setComponents(player, new PositionComponent(5, 5), new ModelComponent(ModelType.Player, false));

            EntityId block = entityData.createEntity();
            entityData.setComponents(block, new PositionComponent(2,1), new ModelComponent(ModelType.DestroyableTile, false));

            xPos = 5;
            yPos = 5;
        }

        @Override
        public void update(float tpf) {
            timer += tpf;
            //   entityData.setComponent(player, new PositionComponent((float) (xPos + 1.5f* Math.sin(timer)), (float) (yPos + 1.5*Math.cos(timer))));
            entityData.setComponent(player, new PositionComponent(xPos += tpf, 4));
        }
    }
}
