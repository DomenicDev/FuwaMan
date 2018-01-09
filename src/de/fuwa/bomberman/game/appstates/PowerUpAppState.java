package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.components.PlayerComponent;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.components.PowerUpComponent;
import de.fuwa.bomberman.game.components.WalkableComponent;
import de.fuwa.bomberman.game.enums.PowerUpType;
import de.fuwa.bomberman.game.utils.GameUtils;

public class PowerUpAppState extends BaseAppState {

    private EntitySet powerups;
    private EntitySet players;
    private EntityData entityData;

    @Override
    public void initialize(AppStateManager stateManager) {
        this.entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.powerups = entityData.getEntities(PositionComponent.class, PowerUpComponent.class);
        this.players = entityData.getEntities(PositionComponent.class, PlayerComponent.class, WalkableComponent.class);
    }

    @Override
    public void update(float tpf) {
        powerups.applyChanges();
        players.applyChanges();

        for (Entity entity : powerups) {
            PositionComponent posUp = entity.get(PositionComponent.class);
            for (Entity entity2 : players) {
                PositionComponent posPlay = entity2.get(PositionComponent.class);
                if (GameUtils.inSameCell(posUp, posPlay)) {

                    PowerUpComponent powCom = entity.get(PowerUpComponent.class);
                    PowerUpType powerUpType = powCom.getPowerUpType();

                    PlayerComponent playCom = entity2.get(PlayerComponent.class);

                    if (powerUpType == powerUpType.SpeedUp) {
                        WalkableComponent walkCom = entity2.get(WalkableComponent.class);
                        float newSpeed = (float)Math.pow(1.05, -3 * walkCom.getSpeed());
                        entityData.setComponents(entity2.getId(), new PlayerComponent(playCom.getBombStrength(), playCom.getBombAmount()), new WalkableComponent(walkCom.getMoveDirection(), newSpeed));
                    } else if (powerUpType == powerUpType.BombStrengthUp) {
                        entityData.setComponent(entity2.getId(), new PlayerComponent(playCom.getBombStrength() + 1, playCom.getBombAmount()));
                    } else if (powerUpType == powerUpType.BombAmountUp) {
                        entityData.setComponent(entity2.getId(), new PlayerComponent(playCom.getBombStrength(), playCom.getBombAmount() + 1));
                    }
                    entityData.removeEntity(entity.getId());
                }
            }
        }
    }

    @Override
    public void cleanup() {
        this.players.close();
        this.players.clear();
        this.players = null;

        this.powerups.close();
        this.powerups.clear();
        this.powerups = null;
    }
}
