package de.fuwa.bomberman.app;

import javax.swing.*;

public abstract class GameApplication {

    private AppStateManager stateManager;

    private JFrame gameContext;

    private AppSettings appSettings;

    public abstract void initGame();

    public void start(AppSettings settings) {
        this.appSettings = settings;


        // Todo: Init game

        stateManager = new AppStateManager(this);

        // ...
        // init thread etc.

        // create gui
        gameContext = new JFrame();
        gameContext.setSize(appSettings.getWidth(), appSettings.getHeight());
        gameContext.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameContext.setLocationRelativeTo(null);
        gameContext.setVisible(true);


        initGame();
    }

    public void update(float tpf) {

    }

    public void render() {

    }


    public AppStateManager getStateManager() {
        return stateManager;
    }

    public void destroy(){

    }

}
