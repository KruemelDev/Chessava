package com.kruemel.chessava.client.game;

import com.kruemel.chessava.shared.game.Figure;

import java.awt.*;

public class Board {
    public Figure[][] figures = new Figure[8][8];

    GamePanel gamePanel;

    public int clickedFieldX = -1;
    public int clickedFieldY = -1;

    public Figure selectedFigure = null;

    public Board(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void Paint(Graphics2D g2d){
        paintBackground(g2d);
        paintFigures(g2d);
        markClickedField(g2d);
    }

    public void markClickedField(Graphics2D g2d){
        if(clickedFieldX == -1 || clickedFieldY == -1) return;
        g2d.setColor(Color.GREEN);
        g2d.setStroke(new BasicStroke(10));
        g2d.drawRect(clickedFieldX * gamePanel.tileSize, clickedFieldY * gamePanel.tileSize, gamePanel.tileSize, gamePanel.tileSize);
        clickedFieldX = -1;
        clickedFieldY = -1;

    }


    private void paintFigures(Graphics2D g2d){
        for(int i = 0; i < figures.length; i++){
            for (int j = 0; j < figures[i].length; j++) {
                Figure figure = figures[i][j];
                if(figure == null){
                    continue;
                }

                g2d.drawImage(figure.image, gamePanel.tileSize * i, gamePanel.tileSize * j, gamePanel.tileSize, gamePanel.tileSize, null);
            }
        }
    }

    private void paintBackground(Graphics2D g2d) {
        int tileWidth = this.gamePanel.gamePanelSize / 8;
        int tileHeight = this.gamePanel.gamePanelSize / 8;

        Color brown = new Color(101, 67, 33);
        Color white = new Color(255, 248, 240);

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if ((x + y) % 2 == 0) {
                    g2d.setColor(brown);
                } else {
                    g2d.setColor(white);
                }
                g2d.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
            }
        }
    }

}
