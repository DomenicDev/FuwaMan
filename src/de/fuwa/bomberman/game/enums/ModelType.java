package de.fuwa.bomberman.game.enums;

public enum ModelType {

    Player(null),
    DestroyableTile("undestroyable.png"),
    UndestroyableTile("undestroyable.png"),
    Bomb("Bomb_Artwork.png"),
    SpeedUp("SpeedUp.png"),
    BombStrengthUp("BombStrengthUp.png"),
    BombAmountUp("BombAmountUp.png"),
    Explosion("Explosion.png");


    ModelType(String filename) {
        this.filename = filename;
    }

    private String filename;


    public String getFilename() {
        return filename;
    }
}
