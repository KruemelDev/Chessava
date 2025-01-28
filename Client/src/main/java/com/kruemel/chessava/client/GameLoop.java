package com.kruemel.chessava.client;

public class GameLoop implements Runnable {

    volatile GamePanel gamePanel;

    public GameLoop(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        while (gamePanel != null) {
            //Thread.onSpinWait();

        }
    }
}
