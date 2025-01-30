package com.kruemel.chessava.client.player;

import com.kruemel.chessava.client.GamePanel;
import com.kruemel.chessava.client.MainFrameManager;
import com.kruemel.chessava.shared.Commands;
import com.kruemel.chessava.shared.Util;

import java.io.*;
import java.net.*;

public class ConnectionHandler {
    public String ip;
    public int port;

    private Socket socket;
    public DataInputStream in;
    public DataOutputStream out;

    Player player;
    GamePanel gamePanel;
    InstructionListener instructionListener;

    public ConnectionHandler(String ip, int port, GamePanel gamePanel, Player player) {
        this.ip = ip;
        this.port = port;
        this.gamePanel = gamePanel;
        this.player = player;
    }

    public boolean ConnectServer(){
        try {
            socket = new Socket(this.ip, this.port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            startInstructionListener();
            System.out.println("Connected to " + ip + ":" + port);
        }catch (IOException e) {
            return false;
        }
        return true;
    }
    private void startInstructionListener(){
        instructionListener = new InstructionListener(this, gamePanel);
        new Thread(instructionListener).start();
    }

    public void SendCloseConnection(String reasonToClose){
        String json = Util.dataToJson(Commands.CLOSE_CONNECTION.getValue(), reasonToClose);
        WriteMessage(json);
    }

    public void ResetToGameModeSelectionScreen(String reasonToClose) {
        SendCloseConnection(reasonToClose);
        this.player.gamePanel = null;
        MainFrameManager.instance.GameModeSelectionScreen();
    }

    public void CloseConnection(){
        if(this.socket != null && !this.socket.isClosed()){
            try {
                this.socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void WriteMessage(String json) {
        try {
            if (out != null) {
                out.writeUTF(json);
                out.flush();
            }
        } catch (IOException e) {
            CloseConnection();
        }

    }
}
