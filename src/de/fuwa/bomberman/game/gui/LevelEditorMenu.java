package de.fuwa.bomberman.game.gui;

import de.fuwa.bomberman.game.enums.BlockType;
import de.fuwa.bomberman.game.enums.Setting;
import de.fuwa.bomberman.game.utils.ExternalDataManager;
import de.fuwa.bomberman.game.utils.GameConstants;
import de.fuwa.bomberman.game.utils.GameField;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelEditorMenu extends JPanel {

    private int gameFieldSizeX;
    private int gameFieldSizeY;
    private Setting setting;

    private BlockType[][] logicalGameField;
    private JButton[][] visualGameField;

    private VisualEditor editor;
    private ImageIcon undestructableBlock;
    private ImageIcon destructableBlock;
    private BlockType selectedBlockType;

    private JSpinner widthSpinner, heightSpinner;

    public LevelEditorMenu() {
        this(GameConstants.MIN_GAME_FIELD_SIZE, GameConstants.MIN_GAME_FIELD_SIZE);
    }

    public LevelEditorMenu(int gameFieldSizeX, int gameFieldSizeY) {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 3));
        topPanel.setPreferredSize(new Dimension(1, 100));
        add(topPanel, BorderLayout.NORTH);

        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new GridLayout(2, 1));
        sizePanel.setBorder(BorderFactory.createTitledBorder("Game field size"));

        JPanel widthPanel = new JPanel();
        widthPanel.add(new JLabel("Width"));


        SizeChangeHandler sizeChangeHandler = new SizeChangeHandler();

        SpinnerNumberModel model = new SpinnerNumberModel(GameConstants.MIN_GAME_FIELD_SIZE, GameConstants.MIN_GAME_FIELD_SIZE, 30, 1);
        this.widthSpinner = new JSpinner(model);
        widthSpinner.addChangeListener(sizeChangeHandler);
        widthPanel.add(widthSpinner);
        sizePanel.add(widthPanel);

        SpinnerModel model1 = new SpinnerNumberModel(GameConstants.MIN_GAME_FIELD_SIZE, GameConstants.MIN_GAME_FIELD_SIZE, 30, 1);
        JPanel heightPanel = new JPanel();
        heightPanel.add(new JLabel("Height"));
        this.heightSpinner = new JSpinner(model1);
        this.heightSpinner.addChangeListener(new SizeChangeHandler());
        heightPanel.add(heightSpinner);
        sizePanel.add(heightPanel);

        topPanel.add(sizePanel);

        JPanel blockTypePanel = new JPanel();
        blockTypePanel.setBorder(BorderFactory.createTitledBorder("Select block type to place"));
        JComboBox<BlockType> typeBox = new JComboBox<>();
        typeBox.addItem(null);
        typeBox.addItem(BlockType.Undestroyable);
        typeBox.addItem(BlockType.Destroyable);
        blockTypePanel.add(typeBox);
        typeBox.setSelectedIndex(0);
        typeBox.addItemListener(e -> selectedBlockType = typeBox.getItemAt(typeBox.getSelectedIndex()));
        topPanel.add(blockTypePanel);

        JPanel savePanel = new JPanel();
        savePanel.setBorder(BorderFactory.createTitledBorder("Save game field"));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            saveMap();
        });
        savePanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            BasicFuwaManPanel.listener.onClickReturnToMainMenu();
        });
        savePanel.add(cancelButton);
        topPanel.add(savePanel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        add(centerPanel, BorderLayout.CENTER);

        this.editor = new VisualEditor();
        centerPanel.add(editor);

        setSetting(Setting.Classic);
        initGameField(gameFieldSizeX, gameFieldSizeY);
    }

    private void initGameField(int gameFieldSizeX, int gameFieldSizeY) {
        this.gameFieldSizeX = gameFieldSizeX;
        this.gameFieldSizeY = gameFieldSizeY;

        this.logicalGameField = new BlockType[gameFieldSizeY][gameFieldSizeX];
        this.visualGameField = new JButton[gameFieldSizeY][gameFieldSizeX];

        this.editor.removeAll();

        this.editor.setLayout(new GridLayout(gameFieldSizeY, gameFieldSizeX));
        this.editor.resizePanel();

        for (int y = 0; y < gameFieldSizeY; y++) {
            for (int x = 0; x < gameFieldSizeX; x++) {
                this.visualGameField[y][x] = new JButton();
                this.visualGameField[y][x].addActionListener(new ButtonClickHandler(x, y));
                this.editor.add(visualGameField[y][x]);
                if (isReservedPosition(x, y)) {
                    setBlockType(x, y, BlockType.Undestroyable);
                }
            }
        }
    }

    private void setSetting(Setting setting) {
        if (this.setting != setting) {
            this.setting = setting;
            reloadImages();
        }
    }

    private void reloadImages() {
        String path = "assets/Textures/Settings/" + setting + "/";
        this.undestructableBlock = new ImageIcon(path + "undes_block_01.png");
        this.destructableBlock = new ImageIcon(path + "des_block_01.png");
    }

    private void setBlockType(int x, int y, BlockType type) {
        logicalGameField[y][x] = type;
        // update visual game field
        updateVisualBlockType(x, y, type);
    }

    private void updateVisualBlockType(int x, int y, BlockType type) {
        JButton block = this.visualGameField[y][x];
        ImageIcon icon = getIconForType(type);
        block.setIcon(icon);
    }

    private ImageIcon getIconForType(BlockType type) {
        if (type == null) return null;
        switch (type) {
            case Destroyable:
                return destructableBlock;
            case Undestroyable:
                return undestructableBlock;
        }
        return null;
    }

    private boolean isReservedPosition(int x, int y) {
        return (y == 0 || x == 0 || y == this.gameFieldSizeY - 1 || x == this.gameFieldSizeX - 1);
    }

    private void scaleImages(int iconWidth, int iconHeight) {
        this.undestructableBlock = new ImageIcon(undestructableBlock.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_FAST));
        this.destructableBlock = new ImageIcon(destructableBlock.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_FAST));
        refreshIconsForButtons();
    }

    private void saveMap() {
        GameField gameField = new GameField(gameFieldSizeX, gameFieldSizeY);
        for (int y = 0; y < gameFieldSizeY; y++) {
            for (int x = 0; x < gameFieldSizeX; x++) {
                gameField.setBlock(x, y, logicalGameField[y][x]);
            }
        }
        String name = JOptionPane.showInputDialog("Please enter a name for this map");
        if (ExternalDataManager.writeCustomMap(name, gameField)) {
            JOptionPane.showMessageDialog(null, "Saved Map");
        } else {
            JOptionPane.showMessageDialog(null, "Could not save map");
        }

    }

    private void refreshIconsForButtons() {
        for (int y = 0; y < gameFieldSizeY; y++) {
            for (int x = 0; x < gameFieldSizeX; x++) {
                ImageIcon icon = getIconForType(logicalGameField[y][x]);
                if (icon != null) {
                    visualGameField[y][x].setIcon(icon);
                }
            }
        }
    }

    private class ButtonClickHandler implements ActionListener {

        private int x, y;

        private ButtonClickHandler(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setBlockType(x, y, selectedBlockType);
        }
    }

    private class SizeChangeHandler implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() == widthSpinner) {
                gameFieldSizeX = (int) widthSpinner.getValue();
            } else {
                gameFieldSizeY = (int) heightSpinner.getValue();
            }
            initGameField(gameFieldSizeX, gameFieldSizeY);
        }
    }

    private class VisualEditor extends JPanel {

        private Dimension preferredDim = new Dimension();
        private int lastTileSize;

        private void resizePanel() {
            Container c = getParent();
            int tileSize = Math.min(c.getWidth() / gameFieldSizeX, c.getHeight() / gameFieldSizeY);

            // we want to resize the icons if the size has changed
            if (lastTileSize != tileSize) {
                scaleImages(tileSize, tileSize);
                this.lastTileSize = tileSize;
            }

            // apply preferred size
            preferredDim.setSize(gameFieldSizeX * tileSize, gameFieldSizeY * tileSize);
        }

        @Override
        public Dimension getPreferredSize() {
            if (getParent() != null) {
                resizePanel();
                return preferredDim;
            }
            return super.getPreferredSize();
        }

    }

}
