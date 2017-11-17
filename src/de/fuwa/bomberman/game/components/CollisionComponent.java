package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;

public class CollisionComponent implements EntityComponent {

    private float xOffset, yOffset, width, height;
    private boolean staticObject;

    public CollisionComponent(float xOffset, float yOffset, float width, float height, boolean staticObject) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.width = width;
        this.height = height;
        this.staticObject = staticObject;
    }

    public float getOffsetX() {
        return xOffset;
    }

    public float getOffsetY() {
        return yOffset;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isStaticObject() {
        return staticObject;
    }
}
