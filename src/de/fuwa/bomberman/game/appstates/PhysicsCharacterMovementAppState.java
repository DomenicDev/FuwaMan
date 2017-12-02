package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.components.BombComponent;
import de.fuwa.bomberman.game.components.CollisionComponent;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.components.WalkableComponent;
import de.fuwa.bomberman.game.enums.MoveDirection;
import de.fuwa.bomberman.game.utils.GameUtils;

import java.util.HashMap;
import java.util.Map;

public class PhysicsCharacterMovementAppState extends BaseAppState {

    private EntitySet characters;
    private EntitySet physicalObjects;
    private EntitySet bombEntities;
    private EntityData entityData;

    // this map stores the character id with the bomb id it is in right now
    private Map<EntityId, EntityId> playerOnBomb = new HashMap<>();

    @Override
    public void initialize(AppStateManager stateManager) {
        this.entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.characters = entityData.getEntities(PositionComponent.class, WalkableComponent.class, CollisionComponent.class);
        this.physicalObjects = entityData.getEntities(PositionComponent.class, CollisionComponent.class);
        this.bombEntities = entityData.getEntities(PositionComponent.class, CollisionComponent.class, BombComponent.class);
    }

    @Override
    public void update(float tpf) {
        physicalObjects.applyChanges();
        characters.applyChanges();
        // we have to check if there are some characters
        // at the position of newly placed bombs
        // if so we have to ignore collisions
        // until the character walked out of the shape
        if (bombEntities.applyChanges()) {
            for (Entity bomb : bombEntities.getAddedEntities()) {
                PositionComponent bombPos = bomb.get(PositionComponent.class);
                for (Entity character : characters) {
                    PositionComponent charPos = character.get(PositionComponent.class);
                    if (GameUtils.inSameCell(charPos, bombPos)) {
                        playerOnBomb.put(character.getId(), bomb.getId());
                    }
                }
            }
        }

        for (Entity character : characters) {

            WalkableComponent walkComponent = character.get(WalkableComponent.class);
            if (walkComponent.getMoveDirection() == MoveDirection.Idle) {
                continue;
            }

            PositionComponent playerPos = character.get(PositionComponent.class);
            CollisionComponent playerCollisionShape = character.get(CollisionComponent.class);

            RectangleFloat r1 = new RectangleFloat();
            RectangleFloat r2 = new RectangleFloat();

            // this boolean will be set to true
            // if we collide with something
            boolean collided = false;

            // we calculate the new position for this entity but we do not apply it yet
            // we first need to check if this would intersect with another collision shape
            PositionComponent newPos = computeNewPosition(playerPos, walkComponent, tpf);

            // we prepare our rectangle with the new potential position
            prepareRectangle(r1, newPos, playerCollisionShape);

            // for now we go through each physical object and check
            // if it would collide with the newly created collision shape rectangle
            for (Entity physicalObject : physicalObjects) {
                // we do not need to check the same entity
                if (character.getId().equals(physicalObject.getId())) {
                    continue;
                }
                // get the relevant information of this physical entity
                PositionComponent physicalPos = physicalObject.get(PositionComponent.class);
                CollisionComponent physicalCollision = physicalObject.get(CollisionComponent.class);

                // we only want to collide with static objects like bombs or blocks
                if (!physicalCollision.isStaticObject()) {
                    continue;
                }

                // we prepare the rectangle of the other physical object
                prepareRectangle(r2, physicalPos, physicalCollision);


                // now we check if they intersect
                // if they do we have a collision
                if (areColliding(r1, r2)) {
                    // if we collide we check if this character
                    // is still "in" a bomb
                    // if so, we have to ignore this collision
                    if (playerOnBomb.containsKey(character.getId())) {
                        if (playerOnBomb.get(character.getId()).equals(physicalObject.getId())) {
                            continue;
                        }
                    }
                    // we collided so we do not do everything
                    // means we do not apply the new calculated position
                    collided = true;
                    break;
                } else {
                    // in the case we did not collide
                    // and the player is still contained in
                    // the bomb list, we remove the character from the list
                    if (playerOnBomb.containsKey(character.getId())) {
                        if (playerOnBomb.get(character.getId()).equals(physicalObject.getId())) {
                            playerOnBomb.remove(character.getId()); // remove it from the list
                            collided = false; // just to make sure the value is set to false
                            break; // we break out
                        }
                    }
                }

            }

            if (!collided) {
                // we did not collide with something
                // so we apply the new calculated position
                entityData.setComponent(character.getId(), newPos);
            }

        }

    }

    private PositionComponent computeNewPosition(PositionComponent pos, WalkableComponent walkComponent, float tpf) {
        MoveDirection moveDir = walkComponent.getMoveDirection();
        float speed = walkComponent.getSpeed();

        float newXPos = pos.getX();
        float newYPos = pos.getY();

        if (moveDir == MoveDirection.Up) {
            newYPos -= (tpf * speed);
        } else if (moveDir == MoveDirection.Down) {
            newYPos += (tpf * speed);
        } else if (moveDir == MoveDirection.Left) {
            newXPos -= (tpf * speed);
        } else if (moveDir == MoveDirection.Right) {
            newXPos += (tpf * speed);
        }
        return new PositionComponent(newXPos, newYPos);
    }

    private void prepareRectangle(RectangleFloat r, PositionComponent pos, CollisionComponent collision) {
        float x = pos.getX() + collision.getOffsetX();
        float y = pos.getY() + collision.getOffsetY();
        r.setBounds(x, y, collision.getWidth(), collision.getHeight());
    }

    private boolean areColliding(RectangleFloat r1, RectangleFloat r2) {
        return r1.x < r2.x + r2.width &&
                r1.x + r1.width > r2.x &&
                r1.y < r2.y + r2.height &&
                r1.y + r1.height > r2.y;
    }

    private class RectangleFloat {

        private float x, y, width, height;

        private void setBounds(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    @Override
    public void cleanup() {
        this.characters.close();
        this.characters.clear();
        this.characters = null;

        this.physicalObjects.close();
        this.physicalObjects.clear();
        this.physicalObjects = null;
    }
}
