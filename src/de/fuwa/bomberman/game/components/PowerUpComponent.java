package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;
import de.fuwa.bomberman.game.enums.PowerUpType;

public class PowerUpComponent implements EntityComponent{

    private PowerUpType powerUpType;

    public PowerUpComponent(PowerUpType powerUpType) {
        this.powerUpType = powerUpType;
    }

    public PowerUpType getPowerUpType() {
        return powerUpType;
    }
}
