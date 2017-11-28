package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.components.ExplosionComponent;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.utils.EntityCreator;
import de.fuwa.bomberman.game.utils.GameUtils;

import javax.swing.text.Position;

public class ExplosionAppState extends BaseAppState{

    private EntitySet explosionEntities;
    private EntityData entityData;
    @Override
    public void initialize(AppStateManager stateManager) {
        this.entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.explosionEntities = entityData.getEntities(ExplosionComponent.class, PositionComponent.class);
    }
    @Override
    public void update(float tpf) {
        explosionEntities.applyChanges();
        for(Entity explosion : explosionEntities){
            ExplosionComponent explosionComponent = explosion.get(ExplosionComponent.class);
            float timer = explosionComponent.getTimer();
            timer -=tpf;
            if(timer<=0){
                System.out.println("Explosion-End");
                entityData.removeEntity(explosion.getId());
            }else{
                entityData.setComponents(explosion.getId(), new ExplosionComponent(timer));
            }
        }
    }
    public void createExplosion(PositionComponent pos){
        explosionEntities.applyChanges();
        pos = GameUtils.getCellPosition(pos);

        EntityCreator.createExplosion(entityData,pos);
    }
    @Override
    public void cleanup() {
        this.explosionEntities.close();
        this.explosionEntities.clear();
        this.explosionEntities = null;
    }
}
