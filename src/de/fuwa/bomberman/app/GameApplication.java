package de.fuwa.bomberman.app;

import de.fuwa.bomberman.app.gui.GameContextFrame;
import de.fuwa.bomberman.app.gui.GameContextListener;

/**
 * The GameApplication is the heart of our application.
 * Here we initialize and run our game thread, create the gui context
 * and control all other parts of the application.
 *
 * However, here no game logic is implemented at all.
 * To implement game logic use {@link AppState} or {@link BaseAppState} instead.
 */
public abstract class GameApplication {

    private AppStateManager stateManager;
    private GameContextFrame gameContext;
    private AppSettings appSettings;
    private GameLoop gameLoop;

    /**
     * This has to be implement by the subclass.
     * It's called during the initialization of the game
     * (when start() is called)
     */
    public abstract void initGame();

    /**
     * This method will initialize the application by
     * creating the game thread, the game context (window),
     * and all other elements of the application to be prepared
     * for the actual work.
     * @param settings the settings of the
     */
    public void start(AppSettings settings) {
        // save specified app settings
        this.appSettings = settings;

        // create a new game loop for the following thread
        this.gameLoop = new GameLoop();

        // create the thread
        Thread gameThread = new Thread(gameLoop);

        // create our AppStateManager to manage AppStates
        stateManager = new AppStateManager(this);

        // create gui by instancing our custom JFrame class
        gameContext = new GameContextFrame(new GuiEventHandler());
        gameContext.setAppSettings(this.appSettings);
        gameContext.buildContext();

        // call user code
        initGame();

        // start game thread
        gameThread.start();
    }


    /**
     * A basic game loop implementation.
     */
    private class GameLoop implements Runnable {

        // a flag which indicates the running state of the application
        private boolean active = true;

        // the last time stamp to measure the time elapsed between the current and the last frane
        private long lastTimeStamp = System.nanoTime();

        /**
         * Called when user stops game or frame window is closed
         */
        private void stopGameLoop() {
            this.active = false; // set flag to false --> will close thread
        }

        @Override
        public void run() {
            // active is a flag which can be set to false which will make
            // this thread being stopped
            while (active) {
                // we get the current time
                long now = System.nanoTime();
                // next we get the time between this and the last frame (in seconds)
                float tpf = (float) (now - lastTimeStamp) / 1000000000L;
                this.lastTimeStamp = now; // update our lastTimeStamp

                // first thing we want to do is
                // adding and removing the app states
                // which are listed in the buffer
                getStateManager().addStates();
                getStateManager().removeStates();

                // now we want to update all app states
                for (AppState appState : getStateManager().getAppStates()) {
                    appState.update(tpf);
                }

                // after update we call render
                for (AppState appState : getStateManager().getAppStates()) {
                    appState.render();
                }
            }
        }
    }

    /**
     * A basic gui event handler.
     */
    private class GuiEventHandler implements GameContextListener {

        @Override
        public void onExit() {
            destroy();
        }
    }

    /**
     * @return the app state manager of this application.
     */
    public AppStateManager getStateManager() {
        return stateManager;
    }

    /**
     * @return the game context to get access to the graphical window
     */
    public GameContextFrame getGameContext() {
        return gameContext;
    }

    public void setAppSettings(AppSettings appSettings) {
        this.appSettings = appSettings;
        this.gameContext.setAppSettings(appSettings);
    }

    public AppSettings getAppSettings() {
        return appSettings;
    }

    /**
     * Called when the game is closed.
     */
    public void destroy(){
        // stop game thread (main game loop)
        this.gameLoop.stopGameLoop();
    }

}
