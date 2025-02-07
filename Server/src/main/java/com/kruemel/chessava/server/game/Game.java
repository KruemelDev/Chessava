package com.kruemel.chessava.server.game;

import com.kruemel.chessava.server.clientHandling.Client;
import com.kruemel.chessava.shared.networking.Commands;
import com.kruemel.chessava.shared.networking.Util;

public class Game {

    public Client player1, player2;
    public Client[] players;

    public Board board;

    public Game(Client player1, Client player2) {
        this.player1 = player1;
        this.player2 = player2;

        players = new Client[]{player1, player2};
        board = new Board(players);
    }
    public void InitGame(){
        setPlayerInGame();
        sendGameStart();
        board.SendFigures();
    }

    private void sendGameStart(){
        for (Client player : players) {
            player.WriteMessage(Util.dataToJson(Commands.START_GAME.getValue()));
        }
    }

    private void setPlayerInGame(){
        this.player1.inGame = true;
        this.player2.inGame = true;
    }

}
