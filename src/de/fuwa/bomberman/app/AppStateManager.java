package de.fuwa.bomberman.app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AppStateManager {

    private List<AppState> applicationStates = new ArrayList<>();

    private ConcurrentLinkedQueue<AppState> appStatesToRemove = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<AppState> appStatesToAdd = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<AppState> toInitialize = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<AppState> toCleanup = new ConcurrentLinkedQueue<>();

    private GameApplication app;

    public AppStateManager(GameApplication app) {
        this.app = app;
    }

    public void attachState(AppState appState) {
        if (appState != null && !appStatesToAdd.contains(appState)) {
            this.appStatesToAdd.add(appState);
        }
    }

    public void detachState(AppState appState) {
        if (appState != null && !appStatesToRemove.contains(appState)) {
            this.appStatesToRemove.add(appState);
        }
    }

    synchronized void addStates() {
        for (AppState appState : appStatesToAdd) {
            if (applicationStates.contains(appState)) {
                continue;
            }
            this.toInitialize.add(appState);
        }
        this.appStatesToAdd.clear(); // clear buffer
        for (AppState appState : toInitialize) {
            this.applicationStates.add(appState);
            appState.initialize(this);
        }
        toInitialize.clear();
    }

    synchronized void removeStates() {
        for (AppState appState : appStatesToRemove) {
            if (!applicationStates.contains(appState)) {
                continue;
            }
            toCleanup.add(appState);
        }
        this.appStatesToRemove.clear();
        for (AppState appState : toCleanup) {
            this.applicationStates.remove(appState);
            appState.cleanup();
        }
        this.toCleanup.clear(); // clear buffer
    }

    public <T extends AppState> T getState(Class<T> stateType) {
        for (AppState state : applicationStates) {
            if (state.getClass().isAssignableFrom(stateType)) {
                return stateType.cast(state);
            }
        }
        return null;
    }

    public GameApplication getGameApplication() {
        return app;
    }

    /**
     * @return a list containing all currently added app states
     */
    List<AppState> getAppStates() {
        return this.applicationStates;
    }

    /**
     * Is called when the game is closed
     */
    public void cleanup() {
        this.applicationStates.clear();
    }
}
