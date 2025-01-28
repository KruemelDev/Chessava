package com.kruemel.chessava.client.player;

import com.kruemel.chessava.client.GamePanel;
import com.kruemel.chessava.client.MainFrameManager;
import com.kruemel.chessava.dto.Util;

import java.io.*;
import java.net.*;

public class ConnectionHandler {
    public String ip;
    public int port;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;


    GamePanel gamePanel;
    public ConnectionHandler(String ip, int port, GamePanel gamePanel) {
        this.ip = ip;
        this.port = port;
        this.gamePanel = gamePanel;
    }

    public boolean ConnectServer(){
        try {
            socket = new Socket(this.ip, this.port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        }catch (IOException e) {
            return false;
        }
        return true;
    }

    public void CloseConnection(){
        String data = Util.dataToJson("CloseConnection");
        WriteMessage(data);
        gamePanel = null;
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void WriteMessage(String json) {
        try {
            if (out != null) {
                out.writeUTF(json);
                out.flush();
            }
        } catch (IOException e) {
            MainFrameManager.instance.AddGameModeSelectionScreen();
        }

    }
}
