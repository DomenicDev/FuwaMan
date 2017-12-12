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
            System.out.println("W-press");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
            System.out.println("A-press");
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = true;
            System.out.println("S-press");
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
            System.out.println("D-press");
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up = false;
            System.out.println("W-released");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
            System.out.println("A-released");
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = false;
            System.out.println("S-released");
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
            System.out.println("D-released");
        }
    }

    @Override
    public void update(float tpf) {
        if (up) {
            moveDirection = MoveDirection.Up;
            System.out.println("MOVE UP");
        }
        else if (down) {
            moveDirection = MoveDirection.Down;
            System.out.println("MOVE DOWN");
        }
        else if (left) {
            moveDirection = MoveDirection.Left;
            System.out.println("MOVE LEFT");
        }
        else if (right) {
            moveDirection = MoveDirection.Right;
            System.out.println("MOVE RIGHT");
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
