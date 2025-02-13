package com.kruemel.chessava.client.player;

import com.kruemel.chessava.client.game.GamePanel;
import com.kruemel.chessava.client.MainFrameManager;
import com.kruemel.chessava.shared.networking.Util;

import java.awt.*;

public class Player {

    public ConnectionHandler connectionHandler;

    private final String ip;
    private final int port;

    public String name;
    public Color color;
    public GamePanel gamePanel;


    public Player(String ip, int port, String name, GamePanel gamePanel) {
        this.ip = ip;
        this.port = port;
        this.gamePanel = gamePanel;
        this.name = name;

    }

    public void InitPlayer() {
        connectionHandler = new ConnectionHandler(this.ip, this.port, gamePanel, this);

        System.out.println(ip + ":" + port);
        boolean status = connectionHandler.ConnectServer();
        System.out.println(status);
        if(!status) {
            MainFrameManager.instance.ShowPopupInfo("Server not found");
            connectionHandler.ResetToGameModeSelectionScreen("Server not found");
        }

        connectionHandler.WriteMessage(Util.dataToJson("Name", this.name));
    }
}
