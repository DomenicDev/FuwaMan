package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;

public class CollisionComponent implements EntityComponent {

    private float x, y, width, height;

    public CollisionComponent(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
