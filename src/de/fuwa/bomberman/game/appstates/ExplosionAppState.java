package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.components.*;
import de.fuwa.bomberman.game.enums.ExplosionImpactType;
import de.fuwa.bomberman.game.enums.PowerUpType;
import de.fuwa.bomberman.game.utils.EntityCreator;
import de.fuwa.bomberman.game.utils.GameUtils;

import java.util.concurrent.ThreadLocalRandom;

public class ExplosionAppState extends BaseAppState {

    private EntitySet explosionEntities;
    private EntitySet entitySet;
    private EntityData entityData;
    private AppStateManager stateManager;

    @Override
    public void initialize(AppStateManager stateManager) {
        this.entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.explosionEntities = entityData.getEntities(ExplosionComponent.class, PositionComponent.class);
        this.entitySet = entityData.getEntities(PositionComponent.class, ExplosionImpactComponent.class);
        this.stateManager = stateManager;
    }

    @Override
    public void update(float tpf) {
        explosionEntities.applyChanges();
        for (Entity explosion : explosionEntities) {

            ExplosionComponent explosionComponent = explosion.get(ExplosionComponent.class);
            PositionComponent pos = explosion.get(PositionComponent.class);

            for (Entity entity : entitySet) {
                ExplosionImpactType explosionImpactType = entity.get(ExplosionImpactComponent.class).getExplosionImpactType();
                PositionComponent posCom = entity.get(PositionComponent.class);

                if (GameUtils.inSameCell(pos, posCom)) {
                    if (explosionImpactType == ExplosionImpactType.Disappear || explosionImpactType == ExplosionImpactType.DisappearAndStopExplosion) {
                        entityData.removeEntity(entity.getId());
                    } else if (explosionImpactType == ExplosionImpactType.Explode) {
                        BombComponent bomCom = entityData.getComponent(entity.getId(), BombComponent.class);
                        if (bomCom != null) {
                            entityData.setComponent(entity.getId(), new BombComponent(0, bomCom.getRadius(), bomCom.getCreator()));
                        }
                    }
                }
            }
            float timer = explosionComponent.getTimer();
            timer -= tpf;
            if (timer <= 0) {
                System.out.println("Explosion-End");
                entityData.removeEntity(explosion.getId());
            } else {
                entityData.setComponents(explosion.getId(), new ExplosionComponent(timer));
            }
        }
    }

    public void createExplosion(PositionComponent pos, int radius) {
        explosionEntities.applyChanges();
        entitySet.applyChanges();
        pos = GameUtils.getCellPosition(pos);

        EntityCreator.createExplosion(entityData, pos);

        int temp[][] = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j <= radius; j++) {

                int x = (int) (pos.getX() + j * temp[i][0]);
                int y = (int) (pos.getY() + j * temp[i][1]);

                boolean stop = false;
                for (Entity entity : entitySet) {
                    PositionComponent posCom = entity.get(PositionComponent.class);

                    if (GameUtils.inSameCell(new PositionComponent(x, y), posCom)) {
                        ExplosionImpactType explosionImpactType = entity.get(ExplosionImpactComponent.class).getExplosionImpactType();
                        if (explosionImpactType == ExplosionImpactType.StopExplosion) {
                            stop = true;
                        } else if (explosionImpactType == ExplosionImpactType.DisappearAndStopExplosion) {
                            DropPowerUpComponent droPowCom = entityData.getComponent(entity.getId(), DropPowerUpComponent.class);
                            if (droPowCom != null) {
                                addPotentialPowerUp(posCom);
                            }
                            entityData.removeEntity(entity.getId());
                            stop = true;
                        }
                    }
                }
                if (stop) break;
                else EntityCreator.createExplosion(entityData, x, y);
            }
        }
    }

    /**
     * Adds a power up with a fixed probability.
     *
     * @param posCom the position to create the power up at
     */
    private void addPotentialPowerUp(PositionComponent posCom) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 100);
        if (randomNum <= 10) {
            EntityCreator.createPowerUP(entityData, posCom.getX(), posCom.getY(), PowerUpType.SpeedUp);
        } else if (randomNum <= 20) {
            EntityCreator.createPowerUP(entityData, posCom.getX(), posCom.getY(), PowerUpType.BombStrengthUp);
        } else if (randomNum <= 30) {
            EntityCreator.createPowerUP(entityData, posCom.getX(), posCom.getY(), PowerUpType.BombAmountUp);
        }
    }

    @Override
    public void cleanup() {
        this.explosionEntities.close();
        this.explosionEntities.clear();
        this.explosionEntities = null;
    }
}
