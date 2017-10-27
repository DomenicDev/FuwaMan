package de.fuwa.bomberman.app;

/**
 * A basic implementation of the {@link AppState} interface.
 * Subclasses may override the empty methods contained in here.
 */
public abstract class BaseAppState implements AppState {

    @Override
    public void initialize(AppStateManager stateManager) {
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void render() {
    }

    @Override
    public void cleanup() {
    }

    @Override
    public boolean equals(Object obj) {
        // return true if obj is the exact same class
        return obj != null && obj.getClass() == getClass();
    }
}
