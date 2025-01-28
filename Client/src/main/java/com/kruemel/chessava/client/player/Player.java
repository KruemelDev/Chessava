package com.kruemel.chessava.client.player;

import com.kruemel.chessava.client.GamePanel;
import com.kruemel.chessava.client.MainFrameManager;
import com.kruemel.chessava.dto.Util;

public class Player {

    public ConnectionHandler connectionHandler;

    private String ip;
    private int port;

    public PlayerType playerType;

    GamePanel gamePanel;
    public Player(String ip, int port, GamePanel gamePanel) {
        this.ip = ip;
        this.port = port;
        this.gamePanel = gamePanel;

        initPlayer();
    }

    private void initPlayer() {
        connectionHandler = new ConnectionHandler(this.ip, this.port, gamePanel);
        System.out.println(ip + ":" + port);
        boolean status = connectionHandler.ConnectServer();
        connectionHandler.WriteMessage(Util.dataToJson("Name", "Test"));
        // TODO add sending individual name
        if(!status) MainFrameManager.instance.AddGameModeSelectionScreen();
        // TODO add option for pop to enter ip + port manualy

    }
}
