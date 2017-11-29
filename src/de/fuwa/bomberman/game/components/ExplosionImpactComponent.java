package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;
import de.fuwa.bomberman.game.enums.ExplosionImpactType;

public class ExplosionImpactComponent implements EntityComponent {

    ExplosionImpactType explosionImpactType;

    public ExplosionImpactComponent(ExplosionImpactType explosionImpactType) {
        this.explosionImpactType = explosionImpactType;
    }

    public ExplosionImpactType getExplosionImpactType() {
        return explosionImpactType;
    }
}
