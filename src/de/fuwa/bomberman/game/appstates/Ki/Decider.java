package de.fuwa.bomberman.game.appstates.Ki;

import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.components.*;
import de.fuwa.bomberman.game.enums.BlockType;
import de.fuwa.bomberman.game.enums.ExplosionImpactType;
import de.fuwa.bomberman.game.utils.GameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Decider {

    public static KiAction decideNextAction(Tile[][] playermap, EntityData entityData, Entity entity, EntitySet bombs, int MapSizeX, int MapSizeY){

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
        PositionComponent newPos;
        boolean placeBomb;
        int waitTime;
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
            newPos = pos;
            placeBomb = false;
            waitTime = 1;
        }
        else if(curBombs >= maxBombs){
            if(bestChanceToPickUpPowerUp > 0){
                newPos = decideAction(chancesToPickUpPowerUp);
                placeBomb = false;
                waitTime = 0;
            }
            else {
                newPos = pos;
                placeBomb = false;
                waitTime = 1;
            }
        }
        else if(randomNum <= bestChanceToDestroyBlock){// Destroy Block
            newPos = decideAction(chancesToDestroyBlock);
            placeBomb = true;
            waitTime = 0;
        }
        else if(randomNum <= bestChanceToDestroyBlock + bestChanceToPickUpPowerUp){ // PickUp PowerUp
            newPos = decideAction(chancesToPickUpPowerUp);
            placeBomb = false;
            waitTime = 0;
        }
        else if(randomNum <= bestChanceToDestroyBlock + bestChanceToPickUpPowerUp + bestChanceToPlaceBombNearPlayer){ //Place a bomb next to an player
            newPos = decideAction(chancesToPlaceBombNearPlayer);
            placeBomb = true;
            waitTime = 0;
        }
        else {
            newPos = pos;
            placeBomb = false;
            waitTime = 1;
        }
        kiAction = new KiAction(AStar.findPath(pos, newPos, entityData, playermap, false, MapSizeX, MapSizeY), placeBomb, waitTime);
        return kiAction;
    }

    private static PositionComponent decideAction(List<KiActionChance> chances){
        float sum = 0;
        float end = 0;
        float random = ThreadLocalRandom.current().nextFloat();
        for(KiActionChance kiActionChance : chances){
            sum += kiActionChance.getChance();
        }
        for(KiActionChance kiActionChance : chances){
            kiActionChance.setChance(kiActionChance.getChance() / sum);
            if(end+kiActionChance.getChance() >= random){
                return  kiActionChance.getTile().getPos();
            }
            else end += kiActionChance.getChance();
        }
        return null;
    }
}
