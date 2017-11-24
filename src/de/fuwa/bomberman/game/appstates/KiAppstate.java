package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.appstates.session.GameSessionAppState;
import de.fuwa.bomberman.game.appstates.session.MultipleGameSessionAppState;
import de.fuwa.bomberman.game.components.KIComponent;
import de.fuwa.bomberman.game.components.PlayerComponent;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.enums.KiActionType;
import de.fuwa.bomberman.game.enums.MoveDirection;
import de.fuwa.bomberman.game.session.GameSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KiAppstate extends BaseAppState {

    Map<EntityId, KiAction> kiActionsMap = new HashMap<>();
    MultipleGameSessionAppState multipleGameSessionAppState;
    EntityData entityData;
    EntitySet entitySet;

    @Override
    public void initialize(AppStateManager stateManager) {

        multipleGameSessionAppState = stateManager.getState(MultipleGameSessionAppState.class);

        this.entityData = stateManager.getState(EntityDataState.class).getEntityData();
        entitySet = entityData.getEntities(PositionComponent.class, KIComponent.class, PlayerComponent.class);
    }

    @Override
    public void update(float tpf){
        if (entitySet.applyChanges()) {
            for(Entity entity : entitySet.getAddedEntities()){
                GameSession gameSession = multipleGameSessionAppState.getGameSession(entity.getId());
                gameSession.applyMoveDirection(MoveDirection.Left);
            }
        }
    }

    public class KiAction{
        float posX;
        float posY;
        KiActionType kiActionType;

        public KiAction(float posX, float posY, KiActionType kiActionType) {
            this.posX = posX;
            this.posY = posY;
            this.kiActionType = kiActionType;
        }
    }
}