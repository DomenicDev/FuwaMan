package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;
import de.fuwa.bomberman.game.enums.ModelType;

public class ModelComponent implements EntityComponent {

    private ModelType modelType;
    private boolean animated;

    public ModelComponent(ModelType modelType, boolean animated) {
        this.modelType = modelType;
        this.animated = animated;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public boolean isAnimated() {
        return animated;
    }
}
