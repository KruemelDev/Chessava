package com.kruemel.chessava.server;

import com.kruemel.chessava.server.clientHandling.Client;

public class Game {
    public Client player1, player2;

    public Game(Client player1, Client player2) {
        this.player1 = player1;
        this.player2 = player2;
    }


}
