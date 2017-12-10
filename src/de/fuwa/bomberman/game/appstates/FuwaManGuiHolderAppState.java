package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.game.gui.*;

/**
 * This app state just creates and holds the guis of the game
 */
public class FuwaManGuiHolderAppState extends BaseAppState {

    private MainMenu mainMenu;
    private SingleplayerMenu singleplayerMenu;
    private MultiplayerConnectMenu multiplayerConnectMenu;
    private MultiplayerLobbyMenu multiplayerLobbyMenu;
    private InGameGui inGameGui;
    private LevelEditorMenu levelEditorMenu;

    public FuwaManGuiHolderAppState() {
        this.mainMenu = new MainMenu();
        this.singleplayerMenu = new SingleplayerMenu();
        this.multiplayerConnectMenu = new MultiplayerConnectMenu();
        this.multiplayerLobbyMenu = new MultiplayerLobbyMenu();
        this.inGameGui = new InGameGui();
        this.levelEditorMenu = new LevelEditorMenu();
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public SingleplayerMenu getSingleplayerMenu() {
        return singleplayerMenu;
    }

    public MultiplayerConnectMenu getMultiplayerConnectMenu() {
        return multiplayerConnectMenu;
    }

    public MultiplayerLobbyMenu getMultiplayerLobbyMenu() {
        return multiplayerLobbyMenu;
    }

    public InGameGui getInGameGui() {
        return inGameGui;
    }

    public LevelEditorMenu getLevelEditorMenu() {
        return levelEditorMenu;
    }
}
