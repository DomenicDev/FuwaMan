package de.fuwa.bomberman.app;

public interface AppState {

    void initialize(AppStateManager stateManager);

    void update(float tpf);

    void render();

    void cleanup();

}
