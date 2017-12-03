package de.fuwa.bomberman.game.appstates.Ki;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.appstates.LogicalGameFieldAppState;
import de.fuwa.bomberman.game.appstates.MainGameAppState;
import de.fuwa.bomberman.game.appstates.PhysicsCharacterMovementAppState;
import de.fuwa.bomberman.game.appstates.session.GameSessionAppState;
import de.fuwa.bomberman.game.appstates.session.MultipleGameSessionAppState;
import de.fuwa.bomberman.game.components.*;
import de.fuwa.bomberman.game.enums.BlockType;
import de.fuwa.bomberman.game.enums.ExplosionImpactType;
import de.fuwa.bomberman.game.enums.KiActionType;
import de.fuwa.bomberman.game.enums.MoveDirection;
import de.fuwa.bomberman.game.session.GameSession;
import de.fuwa.bomberman.game.utils.EntityCreator;
import de.fuwa.bomberman.game.utils.GameUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class KiAppstate extends BaseAppState {

    int MapSizeX;
    int MapSizeY;

    MultipleGameSessionAppState multipleGameSessionAppState;
    EntityData entityData;
    EntitySet entitySet;
    EntitySet entities;
    EntitySet bombs;
    EntitySet explosions;

    Map<EntityId, KiAction> kiActions = new HashMap<>();
    Map<EntityId, Float> kiWaitTimes = new HashMap<>();
    Map<EntityId, Boolean> kidone = new HashMap<>();

    Map<EntityId, List<EntityId>> playerOnBomb = new HashMap<>();

    Tile[][] map;

    @Override
    public void initialize(AppStateManager stateManager) {

        multipleGameSessionAppState = stateManager.getState(MultipleGameSessionAppState.class);

        this.entityData = stateManager.getState(EntityDataState.class).getEntityData();
        entitySet = entityData.getEntities(PositionComponent.class, KIComponent.class, PlayerComponent.class, CollisionComponent.class);
        entities = entityData.getEntities(PositionComponent.class);
        bombs = entityData.getEntities(BombComponent.class);
        explosions = entityData.getEntities(PositionComponent.class, ExplosionImpactComponent.class);

        LogicalGameFieldAppState logicalGameFieldAppState = stateManager.getState(LogicalGameFieldAppState.class);
        MapSizeX = logicalGameFieldAppState.getGameFieldSizeX();
        MapSizeY = logicalGameFieldAppState.getGameFieldSizeY();

        PhysicsCharacterMovementAppState physicsCharacterMovementAppState = stateManager.getState(PhysicsCharacterMovementAppState.class);
        playerOnBomb = physicsCharacterMovementAppState.getPlayerOnBomb();

        for(Entity entity : entitySet){
            kiWaitTimes.put(entity.getId(), 0.f);
            kidone.put(entity.getId(), false);
        }

    }

    @Override
    public void update(float tpf){
        entitySet.applyChanges();
        entities.applyChanges();
        bombs.applyChanges();
        explosions.applyChanges();

        for(Entity entity : entitySet){
            createMap();
            Tile[][] playermap = createPlayerSpecMap(entity);

            boolean force = false;
            KiAction kiAction = kiActions.get(entity.getId());

            kiWaitTimes.put(entity.getId(), kiWaitTimes.get(entity.getId()) - tpf);
            if(inDanger(entity, playermap)){
                if(kidone.get(entity.getId())){
                    walkToSafety(entity, playermap);
                    kidone.put(entity.getId(), false);
                }
                else{
                    if(kiAction != null){
                        Path path = kiAction.getPath();
                        for(int i = 0; i < path.getMoves().size()-1; i++){
                            path.removeMove(path.getMove(i));
                        }
                    }
                }
                force = true;
            }
            else if(kiAction == null){
                decideNextAction(entity, playermap);
                kidone.put(entity.getId(), false);
            }

            kiAction = kiActions.get(entity.getId());
            if(force){
                doAction(entity, kiAction);
            }
            else if(kiWaitTimes.get(entity.getId()) <= 0){
                doAction(entity, kiAction);
            }
        }
    }

    private void walkToSafety(Entity entity, Tile[][] playermap){

        PositionComponent pos = entity.get(PositionComponent.class);
        int posX = (int)pos.getX(), posY = (int)pos.getY(), distance = 1000;
        for(int y = 0; y < MapSizeY; y++){
            for(int x = 0; x < MapSizeX; x++){
                if(playermap[x][y].isReachable() && !playermap[x][y].isDanger()){
                    if(distance > playermap[x][y].getDistanceFromPlayer()){
                        posX = x;
                        posY = y;
                        distance = playermap[x][y].getDistanceFromPlayer();
                    }
                }
            }
        }
        KiAction kiAction = new KiAction(AStar.findPath(pos, new PositionComponent(posX, posY), entityData, playermap, true, MapSizeX, MapSizeY), false, 0);
        kiActions.put(entity.getId(), kiAction);
    }

    public boolean inDanger(Entity entity, Tile[][] playermap){
        KiAction kiAction = kiActions.get(entity.getId());
        if(kiAction != null){
            Path path = kiAction.getPath();
            for(Move move : path.getMoves()){
                if(playermap[(int)move.getPos().getX()][(int)move.getPos().getY()].isDanger()) return true;
            }
        }
        PositionComponent pos = entity.get(PositionComponent.class);
        if(playermap[(int)pos.getX()][(int)pos.getY()].isDanger()) return true;
        return false;
    }

    public void doAction(Entity entity, KiAction kiAction){
        // Do a KiAction until end is reached
        Path path = kiAction.getPath();
        if(path.getMoves().size() > 0){
            Move move =  path.getMove(path.getMoves().size() -1);
            PositionComponent nextCheckpoint = move.getPos();
            MoveDirection dir = move.getDir();
            PositionComponent pos = entity.get(PositionComponent.class);

            GameSession gameSession = multipleGameSessionAppState.getGameSession(entity.getId());

            //Do next move if x or y Pos reached the next checkpoint
            boolean next = false;
            if(dir == MoveDirection.Right && pos.getX() >= nextCheckpoint.getX()){
                next = true;
            }
            else if(dir == MoveDirection.Left && pos.getX() <= nextCheckpoint.getX()){
                next = true;
            }
            else if(dir == MoveDirection.Down && pos.getY() >= nextCheckpoint.getY()){
                next = true;
            }
            else if(dir == MoveDirection.Up && pos.getY() <= nextCheckpoint.getY()) {
                next = true;
            }
            //Remove checkpoint which was reached and start moving to the next checkpoint. If there are no more checkpoint left: place a bomb and remove the KiAction
            if(next){
                dir = MoveDirection.Idle;
                entityData.setComponent(entity.getId(), move.getPos());
                entitySet.applyChanges();
                path.removeMove(move);

                if(path.getMoves().isEmpty()){
                    if(kiActions.get(entity.getId()).isPlaceBomb()) {
                        PlayerComponent playCom = entity.get(PlayerComponent.class);
                        boolean placeBomb =  addTestBomb(move.getPos(), playCom.getBombStrength(), entity);
                        if(placeBomb) gameSession.placeBomb();
                    }
                    kiWaitTimes.put(entity.getId(), kiAction.getWaitTime());
                    kidone.put(entity.getId(), true);
                    kiActions.remove(entity.getId());
                }
            }
            gameSession.applyMoveDirection(dir);
        }
        else {
            kiWaitTimes.put(entity.getId(), kiAction.getWaitTime());
            kidone.put(entity.getId(), true);
            kiActions.remove(entity.getId());
        }
    }

    public boolean addTestBomb(PositionComponent pos, int strength, Entity entity){
        Tile[][] playermap = createPlayerSpecMap(entity);

        List<PositionComponent> dangers = new ArrayList<>();
        dangers.add(pos);

        int temp[][] = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j <= strength; j++) {

                int x = (int) (pos.getX() + j * temp[i][0]);
                int y = (int) (pos.getY() + j * temp[i][1]);

                boolean stop = false;
                for (Entity explosion : explosions) {
                    PositionComponent posCom = explosion.get(PositionComponent.class);

                    if (GameUtils.inSameCell(new PositionComponent(x, y), posCom)) {
                        ExplosionImpactType explosionImpactType = explosion.get(ExplosionImpactComponent.class).getExplosionImpactType();
                        if (explosionImpactType == ExplosionImpactType.StopExplosion) {
                            stop = true;
                        } else if (explosionImpactType == ExplosionImpactType.DisappearAndStopExplosion) {
                            stop = true;
                        }
                    }
                }
                if (stop) break;
                else {
                    dangers.add(new PositionComponent(x, y));
                }
            }
        }
        PositionComponent destination = null;
        int distance = 1000;

        for(int y = 0; y < MapSizeY; y++) {
            for (int x = 0; x < MapSizeX; x++) {
                if (!playermap[x][y].isDanger() && playermap[x][y].isReachable()) {
                    boolean stop = false;
                    for(PositionComponent danger : dangers){
                        if((int)danger.getX() == x && (int)danger.getY() == y){
                            stop = true;
                            break;
                        }
                    }
                    if(!stop){
                        if(playermap[x][y].getDistanceFromPlayer() < distance){
                            destination = new PositionComponent(x, y);
                        }
                    }
                }
            }
        }
        if(destination != null){
            KiAction kiAction = new KiAction(AStar.findPath(pos, destination, entityData, playermap, false, MapSizeX, MapSizeY), false, 0);
            if(kiAction.getPath().getMoves().size() > 0){
                kiActions.put(entity.getId(), kiAction);
                return true;
            }
        }
        return false;
    }

    public void decideNextAction(Entity entity, Tile[][] playermap){

        float bestChanceToDestroyBlock = 0;
        float bestChanceToPickUpPowerUp = 0;
        float bestChanceToPlaceBombNearPlayer = 0;

        List<KiActionChance> chancesToDestroyBlock = new ArrayList<>();
        List<KiActionChance> chancesToPickUpPowerUp = new ArrayList<>();
        List<KiActionChance> chancesToPlaceBombNearPlayer = new ArrayList<>();

        // Find the best move for each Action above
        for(int y = 0; y < MapSizeY; y++){
            for(int x = 0; x < MapSizeX; x++){
                if(playermap[x][y].isReachable()){
                    float temp;
                    float distance = playermap[x][y].getDistanceFromPlayer();
                    if(distance == 0) distance = 0.5f;
                    //chanceToDestroyBlock
                        if(distance < 5){
                            temp = playermap[x][y].getDestroyableBlocksFromThisPos() / distance;
                            chancesToDestroyBlock.add(new KiActionChance(playermap[x][y], temp));
                            if(bestChanceToDestroyBlock < temp) bestChanceToDestroyBlock = temp;
                        }

                    //chanceToPickUpPowerUp
                    if(distance < 10){
                        if(playermap[x][y].isPowerUp()){
                            temp = 10 / distance;
                            chancesToPickUpPowerUp.add(new KiActionChance(playermap[x][y], temp));
                            if(bestChanceToPickUpPowerUp < temp) bestChanceToPickUpPowerUp = temp;
                        }
                    }

                    //chanceToPlaceBombNearPlayer
                    if(playermap[x][y].getPlayer() != null){
                        if(playermap[x][y].getPlayer().getId() != entity.getId().getId()){
                            temp = 10 / distance;
                            chancesToPlaceBombNearPlayer.add(new KiActionChance(playermap[x][y], temp));
                            if(bestChanceToPlaceBombNearPlayer < temp) bestChanceToPlaceBombNearPlayer = temp;
                        }
                    }
                }
            }
        }
        // change the float values of the chance to that if you take the sum of all values you get solution 1.0
        float divider = bestChanceToDestroyBlock + bestChanceToPickUpPowerUp + bestChanceToPlaceBombNearPlayer;
        bestChanceToDestroyBlock /= divider;
        bestChanceToPickUpPowerUp /= divider;
        bestChanceToPlaceBombNearPlayer /= divider;

        // Decide what of there 3 things to do trough random numbers. After that take look at all actions of this category and decide on what to do trough random numbers
        float randomNum = ThreadLocalRandom.current().nextFloat();

        Tile tile;
        KiAction kiAction;
        PositionComponent pos = entity.get(PositionComponent.class);
        PlayerComponent playCom = entity.get(PlayerComponent.class);


        int maxBombs = playCom.getBombAmount();
        int curBombs = 0;
        for(Entity bomb : bombs){
            BombComponent boCom = bomb.get(BombComponent.class);
            if(boCom.getCreator().getId() == entity.getId().getId()){
                curBombs++;
            }
        }
        if(bestChanceToDestroyBlock == 0.f && bestChanceToPickUpPowerUp == 0.f && bestChanceToPlaceBombNearPlayer == 0.f){
            kiAction = new KiAction(AStar.findPath(pos, pos, entityData, playermap,false, MapSizeX, MapSizeY), false, 1);
        }
        else if(curBombs >= maxBombs){
            if(bestChanceToPickUpPowerUp > 0){
                tile = decideAction(chancesToPickUpPowerUp);
                kiAction = new KiAction(AStar.findPath(pos, tile.getPos(), entityData, playermap, false, MapSizeX, MapSizeY), false, 0);
            }
            else {
                kiAction = new KiAction(AStar.findPath(pos, pos, entityData, playermap, false, MapSizeX, MapSizeY), false, 1);
            }
        }
        else if(randomNum <= bestChanceToDestroyBlock){// Destroy Block
            tile = decideAction(chancesToDestroyBlock);
            kiAction = new KiAction(AStar.findPath(pos, tile.getPos(), entityData, playermap,false, MapSizeX, MapSizeY), true, 0);
        }
        else if(randomNum <= bestChanceToDestroyBlock + bestChanceToPickUpPowerUp){ // PickUp PowerUp
            tile = decideAction(chancesToPickUpPowerUp);
            kiAction = new KiAction(AStar.findPath(pos, tile.getPos(), entityData, playermap, false, MapSizeX, MapSizeY), false, 0);
        }
        else if(randomNum <= bestChanceToDestroyBlock + bestChanceToPickUpPowerUp + bestChanceToPlaceBombNearPlayer){ //Place a bomb next to an player
            tile = decideAction(chancesToPlaceBombNearPlayer);
            kiAction = new KiAction(AStar.findPath(pos, tile.getPos(), entityData, playermap, false, MapSizeX, MapSizeY), true, 0);
        }
        else {
            kiAction = new KiAction(AStar.findPath(pos, pos, entityData, playermap, false, MapSizeX, MapSizeY), false, 1);
        }
        kiActions.put(entity.getId(), kiAction);
    }

    public Tile decideAction(List<KiActionChance> chances){
        float sum = 0;
        float end = 0;
        float random = ThreadLocalRandom.current().nextFloat();
        for(KiActionChance kiActionChance : chances){
            sum += kiActionChance.getChance();
        }
        for(KiActionChance kiActionChance : chances){
            kiActionChance.setChance(kiActionChance.getChance() / sum);
            if(end+kiActionChance.getChance() >= random){
                return  kiActionChance.getTile();
            }
            else end += kiActionChance.getChance();
        }
        return null;
    }

    public void createMap(){

        map = new Tile[MapSizeX][MapSizeY];
        for(int y = 0; y < MapSizeY; y++){
            for(int x = 0; x < MapSizeX; x++){
                map[x][y] = new Tile(new PositionComponent(x, y));
            }
        }

        // Set if the tiles are destructible, walkable, have an PowerUp or have an explosion
        for(Entity entity : entities){
            PositionComponent pos = entity.get(PositionComponent.class);
            PlayerComponent playCom = entityData.getComponent(entity.getId(), PlayerComponent.class);
            BlockComponent blockCom = entityData.getComponent(entity.getId(), BlockComponent.class);
            PowerUpComponent powCom = entityData.getComponent(entity.getId(), PowerUpComponent.class);
            ExplosionComponent exCom = entityData.getComponent(entity.getId(), ExplosionComponent.class);
            BombComponent boCom = entityData.getComponent(entity.getId(), BombComponent.class);

            if(playCom != null) map[(int)pos.getX()][(int)pos.getY()].setPlayer(entity.getId());
            else if(blockCom != null){
                map[(int)pos.getX()][(int)pos.getY()].setWalkable(false);
                if(blockCom.getBlockType() == BlockType.Destroyable) {
                    map[(int)pos.getX()][(int)pos.getY()].setDestructible(true);
                }
            }
            else if(powCom != null) map[(int)pos.getX()][(int)pos.getY()].setPowerUp(true);
            else if(exCom != null) map[(int)pos.getX()][(int)pos.getY()].setDanger(true);
            else if(boCom != null) {
                map[(int) pos.getX()][(int) pos.getY()].setWalkable(false);
                map[(int) pos.getX()][(int) pos.getY()].setDanger(true);

                int temp[][] = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
                int strength = boCom.getRadius();
                for (int i = 0; i < 4; i++) {
                    for (int j = 1; j <= strength; j++) {

                        int x = (int) (pos.getX() + j * temp[i][0]);
                        int y = (int) (pos.getY() + j * temp[i][1]);

                        boolean stop = false;
                        for (Entity explosion : explosions) {
                            PositionComponent posCom = explosion.get(PositionComponent.class);

                            if (GameUtils.inSameCell(new PositionComponent(x, y), posCom)) {
                                ExplosionImpactType explosionImpactType = explosion.get(ExplosionImpactComponent.class).getExplosionImpactType();
                                if (explosionImpactType == ExplosionImpactType.StopExplosion) {
                                    stop = true;
                                } else if (explosionImpactType == ExplosionImpactType.DisappearAndStopExplosion) {
                                    stop = true;
                                }
                            }
                        }
                        if (stop) break;
                        else {
                            map[x][y].setDanger(true);
                        }
                    }
                }
            }
        }
    }

    public Tile[][] createPlayerSpecMap(Entity player){
        // set if the tile is reachable and how many blocks could be destroyed if a bomb is placed on this tile trough recursion.
        Tile[][] playerMap = map.clone();
        PositionComponent pos = player.get(PositionComponent.class);
        PlayerComponent playCom = player.get(PlayerComponent.class);

        for(EntityId entityId : playerOnBomb.get(player.getId())){
            PositionComponent posCom = entityData.getComponent(entityId, PositionComponent.class);
            playerMap[(int)posCom.getX()][(int)posCom.getY()].setWalkable(true);
        }

        setIsReachableAndDestroyableBlocksFromThisPos((int)player.get(PositionComponent.class).getX(), (int)player.get(PositionComponent.class).getY(),0, 0, playerMap, playCom.getBombStrength());

        for(int y = 0; y < MapSizeY; y++){
            for(int x = 0; x < MapSizeX; x++){
                playerMap[x][y].setDistanceFromPlayer(Math.abs(x - (int)pos.getX()) + Math.abs(y - (int)pos.getY()));
            }
        }
        return playerMap;
    }

    public void setIsReachableAndDestroyableBlocksFromThisPos(int x, int y, int lastx, int lasty, Tile[][] map, int strenth){
        if(x < 0 || x >= MapSizeX || y < 0 || y >= MapSizeY) return;
        if(!map[x][y].isWalkable()) {
            if(map[x][y].isDestructible()){
                int difX = lastx-x, difY = lasty-y;
                int posX = lastx, posY = lasty;
                for(int i = 1; i <= strenth; i++){
                    if(posX < 0 || posX >= MapSizeX || posY < 0 || posY >= MapSizeY) break;
                    map[posX][posY].setDestroyableBlocksFromThisPos(map[lastx][lasty].getDestroyableBlocksFromThisPos()+1);
                    posX += difX;
                    posY += difY;
                }
                return;
            }
        }
        else if(map[x][y].isReachable()) return;
        else{
            map[x][y].setReachable(true);

            setIsReachableAndDestroyableBlocksFromThisPos(x+1, y, x, y, map, strenth);
            setIsReachableAndDestroyableBlocksFromThisPos(x-1, y, x, y, map, strenth);
            setIsReachableAndDestroyableBlocksFromThisPos(x, y+1, x, y, map, strenth);
            setIsReachableAndDestroyableBlocksFromThisPos(x, y-1, x, y, map, strenth);
        }
    }
}