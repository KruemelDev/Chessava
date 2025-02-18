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
                    gamePanel.InGame = false;
                    break;
                case PLAYER_AVAILABLE:
                    if(gamePanel.gameMode == GameMode.SINGLE_PLAYER && !gamePanel.players[0].equals(connectionHandler.player)) break;
                    String players = packet.getData();
                    if(!players.isEmpty()){
                        UpdatePlayerList(players);
                    }
                    break;
                case BATTLE_REQUEST:
                    String opponentName = packet.getData();
                    if (opponentName.isEmpty()) break;
                    if(!gamePanel.BattleRequest(opponentName)){
                        this.connectionHandler.WriteMessage(Util.dataToJson(Commands.BATTLE_DECLINE.getValue(), opponentName));
                    } else{
                        this.connectionHandler.WriteMessage(Util.dataToJson(Commands.BATTLE_ACCEPT.getValue(), opponentName));
                    }
                    break;
                case START_GAME:
                    gamePanel.InGame = true;
                    gamePanel.repaint();
                    gamePanel.addMouseListener();
                    break;
                case GAME_COLOR:
                    String color = packet.getData();
                    handlePlayerColor(color);
                    break;
                case SET_FIGURES:
                    if(gamePanel.gameMode == GameMode.SINGLE_PLAYER && !gamePanel.players[0].equals(connectionHandler.player)) break;
                    String figuresString = packet.getData();
                    gamePanel.board.figures = figureStringToFigures(figuresString);
                    gamePanel.repaint();
                    gamePanel.board.selectedFigure = null;
                    break;
                case NEXT_PLAYER:
                    String name = packet.getData();
                    if (gamePanel.players[0].equals(connectionHandler.player)){
                        gamePanel.repaint();
                        gamePanel.board.ChangeCurrentMarkColor();
                    }
                    gamePanel.currentPlayerName = name;
                    System.out.println("current player: " + name);
                    break;
                case FIGURE_SELECT:
                    String input = "";
                    while(input.trim().isEmpty()){
                        input = MainFrameManager.instance.ShowPopUpInput(packet.getData());
                    }
                    this.connectionHandler.WriteMessage(Util.dataToJson(Commands.FIGURE_SELECT.getValue(), input));
                    break;
                case END_GAME:
                    gamePanel.EndGame(packet.getData());
                    gamePanel.InGame = false;
                    break;
                case ERROR:
                    MainFrameManager.instance.ShowPopupInfo(packet.getData());
                    break;

            }

        }
    }

    private void handlePlayerColor(String color) {
        if (gamePanel.gameMode == GameMode.MULTI_PLAYER) {
            if (color.equals("white")) MainFrameManager.instance.ShowPopupInfo("You color is white. You start the game!");
            else if (color.equalsIgnoreCase("black")) MainFrameManager.instance.ShowPopupInfo("Your color is black. You need to wait for your opponent!");
        }
        addColorToPlayer(color);
    }

    private void addColorToPlayer(String color){
        if(color.isEmpty()) return;
        if (color.equals("black")){
            this.connectionHandler.player.color = Color.BLACK;
        } else if (color.equals("white")){
            this.connectionHandler.player.color = Color.WHITE;
        }

    }

    private Figure[][] figureStringToFigures(String figuresString){
        String[] figuresSplit = figuresString.split("\\|");

        Figure[][] figures = new Figure[8][8];
         for(int y = 0; y < 8; y++){
             for(int x = 0; x < 8; x++){
                 int index = y * 8 + x;
                 String currentString = figuresSplit[index];

                 if(currentString.isEmpty() || currentString.equals("null")){
                     figures[y][x] = null;
                     continue;
                 }
                 String[] figureColorSplit = currentString.split("\\(");

                 FigureType figureType = FigureType.valueOf(figureColorSplit[0].toUpperCase());
                 Color color = getFigureColor(figureColorSplit[1]);
                 if (color == null) connectionHandler.ResetToGameModeSelectionScreen("No color found for figure");
                 Figure figure = figureType.createInstance(color, x, y);
                 figures[y][x] = figure;
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
