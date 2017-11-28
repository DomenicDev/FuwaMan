package de.fuwa.bomberman.game.appstates.Ki;

import de.fuwa.bomberman.game.components.PositionComponent;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<Move> moves;

    public Path(List<Move> moves) {
        this.moves = moves;
    }

    public Path(){
        this.moves = new ArrayList<>();
    }

    public List<Move> getMoves() {
        return moves;
    }

    public Move getMove(int i){
        return moves.get(i);
    }

    public void addMove(Move move){
        moves.add(move);
    }

    public void removeMove(Move move){
        moves.remove(move);
    }
}
