package com.kruemel.chessava.client.game;

import com.kruemel.chessava.shared.game.Figure;

import java.awt.*;

public class Board {
    public Figure[][] figures = new Figure[8][8];

    public static boolean gameStart = false;

    GamePanel gamePanel;

    public Board(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void Paint(Graphics2D g2d){
        if(!gameStart)return;
        paintBackground(g2d);
    }

    private void paintFigures(Graphics2D g2d){
        for (Figure[] row : figures) {
            for (Figure figure : row) {

            }
        }
    }

    private void paintBackground(Graphics2D g2d) {
        int tileWidth = this.gamePanel.gamePanelSize / 8;
        int tileHeight = this.gamePanel.gamePanelSize / 8;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if ((x + y) % 2 == 0) {
                    g2d.setColor(Color.BLACK);
                } else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
            }
        }
    }

}
