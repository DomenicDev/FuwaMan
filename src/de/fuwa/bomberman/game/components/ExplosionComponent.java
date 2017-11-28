package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;

public class ExplosionComponent implements EntityComponent {

    //private int bombCentreX, bombCentreY;
    private float timer;
    public ExplosionComponent(float timer){
        //this.bombCentreX = bombCentreX;
        //this.bombCentreY = bombCentreY;
        this.timer = timer;
    }

    public float getTimer() {
        return timer;
    }
}
