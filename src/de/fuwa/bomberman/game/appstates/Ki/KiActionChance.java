package de.fuwa.bomberman.game.appstates.Ki;

public class KiActionChance {
    Tile tile;
    float chance;

    public KiActionChance(Tile tile, float chance) {
        this.tile = tile;
        this.chance = chance;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public float getChance() {
        return chance;
    }

    public void setChance(float chance) {
        this.chance = chance;
    }
}
