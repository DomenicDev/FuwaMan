package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;
import de.fuwa.bomberman.game.enums.ActionWhenTouchingExplosionType;

public class ActionWhenTouchingExplosionComponent implements EntityComponent {

    ActionWhenTouchingExplosionType actionWhenTouchingExplosionType;

    public ActionWhenTouchingExplosionComponent(ActionWhenTouchingExplosionType actionWhenTouchingExplosionType) {
        this.actionWhenTouchingExplosionType = actionWhenTouchingExplosionType;
    }

    public ActionWhenTouchingExplosionType getActionWhenTouchingExplosionType() {
        return actionWhenTouchingExplosionType;
    }
}
