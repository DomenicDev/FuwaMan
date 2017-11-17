package de.fuwa.bomberman.game.enums;

public enum ModelType {

    Player("playerNew.png"),
    DestroyableTile("undestroyable.png"),
    UndestroyableTile("undestroyable.png"),
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
