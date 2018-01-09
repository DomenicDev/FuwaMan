package de.fuwa.bomberman.game.gui;

import de.fuwa.bomberman.game.enums.BlockType;

import javax.swing.*;
import java.awt.*;

public class LevelEditorGui extends JPanel {

    private ImageIcon undestructableBlock, destructableBlock;

    private BlockType[][] gameField;
    private JButton[][] visualGameField;
    private int sizeX;
    private int sizeY;

    private JPanel gameFieldPanel;

    public LevelEditorGui(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;



        String path = "assets/Textures/Settings/Classic/";
        this.undestructableBlock = new ImageIcon(path + "undes_block_01.png");
        this.destructableBlock = new ImageIcon(path + "des_block_01.png");
        setLayout(new BorderLayout());

        JPanel settingsPanel = new JPanel();
        settingsPanel.setPreferredSize(new Dimension(1, 200));
        add(settingsPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.YELLOW);
        FlowLayout f = new FlowLayout(FlowLayout.CENTER);
        f.setAlignment(FlowLayout.CENTER);
        f.setAlignOnBaseline(true);
        centerPanel.setLayout(f);
        add(centerPanel, BorderLayout.CENTER);

        this.gameFieldPanel = new JPanel();
        gameFieldPanel.setLayout(new GridLayout(sizeY, sizeX));
        gameFieldPanel.setBackground(Color.RED);




        centerPanel.add(gameFieldPanel);


        initGameField();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = gameFieldPanel.getWidth() / sizeX;
        int height = gameFieldPanel.getHeight() / sizeY;
        int t = Math.min(width, height);

        int texScale = Math.min(sizeX, sizeY);

        gameFieldPanel.setMinimumSize(new Dimension(t * texScale, t * texScale));
        gameFieldPanel.setPreferredSize(new Dimension(t * texScale, t * texScale));


        Image i = undestructableBlock.getImage();
        Image newImage = i.getScaledInstance(t, t, Image.SCALE_SMOOTH);
        this.undestructableBlock = new ImageIcon(newImage);

        repaintButtons();
    }

    private void initGameField() {
        this.gameField = new BlockType[sizeY][sizeX];
        this.visualGameField = new JButton[sizeY][sizeX];
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JButton block = new JButton("(" + x + "," + y + ")");
                gameFieldPanel.add(block);
                visualGameField[y][x] = block;
                if (isProtectedPosition(x, y, sizeX, sizeY)) {
                    setBlock(x, y, BlockType.Undestroyable);
                }
            }
        }
    }

    private void setBlock(int x, int y, BlockType type) {
        this.gameField[y][x] = type;
        updateButton(x, y, type);
    }

    private void updateButton(int x, int y, BlockType type) {
        Icon blockImage = getImageForType(type);
        if (blockImage != null) {
            JButton block = visualGameField[y][x];
            block.setIcon(blockImage);
        }
    }

    private void repaintButtons() {
        for (int y = 0; y < sizeY; y++){
            for (int x = 0; x < sizeX; x++) {
                Icon i = getImageForType(gameField[y][x]);
                visualGameField[y][x].setIcon(i);
            }
        }
    }

    private Icon getImageForType(BlockType type) {
        if (type == BlockType.Undestroyable) {
            return undestructableBlock;
        } else if (type == BlockType.Destroyable) {
            return destructableBlock;
        }
        return null;
    }

    private boolean isProtectedPosition(int x, int y, int sizeX, int sizeY) {
        return (y == 0 || x == 0 || y == sizeY - 1 || x == sizeX - 1);
    }

}
