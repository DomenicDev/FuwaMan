package de.fuwa.bomberman.game.utils;

import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.enums.BlockType;

public class GameUtils {

    /**
     * Returns true if the specified positions are logically in the same cell.
     *
     * @param p1 the first position
     * @param p2 the second position
     * @return true if the positions are refering to the same logical cell
     */
    public static boolean inSameCell(PositionComponent p1, PositionComponent p2) {
        int x1 = Math.round(p1.getX());
        int x2 = Math.round(p2.getX());
        int y1 = Math.round(p1.getY());
        int y2 = Math.round(p2.getY());
        return x1 == x2 && y1 == y2;
    }

    public static GameField createSimpleGameField() {
        int width = 11;
        int height = 11;

        GameField gameField = new GameField(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y == 0 || x == 0 || y == height - 1 || x == width - 1 || (x % 2 == 0 && y % 2 == 0)) {
                    gameField.setBlock(x, y, BlockType.Undestroyable);
                }
            }
        }
        return gameField;
    }

    public static GameField createComplexGameField(){
        int width = 14;
        int height = 4;
        width = Math.max(11,width);
        height = Math.max(11,height);
        if(width%2==0)
            width++;
        if(height%2==0)
            height++;

        GameField gameField = new GameField(width, height);

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(x==0||y==0||x==width-1||y==height-1||x%2==0&&y%2==0){
                    gameField.setBlock(x, y, BlockType.Undestroyable);
                }else if(!( x == 1 && y == 1 || x == 2 && y == 1 || x == 1 && y == 2 ||
                        ( x == width - 2 && y == 1 || x == width - 3 && y == 1 || x == width - 2 && y == 2)||
                        ( x == 1 && y == height - 3 || x == 1 && y == height - 2 || x == 2 && y == height - 2)||
                        ( x == width - 2 && y == height - 2 || x == width - 3 && y == height - 2 || x == width - 2 && y == height - 3))){
                    gameField.setBlock(x,y,BlockType.Destroyable);
                }
            }
        }

        return gameField;
    }
    public static PositionComponent getCellPosition(PositionComponent pos){
        int x = Math.round(pos.getX());
        int y = Math.round(pos.getY());
        return new PositionComponent(x,y);
    }
}
