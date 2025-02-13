package com.kruemel.chessava.server.game;

import com.kruemel.chessava.server.clientHandling.Client;
import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;
import com.kruemel.chessava.shared.game.figureTypes.Pawn;
import com.kruemel.chessava.shared.networking.Commands;
import com.kruemel.chessava.shared.networking.Util;

import java.awt.*;
import java.util.Random;

public class Game {

    public Client player1, player2;
    public Client[] players;

    public Board board;

    public Client currentPlayer;

    public Game(Client player1, Client player2) {
        this.player1 = player1;
        this.player2 = player2;

        players = new Client[]{player1, player2};
        board = new Board(players, this);
    }
    public void InitGame(){
        setPlayerInGame();
        sendGameStart();
        initPlayerColors();
        sendPlayerColors();
        SendCurrentPlayer();
        board.SendFigures();
    }
    private void initPlayerColors(){
        Random random = new Random();
        int result = random.nextInt(0, 2);

        if (result == 0){
            this.player1.gameColor = Color.WHITE;
            this.player2.gameColor = Color.BLACK;
            this.currentPlayer = player1;
        }
        else if (result == 1){
            this.player2.gameColor = Color.WHITE;
            this.player1.gameColor = Color.BLACK;
            this.currentPlayer = player2;
        }
    }

    private void sendPlayerColors(){
        for (Client player : players) {
            if (player.gameColor == Color.BLACK) {
                player.WriteMessage(Util.dataToJson(Commands.GAME_COLOR.getValue(), "black".toLowerCase()));
            } else if (player.gameColor == Color.WHITE) {
                player.WriteMessage(Util.dataToJson(Commands.GAME_COLOR.getValue(), "white".toLowerCase()));
            }

        }
    }

    public void MoveFigure(int currentX, int currentY, int destinationX, int destinationY){
        try{
            Figure figure = board.board[currentY][currentX];
            if (figure == null) return;
            if (figure.color != this.currentPlayer.gameColor) return;
            if (currentX == destinationX && currentY == destinationY) return;
            if(figure.CheckMove(destinationX, destinationY, board.board)){
                figure.moved = true;
                board.ApplyMove(figure, destinationX, destinationY);
                board.SendFigures();
                if (!HandlePawnOpponentSide(figure)){
                    NextPlayer();
                    SendCurrentPlayer();
                }
            }
        } catch (Exception e){
            SendAllPlayersError("Error occurred while processing move. Try again later.");
        }


    }
    public void SendFigureSelectionOffer(Client client) {
        client.WriteMessage(Util.dataToJson(Commands.FIGURE_SELECT.getValue(), "Select an figure to spawn. Queen, Rook, Bishop, Knight? "));
    }

    public boolean HandlePawnOpponentSide(Figure figure) {
        if (figure.type != FigureType.PAWN) return false;
        Pawn pawn = (Pawn) figure;
        if(pawn.OnOpponentSide()){
            Client client = this.GetPlayerByFigureColor(pawn);
            client.figureSelectActive = true;
            client.selectFigure = figure;
            SendFigureSelectionOffer(client);
            return true;
        }
        return false;
    }

    public Client GetOpponentByPlayer(Client player) {
        for (Client client : players) {
            if (player != client) return client;
        }
        return null;
    }

    public Client GetPlayerByFigureColor(Figure figure){
        for (Client player : players) {
            if (figure.color == player.gameColor) return player;
        }
        return null;
    }

    public void NextPlayer(){
        if(this.currentPlayer == player1) currentPlayer = player2;
        else currentPlayer = player1;
    }

    public void SendCurrentPlayer(){
        for (Client player : players) {
            player.WriteMessage(Util.dataToJson(Commands.NEXT_PLAYER.getValue(), this.currentPlayer.name));
        }
    }

    public void SendAllPlayersError(String message){
        for (Client player : players) {
            player.WriteMessage(Util.dataToJson(Commands.ERROR.getValue(), message));
        }
    }

    private void sendGameStart(){
        for (Client player : players) {
            player.WriteMessage(Util.dataToJson(Commands.START_GAME.getValue()));
        }
    }

    private void setPlayerInGame(){
        for (Client player : players) {
            player.inGame = true;
            player.game = this;
        }
    }

}
