package de.fuwa.bomberman.game.appstates.visual;

import de.fuwa.bomberman.app.gui.SpriteSheetImageObject;
import de.fuwa.bomberman.game.enums.MoveDirection;

public class PlayerSpriteSheetControl extends AbstractSpriteSheetControl {

    private MoveDirection moveDirection = MoveDirection.Idle;
    private SpriteAnimation[] animations;
    private SpriteAnimation currentAnimation;

    @Override
    protected void setupControl(SpriteSheetImageObject spriteSheet) {
        super.setupControl(spriteSheet);

        this.animations = new SpriteAnimation[4];
        this.animations[0] = new SpriteAnimation(spriteSheet.getSubImages()[0]); // down
        this.animations[1] = new SpriteAnimation(spriteSheet.getSubImages()[1]); // up
        this.animations[2] = new SpriteAnimation(spriteSheet.getSubImages()[2]); // left
        this.animations[3] = new SpriteAnimation(spriteSheet.getSubImages()[3]); // right

        // we set a start sprite
        this.currentAnimation = animations[0];
        applyImage();
    }

    public void setMoveDirection(MoveDirection moveDirection) {
        if (moveDirection == null || this.moveDirection == moveDirection) return;
        this.moveDirection = moveDirection;
        if (moveDirection == MoveDirection.Idle) {
            currentAnimation.stop();
        } else {
            this.currentAnimation = getAnimationForMoveDirection();
            this.currentAnimation.start();
        }
    }

    private SpriteAnimation getAnimationForMoveDirection() {
        switch (moveDirection) {
            case Down:
                return animations[0];
            case Up:
                return animations[1];
            case Left:
                return animations[2];
            case Right:
                return animations[3];
            default:
                return animations[0];
        }
    }

    private void applyImage() {
        spriteSheet.setImageToDraw(currentAnimation.getCurrentFrame());
    }

    @Override
    public void update(float tpf) {
        if (currentAnimation != null) {
            currentAnimation.update(tpf);
            applyImage();
        }
    }
}
