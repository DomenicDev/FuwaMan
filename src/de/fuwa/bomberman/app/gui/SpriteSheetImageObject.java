package de.fuwa.bomberman.app.gui;

import java.awt.image.BufferedImage;

public class SpriteSheetImageObject extends AbstractDrawableObject {

    private BufferedImage spriteSheet;
    private BufferedImage[][] subImages;
    private int sizeX;
    private int sizeY;

    public SpriteSheetImageObject(BufferedImage spriteSheet, int sizeX, int sizeY) {
        this.spriteSheet = spriteSheet;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        // create subimage array
        this.subImages = new BufferedImage[sizeY][sizeX];

        final int TILE_SIZE_X = spriteSheet.getWidth() / sizeX;
        final int TILE_SIZE_Y = spriteSheet.getHeight() / sizeY;

        // fill with subimages
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                subImages[y][x] = spriteSheet.getSubimage(x * TILE_SIZE_X, y * TILE_SIZE_Y, TILE_SIZE_X, TILE_SIZE_Y);
            }
        }
    }

    public BufferedImage[][] getSubImages() {
        return subImages;
    }

    public BufferedImage getSprite(int x, int y) {
        if (x >= 0 && x < sizeX && y >= 0 && y < sizeY) {
            return subImages[y][x];
        }
        return null;
    }

}
