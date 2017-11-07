package de.fuwa.bomberman.tests;

import de.fuwa.bomberman.app.AppSettings;
import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.GameApplication;

public class TestMain extends GameApplication {

    public static void main(String[] args) {
        TestMain test = new TestMain();
        AppSettings settings = new AppSettings(800, 600, false);
        test.start(settings);
    }

    @Override
    public void initGame() {
        getStateManager().attachState(new TimerAppState());
        getStateManager().attachState(new DestroyerAppState());
    }

    private class TimerAppState extends BaseAppState {

        private float timer = 0;

        @Override
        public void update(float tpf) {
            timer += tpf;
            System.out.println(timer);
        }

        @Override
        public void cleanup() {
            System.out.println("bin in cleanup");
        }
    }

    private class DestroyerAppState extends BaseAppState {

        private AppStateManager stateManager;

        private float timer = 0;

        @Override
        public void initialize(AppStateManager stateManager) {
            this.stateManager = stateManager;
        }

        @Override
        public void update(float tpf) {
            timer += tpf;

            if (timer > 10) {
                stateManager.detachState(stateManager.getState(TimerAppState.class));
            }
        }
    }

}
