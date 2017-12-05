package de.fuwa.bomberman.game.appstates.Ki;

import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.game.components.BlockComponent;
import de.fuwa.bomberman.game.components.PositionComponent;

public class Tile{
    private PositionComponent pos;
    private boolean reachable;
    private boolean walkable;
    private boolean destructible;
    private boolean danger;
    private boolean powerUp;
    private EntityId player;
    private int destroyableBlocksFromThisPos;
    private int distanceFromPlayer;

    public Tile(PositionComponent pos, boolean reachable, boolean walkable, boolean destructible, boolean danger, boolean powerUp, EntityId player, int destroyableBlocksFromThisPos, int distanceFromPlayer) {
        this.pos = pos;
        this.reachable = reachable;
        this.walkable = walkable;
        this.destructible = destructible;
        this.danger = danger;
        this.powerUp = powerUp;
        this.player = player;
        this.destroyableBlocksFromThisPos = destroyableBlocksFromThisPos;
        this.distanceFromPlayer = distanceFromPlayer;
    }

    public Tile(PositionComponent pos){
        this.walkable = true;
        this.destructible = false;
        this.powerUp = false;
        this.danger = false;
        this.reachable = false;
        this.player = null;
        this.destroyableBlocksFromThisPos = 0;
        this.pos = pos;
    }

    public PositionComponent getPos() {
        return pos;
    }

    public void setPos(PositionComponent pos) {
        this.pos = pos;
    }

    public EntityId getPlayer() {
        return player;
    }

    public void setPlayer(EntityId player) {
        this.player = player;
    }

    public boolean isDanger() {
        return danger;
    }

    public void setDanger(boolean danger) {
        this.danger = danger;
    }

    public boolean isPowerUp() {
        return powerUp;
    }

    public void setPowerUp(boolean powerUp) {
        this.powerUp = powerUp;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }


    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public int getDestroyableBlocksFromThisPos() {
        return destroyableBlocksFromThisPos;
    }

    public void setDestroyableBlocksFromThisPos(int destroyableBlocksFromThisPos) {
        this.destroyableBlocksFromThisPos = destroyableBlocksFromThisPos;
    }

    public int getDistanceFromPlayer() {
        return distanceFromPlayer;
    }

    public void setDistanceFromPlayer(int distanceFromPlayer) {
        this.distanceFromPlayer = distanceFromPlayer;
    }
}
