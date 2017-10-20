package de.fuwa.bomberman.app;

import java.util.ArrayList;
import java.util.List;

public class AppStateManager {

    private List<AppState> applicationStates = new ArrayList<>();

    private GameApplication app;

    public AppStateManager(GameApplication app) {
        this.app = app;
    }

    public void attachState(AppState appState) {
        if (applicationStates.contains(appState)) {
            return;
        }
        this.applicationStates.add(appState);
    }

    public void detachState(AppState appState) {
        if (applicationStates.contains(appState)) {
            applicationStates.remove(appState);
        }
    }

    public <T extends AppState> T getState(Class<T> stateType) {
        for (AppState state : applicationStates) {
            if (state.getClass().isAssignableFrom(stateType)) {
                return (T) state;
            }
        }
        return null;
    }

    public void cleanup() {
        this.applicationStates.clear();
    }
}
