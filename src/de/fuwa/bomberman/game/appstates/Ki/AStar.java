package de.fuwa.bomberman.game.appstates.Ki;

import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.components.CollisionComponent;
import de.fuwa.bomberman.game.components.PlayerComponent;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.enums.MoveDirection;

import java.util.ArrayList;
import java.util.List;

public class AStar {
    static int MapSizeX = 11;
    static int MapSizeY = 11;
    public static Path findPath(PositionComponent start, PositionComponent destination, EntityData entityData){

        Node[][] nodes = createMap(entityData, destination);

        List<Node> closedList = new ArrayList<>();

        List<Node> openList = new ArrayList<>();
        openList.add(nodes[(int)start.getX()][(int)start.getY()]);

        boolean done = false;
        Node current;
        while (!done){
            current = findLowestFInOpen(openList);
            closedList.add(current);
            openList.remove(current);

            if(current.getPos().getX() == destination.getX() && current.getPos().getY() == destination.getY()){
                return calcPath(nodes[(int)start.getX()][(int)start.getY()], current);
            }

            List<Node> adjacentNodes = getAdjacent(current, nodes, closedList);

            for(int i = 0; i < adjacentNodes.size(); i++){
                Node currentAdj = adjacentNodes.get(i);
                if(!openList.contains(currentAdj)){
                    currentAdj.setPrevious(current);
                    currentAdj.setG(current.getG() + 1);
                    openList.add(currentAdj);
                }
                else{
                    if(currentAdj.getG() > current.getG()){
                        current.setPrevious(current);
                        currentAdj.setG(current.getG());
                    }
                }
            }

            if(openList.isEmpty()){
                return new Path();
            }
        }
        return null;
    }

    private static Node findLowestFInOpen(List<Node> openList){

        Node cheapest = openList.get(0);
        for(int i = 0; i < openList.size(); i++){
            if(openList.get(i).getF() < cheapest.getF()){
                cheapest = openList.get(i);
            }
        }
        return cheapest;
    }

    private static Path calcPath(Node start, Node destination){
        Path path = new Path();

        Node curr = destination;

        boolean done = false;
        while(!done){
            Node temp = curr.getPrevious();

            MoveDirection dir = MoveDirection.Idle;

            System.out.println(temp.getPos().getX() + " " + temp.getPos().getY());
            System.out.println(curr.getPos().getX() + " " + curr.getPos().getY());

            if(temp.getPos().getX() < curr.getPos().getX()) dir = MoveDirection.Right;
            else if(temp.getPos().getX() > curr.getPos().getX()) dir = MoveDirection.Left;
            else if(temp.getPos().getY() < curr.getPos().getY()) dir = MoveDirection.Down;
            else if(temp.getPos().getY() > curr.getPos().getY()) dir = MoveDirection.Up;
            System.out.println(dir);
            System.out.println(" ");

            path.addMove(new Move(curr.getPos(), dir));
            curr = temp;

            if(curr.equals((start))){
                done = true;
            }
        }
        return path;
    }

    private static List<Node> getAdjacent(Node node, Node nodes[][], List<Node> closedList){
        List<Node> adjacentNodes = new ArrayList<>();

        int x = (int)node.getPos().getX();
        int y = (int)node.getPos().getY();

        Node temp;
        if(x > 0){
            temp = nodes[x-1][y];
            if(temp.isWalkable() && !closedList.contains(temp)) adjacentNodes.add(temp);
        }
        if(x < MapSizeX){
            temp = nodes[x+1][y];
            if(temp.isWalkable() && !closedList.contains(temp)) adjacentNodes.add(temp);
        }
        if(y > 0){
            temp = nodes[x][y-1];
            if(temp.isWalkable() && !closedList.contains(temp)) adjacentNodes.add(temp);
        }
        if(y < MapSizeY){
            temp = nodes[x][y+1];
            if(temp.isWalkable() && !closedList.contains(temp)) adjacentNodes.add(temp);
        }

        return adjacentNodes;
    }

    private static Node[][] createMap(EntityData entityData, PositionComponent destination){
        Node[][] nodes = new Node[MapSizeX][MapSizeY];

        EntitySet blockingEntities = entityData.getEntities(PositionComponent.class, CollisionComponent.class);

        for(int i = 0; i < MapSizeY; i++){
            for(int j = 0; j < MapSizeX; j++){
                nodes[j][i] = new Node(new PositionComponent(j, i), true, Math.abs(j - (int)destination.getX()) + Math.abs(i - (int)destination.getY()));
            }
        }

        for(Entity entity : blockingEntities){
            PlayerComponent playCom = entityData.getComponent(entity.getId(), PlayerComponent.class);
            if(playCom == null){
                PositionComponent posCom = entity.get(PositionComponent.class);

                nodes[(int)posCom.getX()][(int)posCom.getY()].setWalkable(false);
            }
        }
        return nodes;
    }
}
