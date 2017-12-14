package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.gui.GameContextFrame;
import de.fuwa.bomberman.game.appstates.session.GameSessionAppState;
import de.fuwa.bomberman.game.enums.MoveDirection;
import de.fuwa.bomberman.game.session.GameSession;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputAppState extends BaseAppState implements KeyListener {

    private GameContextFrame frame;
    private GameSession gameSession;
    private MoveDirection moveDirection;
    private MoveDirection lastMoveDir;
    private boolean up, down, left, right;

    @Override
    public void initialize(AppStateManager stateManager) {
        this.gameSession = stateManager.getState(GameSessionAppState.class).getGameSession();
        frame = stateManager.getGameApplication().getGameContext();
        frame.addKeyListener(this);
        moveDirection = MoveDirection.Idle;
        lastMoveDir = MoveDirection.Idle;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            gameSession.placeBomb();
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        }
    }

    @Override
    public void update(float tpf) {
        if (up) {
            moveDirection = MoveDirection.Up;
        }
        else if (down) {
            moveDirection = MoveDirection.Down;
        }
        else if (left) {
            moveDirection = MoveDirection.Left;
        }
        else if (right) {
            moveDirection = MoveDirection.Right;
        }else{
            moveDirection = MoveDirection.Idle;

        }

        if (moveDirection != lastMoveDir) {
            lastMoveDir = moveDirection;
            gameSession.applyMoveDirection(moveDirection);
        }

    }

    @Override
    public void cleanup() {
        this.frame.removeKeyListener(this);
        this.gameSession = null;
    }
}
