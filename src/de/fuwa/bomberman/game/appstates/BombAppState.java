package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.components.BombComponent;
import de.fuwa.bomberman.game.components.PlayerComponent;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.session.GameSession;
import de.fuwa.bomberman.game.utils.EntityCreator;
import de.fuwa.bomberman.game.utils.GameUtils;

public class BombAppState extends BaseAppState {

    private EntitySet bombEntities;
    private EntityData entityData;
    private GameSession gameSession;
    private ExplosionAppState explosionAppState;

    @Override
    public void initialize(AppStateManager stateManager) {
        this.entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.bombEntities = entityData.getEntities(BombComponent.class, PositionComponent.class);
        this.explosionAppState = stateManager.getState(ExplosionAppState.class);
    }

    @Override
    public void update(float tpf){
        bombEntities.applyChanges();
        for(Entity bomb : bombEntities){
            BombComponent bombComponent = bomb.get(BombComponent.class);
            float timer = bombComponent.getTimer();
            timer -= tpf;
            if(timer <= 0){
                System.out.println("Boom!");
                explosionAppState.createExplosion(bomb.get(PositionComponent.class), bombComponent.getRadius());
                entityData.removeEntity(bomb.getId());
            }else{
                entityData.setComponents(bomb.getId(),new BombComponent(timer, bombComponent.getRadius(), bombComponent.getCreator()));
            }
        }
    }

    public void placeBomb(PositionComponent position, int strength, PlayerComponent playCom, EntityId creator){
        bombEntities.applyChanges();
        position = GameUtils.getCellPosition(position);
        int maxBombAmount = playCom.getBombAmount();
        int currentBombAmount = 0;
        for(Entity bomb : bombEntities){
            PositionComponent bombPos = bomb.get(PositionComponent.class);
            if(GameUtils.inSameCell(bombPos,position)){
                return;
            }
            if(creator == bomb.get(BombComponent.class).getCreator()){
                currentBombAmount++;
            }
        }
        if(currentBombAmount < maxBombAmount){
            EntityCreator.createBomb(entityData, position, strength, creator);
        }
    }

    @Override
    public void cleanup() {
        this.bombEntities.close();
        this.bombEntities.clear();
        this.bombEntities = null;
    }
}
