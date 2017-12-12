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
    private OptionsMenu optionsMenu;
    private LevelEditorMenu levelEditorMenu;
    private Credits credits;
    private ClientWaitingLobbyMenu clientWaitingLobbyMenu;

    public FuwaManGuiHolderAppState() {
        this.mainMenu = new MainMenu();
        this.singleplayerMenu = new SingleplayerMenu();
        this.multiplayerConnectMenu = new MultiplayerConnectMenu();
        this.multiplayerLobbyMenu = new MultiplayerLobbyMenu();
        this.inGameGui = new InGameGui();
        this.optionsMenu = new OptionsMenu();
        this.levelEditorMenu = new LevelEditorMenu();
        this.credits = new Credits();
        this.clientWaitingLobbyMenu = new ClientWaitingLobbyMenu();
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
    public OptionsMenu getOptionsMenu(){
        return optionsMenu;
    }

    public InGameGui getInGameGui() {
        return inGameGui;
    }

    public LevelEditorMenu getLevelEditorMenu() {
        return levelEditorMenu;
    }

    public Credits getCredits () {return credits; }

    public ClientWaitingLobbyMenu getClientWaitingLobbyMenu() {
        return clientWaitingLobbyMenu;
    }
}
