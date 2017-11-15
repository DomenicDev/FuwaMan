package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.components.WalkableComponent;
import de.fuwa.bomberman.game.enums.MoveDirection;

public class SimpleMovementAppState extends BaseAppState {

    private EntitySet movables;
    private EntityData entityData;

    @Override
    public void initialize(AppStateManager stateManager) {
        this.entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.movables = entityData.getEntities(PositionComponent.class, WalkableComponent.class);
    }

    @Override
    public void update(float tpf) {
       movables.applyChanges();

       for (Entity entity : movables) {
           PositionComponent pos = entity.get(PositionComponent.class);
           WalkableComponent walkComp = entity.get(WalkableComponent.class);
           MoveDirection moveDir = walkComp.getMoveDirection();

           if (moveDir == MoveDirection.Up) {
               entityData.setComponent(entity.getId(), new PositionComponent(pos.getX(), pos.getY() - (tpf * walkComp.getSpeed())));
           } else if (moveDir == MoveDirection.Down) {
               entityData.setComponent(entity.getId(), new PositionComponent(pos.getX(), pos.getY() + (tpf * walkComp.getSpeed())));
           } else if (moveDir == MoveDirection.Left) {
               entityData.setComponent(entity.getId(), new PositionComponent(pos.getX() - (tpf*walkComp.getSpeed()), pos.getY()));
           } else if (moveDir == MoveDirection.Right) {
               entityData.setComponent(entity.getId(), new PositionComponent(pos.getX() + (tpf*walkComp.getSpeed()), pos.getY()));
           }
       }
    }
}
