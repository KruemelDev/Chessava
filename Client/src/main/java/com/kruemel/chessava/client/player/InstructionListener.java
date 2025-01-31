package com.kruemel.chessava.client.player;

import com.kruemel.chessava.client.GameMode;
import com.kruemel.chessava.client.GamePanel;
import com.kruemel.chessava.client.MainFrameManager;
import com.kruemel.chessava.shared.Commands;
import com.kruemel.chessava.shared.Packet;
import com.kruemel.chessava.shared.Util;

import java.util.Objects;

public class InstructionListener implements Runnable{

    volatile ConnectionHandler connectionHandler;
    GamePanel gamePanel;
    public InstructionListener(ConnectionHandler connectionHandler, GamePanel gamePanel) {
        this.connectionHandler = connectionHandler;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        String json;
        while (connectionHandler != null) {
            try {
                json = connectionHandler.in.readUTF();
            } catch (Exception e) {
                connectionHandler.ResetToGameModeSelectionScreen("error");
                break;
            }
            Packet packet = Util.jsonToData(json);
            if(packet == null) continue;

            Commands command = Commands.fromString(packet.getCommand());

            switch (command){
                case CLOSE_CONNECTION:
                    String reason = packet.getData();
                    if(!Objects.equals(reason, "")) MainFrameManager.instance.ShowPopupInfo(reason);
                    if (gamePanel.gameMode == GameMode.SINGLE_PLAYER) gamePanel.DestroyPlayer();
                    connectionHandler.ResetToGameModeSelectionScreen("ClosedCon");
                    break;

                case PLAYER_AVAILABLE:
                    if(gamePanel.gameMode == GameMode.SINGLE_PLAYER && !gamePanel.players[0].equals(connectionHandler.player)) break;
                    String players = packet.getData();
                    if(!players.isEmpty()){
                        UpdatePlayerList(players);
                    }
                    break;
            }

        }
    }
    public void UpdatePlayerList(String players){
        String[] playerArray = players.split("\\|");
        for (int i = 0; i < playerArray.length; i++) {
            if (Objects.equals(playerArray[i], gamePanel.players[0].name) || Objects.equals(playerArray[i], gamePanel.players[1].name)) {
                playerArray[i] = playerArray[i] + " (self)";
            }
        }
        MainFrameManager.instance.UpdatePlayerList(playerArray);
    }


}
