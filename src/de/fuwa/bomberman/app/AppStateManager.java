package de.fuwa.bomberman.app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AppStateManager {

    private List<AppState> applicationStates = new ArrayList<>();

    private ConcurrentLinkedQueue<AppState> appStatesToRemove = new ConcurrentLinkedQueue<>();
    private List<AppState> appStatesToAdd    = new ArrayList<>();
    private List<AppState> toInitialize = new ArrayList<>();

    private GameApplication app;

    public AppStateManager(GameApplication app) {
        this.app = app;
    }

    public void attachState(AppState appState) {
        this.appStatesToAdd.add(appState);
    }

    public void detachState(AppState appState) {
        this.appStatesToRemove.add(appState);
    }

    void addStates() {
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

    void removeStates() {
        for (AppState appState : appStatesToRemove) {
            if (applicationStates.contains(appState)) {
                applicationStates.remove(appState);
                appState.cleanup();
            }
        }
        this.appStatesToRemove.clear(); // clear buffer
    }

    public <T extends AppState> T getState(Class<T> stateType) {
        for (AppState state : applicationStates) {
            if (state.getClass().isAssignableFrom(stateType)) {
                return (T) state;
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
    public List<AppState> getAppStates() {
        return this.applicationStates;
    }

    /**
     * Is called when the game is closed
     */
    public void cleanup() {
        this.applicationStates.clear();
    }
}
