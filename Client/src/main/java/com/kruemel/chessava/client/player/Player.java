package com.kruemel.chessava.client.player;

import com.kruemel.chessava.client.game.GamePanel;
import com.kruemel.chessava.client.MainFrameManager;
import com.kruemel.chessava.shared.networking.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Player {

    public ConnectionHandler connectionHandler;

    private String ip;
    private int port;

    public DataInputStream in;
    public DataOutputStream out;

    public String name;
    GamePanel gamePanel;

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

        // TODO add option for pop to enter ip + port manualy

    }
}
