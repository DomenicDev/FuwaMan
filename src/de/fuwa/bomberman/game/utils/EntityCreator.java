package de.fuwa.bomberman.game.utils;

import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.game.components.CollisionComponent;
import de.fuwa.bomberman.game.components.ModelComponent;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.components.WalkableComponent;
import de.fuwa.bomberman.game.enums.ModelType;
import de.fuwa.bomberman.game.enums.MoveDirection;

public class EntityCreator {

    public static EntityId createPlayer(EntityData entityData, float startX, float startY) {
        EntityId player = entityData.createEntity();
        entityData.setComponents(player,
                new PositionComponent(startX, startY),
                new CollisionComponent(0.20f, 0.5f, 0.6f, 0.45f, false),
                new ModelComponent(ModelType.Player, true),
                new WalkableComponent(MoveDirection.Idle, 2)
        );

        return player;
    }

    public static EntityId createBlock(EntityData entityData, float posX, float posY) {
        EntityId block = entityData.createEntity();
        entityData.setComponents(block,
                new PositionComponent(posX, posY),
                new CollisionComponent(0, 0, 1, 1, true),
                new ModelComponent(ModelType.UndestroyableTile, false)
                // todo add more
        );

        return block;
    }

}
