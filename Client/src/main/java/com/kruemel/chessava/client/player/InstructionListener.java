package com.kruemel.chessava.client.player;

import com.kruemel.chessava.client.*;
import com.kruemel.chessava.client.game.GameMode;
import com.kruemel.chessava.client.game.GamePanel;
import com.kruemel.chessava.shared.networking.Commands;
import com.kruemel.chessava.shared.networking.Packet;
import com.kruemel.chessava.shared.networking.Util;
import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;

import java.awt.*;

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
                case BATTLE_REQUEST:
                    String name = packet.getData();
                    if (name.isEmpty()) break;
                    if(!gamePanel.BattleRequest(name)){
                        this.connectionHandler.WriteMessage(Util.dataToJson(Commands.BATTLE_DECLINE.getValue(), name));
                    } else{
                        this.connectionHandler.WriteMessage(Util.dataToJson(Commands.BATTLE_ACCEPT.getValue(), name));
                    }
                    break;
                case START_GAME:
                    gamePanel.repaint();
                    gamePanel.addMouseListener();
                    break;
                case SET_FIGURES:
                    if(gamePanel.gameMode == GameMode.SINGLE_PLAYER && !gamePanel.players[0].equals(connectionHandler.player)) break;
                    String figuresString = packet.getData();
                    gamePanel.board.figures = figureStringToFigures(figuresString);
                    gamePanel.repaint();
                    break;
                case CURRENT_PLAYER:
                    name = packet.getData();
                    gamePanel.currentPlayerName = name;
                    System.out.println("current player: " + name);
                    break;
                case ERROR:
                    MainFrameManager.instance.ShowPopupInfo(packet.getData());
                    break;

            }

        }
    }

    private Figure[][] figureStringToFigures(String figuresString){
        String[] figuresSplit = figuresString.split("\\|");

        Figure[][] figures = new Figure[8][8];
         for(int i = 0; i < 8; i++){
             for(int j = 0; j < 8; j++){
                 String currentString = figuresSplit[i * 8 + j];

                 if(currentString.isEmpty() || currentString.equals("null")){
                     figures[j][i] = null;
                     continue;
                 }

                 String[] figureColorSplit = currentString.split("\\(");

                 FigureType figureType = FigureType.valueOf(figureColorSplit[0].toUpperCase());
                 Color color = getFigureColor(figureColorSplit[1]);
                 if (color == null) connectionHandler.ResetToGameModeSelectionScreen("No color found for figure");
                 Figure figure = figureType.createInstance(color, j, i);
                 figures[j][i] = figure;
             }
         }

        return figures;
    }

    private Color getFigureColor(String str){
        String colorString = str.replaceAll("[\\(\\)]", "");
        if (colorString.equals("black")){
            return Color.BLACK;
        } else if (colorString.equals("white")){
            return Color.WHITE;
        }
        return null;
    }


    public void UpdatePlayerList(String players){
        String[] playerArray = players.split("\\|");
        for (int i = 0; i < playerArray.length; i++) {
            for (Player player : gamePanel.players){
                if (player == null) continue;
                if (Objects.equals(playerArray[i], player.name)) {
                    playerArray[i] = playerArray[i] + " (self)";
                }
            }
        }
        MainFrameManager.instance.UpdatePlayerList(playerArray);
    }


}
