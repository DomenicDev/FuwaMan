package de.fuwa.bomberman.game.appstates.Ki;

import de.fuwa.bomberman.game.components.PositionComponent;

import java.util.List;

public class Node {

    private PositionComponent pos;
    private boolean walkable;
    private int g;
    private int h;
    private Node Previous;

    public Node(PositionComponent pos, boolean walkable, int h) {
        this.pos = pos;
        this.walkable = walkable;
        this.h = h;
    }

    public PositionComponent getPos() {
        return pos;
    }

    public void setPos(PositionComponent pos) {
        this.pos = pos;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public int getH() {
        return h;
    }

    public void setH(Node node) {
        setH(Math.abs((int)pos.getX() - (int)node.getPos().getX())
                + Math.abs((int)pos.getY() - (int)node.getPos().getY()));
    }

    public void setH(int h){
        this.h = h;
    }

    public Node getPrevious() {
        return Previous;
    }

    public void setPrevious(Node previous) {
        Previous = previous;
    }

    public int getF(){
        return g + h;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }
}
