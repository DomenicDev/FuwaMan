package de.fuwa.bomberman.game.enums;

public enum ModelType {

    Player("player.gif"),
    DestroyableTile("block.jpeg"),
    UndestroyableTile("block.jpeg"),
    Bomb(null),
    PowerUp(null);

    ModelType(String filename) {
        this.filename = filename;
    }

    private String filename;


    public String getFilename() {
        return filename;
    }
}
