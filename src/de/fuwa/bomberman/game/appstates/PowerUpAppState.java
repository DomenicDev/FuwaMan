package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.components.*;
import de.fuwa.bomberman.game.enums.PowerUpType;
import de.fuwa.bomberman.game.utils.GameUtils;
import javafx.geometry.Pos;

public class PowerUpAppState extends BaseAppState{

    EntitySet powerups;
    EntitySet players;
    EntityData entityData;

    @Override
    public void initialize(AppStateManager stateManager) {
        entityData = stateManager.getState(EntityDataState.class).getEntityData();
        powerups = entityData.getEntities(PositionComponent.class, PowerUpComponent.class);
        players = entityData.getEntities(PositionComponent.class, PlayerComponent.class, WalkableComponent.class);
    }

    @Override
    public void update(float tpf) {
        powerups.applyChanges();
        players.applyChanges();

        for(Entity entity : powerups){
            PositionComponent posUp = entity.get(PositionComponent.class);
            for(Entity entity2 : players){
                PositionComponent posPlay = entity2.get(PositionComponent.class);
                if(GameUtils.inSameCell(posUp, posPlay)){

                    PowerUpComponent powCom = entity.get(PowerUpComponent.class);
                    PowerUpType powerUpType = powCom.getPowerUpType();

                    PlayerComponent playCom = entity2.get(PlayerComponent.class);
                    WalkableComponent walkCom = entity2.get(WalkableComponent.class);

                    if(powerUpType == powerUpType.SpeedUp){
                        entityData.setComponents(entity2.getId(), new PlayerComponent( playCom.getBombStrength(), playCom.getBombAmount()), new WalkableComponent(walkCom.getMoveDirection(), walkCom.getSpeed() + 1));
                    }
                    else if(powerUpType == powerUpType.BombStrengthUp){
                        entityData.setComponent(entity2.getId(), new PlayerComponent( playCom.getBombStrength() + 1, playCom.getBombAmount()));
                    }
                    else if(powerUpType == powerUpType.BombAmountUp){
                        entityData.setComponent(entity2.getId(), new PlayerComponent(playCom.getBombStrength(), playCom.getBombAmount() + 1));
                    }
                    entityData.removeComponent(entity.getId(), PositionComponent.class);
                    entityData.removeComponent(entity.getId(), PowerUpComponent.class);
                    entityData.removeComponent(entity.getId(), ModelComponent.class);
                    entityData.removeEntity(entity.getId());
                }
            }
        }
    }
}
