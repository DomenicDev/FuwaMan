package de.fuwa.bomberman.game.utils;

import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.game.components.*;
import de.fuwa.bomberman.game.enums.*;


public class EntityCreator {

    public static EntityId createPlayer(EntityData entityData, float startX, float startY, String name) {
        return createPlayer(entityData, null, startX, startY, name, false);
    }

    public static EntityId createPlayer(EntityData entityData, EntityId playerId, float startX, float startY, String name, boolean isKi) {
        EntityId player = playerId != null ? playerId : entityData.createEntity();
        entityData.setComponents(player,
                new PositionComponent(startX, startY),
                new CollisionComponent(0.15f, 0.5f, 0.7f, 0.45f, false),
                new ModelComponent(ModelType.Player, true),
                new NameComponent(name),
                new WalkableComponent(MoveDirection.Idle, 2),
                new PlayerComponent(1, 1),
                new ExplosionImpactComponent(ExplosionImpactType.Disappear)
        );

        if (isKi) {
            entityData.setComponent(playerId, new KIComponent());
        }

        return player;
    }

    public static EntityId createBlock(EntityData entityData, float posX, float posY, boolean destroyable) {
        EntityId block = entityData.createEntity();
        ModelType modelType;
        BlockType blockType;
        ExplosionImpactType explosionImpactType;

        if(destroyable) {
            modelType = ModelType.DestroyableTile;
            blockType = BlockType.Destroyable;
            explosionImpactType = ExplosionImpactType.DisappearAndStopExplosion;
        }
        else{
            modelType = ModelType.UndestroyableTile;
            blockType = BlockType.Undestroyable;
            explosionImpactType = ExplosionImpactType.StopExplosion;
        }

        entityData.setComponents(block,
                new PositionComponent(posX, posY),
                new CollisionComponent(0, 0, 1, 1, true),
                new ModelComponent(modelType, false),
                new BlockComponent(blockType),
                new ExplosionImpactComponent(explosionImpactType),
                new DropPowerUpComponent()
        );

        return block;
    }

    public static EntityId createPowerUP(EntityData entityData, float posX, float posY, PowerUpType powerUpType){
        EntityId powerUp = entityData.createEntity();
        ModelType modelType;

        if(powerUpType == PowerUpType.SpeedUp) modelType = ModelType.SpeedUp;
        else if(powerUpType == PowerUpType.BombStrengthUp) modelType = ModelType.BombStrengthUp;
        else modelType = ModelType.BombAmountUp;

        entityData.setComponents(powerUp,
                new PositionComponent(posX, posY),
                new ModelComponent(modelType , false),
                new PowerUpComponent(powerUpType),
                new ExplosionImpactComponent(ExplosionImpactType.Disappear)
        );

        return  powerUp;
    }
    public static EntityId createBomb(EntityData entityData, PositionComponent pos, int strength, EntityId creator){
        EntityId bombEntity = entityData.createEntity();
        entityData.setComponents(bombEntity,
                pos,
                new BombComponent(2.5f, strength, creator),
                new ModelComponent(ModelType.Bomb,false),
                new ExplosionImpactComponent(ExplosionImpactType.Explode)
        );
        return bombEntity;
    }

    public static EntityId createBomb(EntityData entityData, int x, int y, int strength, EntityId creator){
        EntityId bomb = entityData.createEntity();

        entityData.setComponents(bomb,
                new PositionComponent(x, y),
                //new CollisionComponent(0,0,1,1,true ),
                new ModelComponent(ModelType.Bomb, false),
                new BombComponent(5, strength, creator),
                new ExplosionImpactComponent(ExplosionImpactType.Explode)
        );

        return bomb;
    }
    public static EntityId createExplosion(EntityData entityData, PositionComponent pos){
        pos = GameUtils.getCellPosition(pos);
        return createExplosion(entityData,(int)pos.getX(), (int)pos.getY());
    }

    public static EntityId createExplosion(EntityData entityData, int centreX, int centreY){
        EntityId explosion = entityData.createEntity();

        entityData.setComponents(explosion,
                new PositionComponent(centreX,centreY),
                new ModelComponent(ModelType.Explosion, false),
                new ExplosionComponent(1f)
        );
        return explosion;
    }

}
