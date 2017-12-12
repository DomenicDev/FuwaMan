package de.fuwa.bomberman.game.appstates.visual;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.appstates.FuwaManGuiHolderAppState;
import de.fuwa.bomberman.game.components.NameComponent;
import de.fuwa.bomberman.game.components.PlayerComponent;
import de.fuwa.bomberman.game.components.ScoreComponent;
import de.fuwa.bomberman.game.gui.InGameGui;

public class VisualPlayerInfoAppState extends BaseAppState {

    private EntitySet players;
    private InGameGui inGameGui;

    @Override
    public void initialize(AppStateManager stateManager) {
        EntityData entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.players = entityData.getEntities(PlayerComponent.class, ScoreComponent.class, NameComponent.class);
        this.inGameGui = stateManager.getState(FuwaManGuiHolderAppState.class).getInGameGui();
    }

    @Override
    public void update(float tpf) {

        if (players.applyChanges()) {

            for (Entity player : players.getAddedEntities()) {
                String name = player.get(NameComponent.class).getName();
                inGameGui.addPlayerInfo(player.getId(), name);
            }

            for (Entity player : players.getChangedEntities()) {
                int score = player.get(ScoreComponent.class).getScore();
                inGameGui.refreshScoreForPlayer(player.getId(), score);
            }

            for (Entity player : players.getRemovedEntities()) {
                inGameGui.removePlayerInfo(player.getId());
            }

        }

    }

    @Override
    public void cleanup() {
        for (Entity entity : players) {
            inGameGui.removePlayerInfo(entity.getId());
        }
        this.players.close();
        this.players.clear();
        this.players = null;
    }
}
