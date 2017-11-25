package de.fuwa.bomberman.game.utils;

import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.game.components.*;
import de.fuwa.bomberman.game.enums.BlockType;
import de.fuwa.bomberman.game.enums.ModelType;
import de.fuwa.bomberman.game.enums.MoveDirection;
import de.fuwa.bomberman.game.enums.PowerUpType;


public class EntityCreator {

    public static EntityId createPlayer(EntityData entityData, float startX, float startY, String name) {
        return createPlayer(entityData, null, startX, startY, name, false);
    }

    public static EntityId createPlayer(EntityData entityData, EntityId playerId, float startX, float startY, String name, boolean isKi) {
        EntityId player = playerId != null ? playerId : entityData.createEntity();
        entityData.setComponents(player,
                new PositionComponent(startX, startY),
                new CollisionComponent(0.20f, 0.5f, 0.6f, 0.45f, false),
                new ModelComponent(ModelType.Player, true),
                new NameComponent(name),
                new WalkableComponent(MoveDirection.Idle, 2),
                new PlayerComponent(1, 1)
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

        if(destroyable) {
            modelType = ModelType.DestroyableTile;
            blockType = BlockType.Destroyable;
        }
        else{
            modelType = ModelType.UndestroyableTile;
            blockType = BlockType.Undestroyable;
        }

        entityData.setComponents(block,
                new PositionComponent(posX, posY),
                new CollisionComponent(0, 0, 1, 1, true),
                new ModelComponent(modelType, false),
                new BlockComponent(blockType)
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
                new PowerUpComponent(powerUpType)
        );

        return  powerUp;
    }
    public static EntityId createBomb(EntityData entityData, PositionComponent pos){
        EntityId bombEntity = entityData.createEntity();
        entityData.setComponents(bombEntity, pos, new BombComponent(5, 2), new ModelComponent(ModelType.Bomb,false));
        return bombEntity;
    }

    public static EntityId createBomb(EntityData entityData, int x, int y, int strength){
        EntityId bomb = entityData.createEntity();

        entityData.setComponents(bomb,
                new PositionComponent(x, y),
                //new CollisionComponent(0,0,1,1,true ),
                new ModelComponent(ModelType.Bomb, false),
                new BombComponent(5, strength)
        );

        return bomb;
    }
}
