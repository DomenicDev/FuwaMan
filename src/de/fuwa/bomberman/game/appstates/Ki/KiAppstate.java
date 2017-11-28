package de.fuwa.bomberman.game.appstates.Ki;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.appstates.MainGameAppState;
import de.fuwa.bomberman.game.appstates.session.GameSessionAppState;
import de.fuwa.bomberman.game.appstates.session.MultipleGameSessionAppState;
import de.fuwa.bomberman.game.components.*;
import de.fuwa.bomberman.game.enums.BlockType;
import de.fuwa.bomberman.game.enums.KiActionType;
import de.fuwa.bomberman.game.enums.MoveDirection;
import de.fuwa.bomberman.game.session.GameSession;
import de.fuwa.bomberman.game.utils.GameUtils;

import java.lang.reflect.Array;
import java.util.*;

public class KiAppstate extends BaseAppState {

    MultipleGameSessionAppState multipleGameSessionAppState;
    EntityData entityData;
    EntitySet entitySet;

    Map<EntityId, KiAction> kiActions = new HashMap<>();

    @Override
    public void initialize(AppStateManager stateManager) {

        multipleGameSessionAppState = stateManager.getState(MultipleGameSessionAppState.class);

        this.entityData = stateManager.getState(EntityDataState.class).getEntityData();
        entitySet = entityData.getEntities(PositionComponent.class, KIComponent.class, PlayerComponent.class);

        for(Entity entity : entitySet){
            //PositionComponent posCom = entity.get(PositionComponent.class);
            //KiAction kiAction = new KiAction(AStar.findPath(posCom, new PositionComponent(1, 5), entityData), true, 0);
            //kiActions.put(entity.getId(), kiAction);
        }
    }

    @Override
    public void update(float tpf){
        entitySet.applyChanges();
        for(Entity entity : entitySet){
            if(!kiActions.containsKey(entity.getId())){
                //Decide what to do next
            }
            //else if(kiInDanger) // Decide how to react
            else{ // do Action
                Path path = kiActions.get(entity.getId()).getPath();
                Move move =  path.getMove(path.getMoves().size() -1);
                PositionComponent nextCheckpoint = move.getPos();
                MoveDirection dir = move.getDir();
                PositionComponent pos = entity.get(PositionComponent.class);

                GameSession gameSession = multipleGameSessionAppState.getGameSession(entity.getId());

                boolean next = false;
                CollisionComponent colCom = entity.get(CollisionComponent.class);
                if(dir == MoveDirection.Right && pos.getX() >= nextCheckpoint.getX()){
                    next = true;
                }
                else if(dir == MoveDirection.Left && pos.getX() <= nextCheckpoint.getX()){
                    next = true;
                }
                else if(dir == MoveDirection.Down && pos.getY() >= nextCheckpoint.getY()){
                    next = true;
                }
                else if(dir == MoveDirection.Up && pos.getY() <= nextCheckpoint.getY()){
                    next = true;
                }

                if(next){
                    dir = MoveDirection.Idle;
                    entityData.setComponent(entity.getId(), move.getPos());
                    path.removeMove(move);

                    if(path.getMoves().isEmpty()){
                        if(kiActions.get(entity.getId()).isPlaceBomb()) gameSession.placeBomb();
                        kiActions.remove(entity.getId());
                    }
                }
                gameSession.applyMoveDirection(dir);
            }
        }
    }
}