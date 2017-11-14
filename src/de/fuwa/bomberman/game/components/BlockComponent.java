package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;
import de.fuwa.bomberman.game.enums.BlockType;

public class BlockComponent implements EntityComponent {

    private BlockType blockType;

    public BlockComponent(BlockType blockType) {
        this.blockType = blockType;
    }

    public BlockType getBlockType() {
        return blockType;
    }
}
