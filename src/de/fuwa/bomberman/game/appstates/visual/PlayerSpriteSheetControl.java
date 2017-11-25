package de.fuwa.bomberman.game.appstates.visual;

import de.fuwa.bomberman.app.gui.SpriteSheetImageObject;
import de.fuwa.bomberman.game.enums.MoveDirection;

public class PlayerSpriteSheetControl extends AbstractSpriteSheetControl {

    private MoveDirection moveDirection;
    private SpriteAnimation[] animations;


    @Override
    protected void setupControl(SpriteSheetImageObject spriteSheet) {
        super.setupControl(spriteSheet);

        this.animations = new SpriteAnimation[4];

    }

    public void setMoveDirection(MoveDirection moveDirection) {
        this.moveDirection = moveDirection;
    }

    @Override
    public void update(float tpf) {
        // todo
    }
}
