package com.kruemel.chessava.server.game;

import com.kruemel.chessava.server.clientHandling.Client;
import com.kruemel.chessava.shared.networking.Commands;
import com.kruemel.chessava.shared.networking.Util;
import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.figureTypes.*;

import java.awt.Color;

public class Board {
    public Figure[][] figures = {
            {new Rook(Color.BLACK), new Knight(Color.BLACK), new Bishop(Color.BLACK), new Queen(Color.BLACK), new King(Color.BLACK), new Bishop(Color.BLACK), new Knight(Color.BLACK), new Rook(Color.BLACK)},
            {new Pawn(Color.BLACK), new Pawn(Color.BLACK), new Pawn(Color.BLACK), new Pawn(Color.BLACK), new Pawn(Color.BLACK), new Pawn(Color.BLACK), new Pawn(Color.BLACK), new Pawn(Color.BLACK)},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {new Pawn(Color.WHITE), new Pawn(Color.WHITE), new Pawn(Color.WHITE), new Pawn(Color.WHITE), new Pawn(Color.WHITE), new Pawn(Color.WHITE), new Pawn(Color.WHITE), new Pawn(Color.WHITE)},
            {new Rook(Color.WHITE), new Knight(Color.WHITE), new Bishop(Color.WHITE), new Queen(Color.WHITE), new King(Color.WHITE), new Bishop(Color.WHITE), new Knight(Color.WHITE), new Rook(Color.WHITE)}
    };



    Client[] players;
    public Board(Client[] players) {
        this.players = players;
    }

    public void SendFigures(){
        StringBuilder board = new StringBuilder();
        for (int i = 0; i < figures.length; i++) {
            for (int j = 0; j < figures[i].length; j++) {
                Figure figure = figures[i][j];
                if(figure == null){
                    board.append("null").append("|");
                    continue;
                }
                board.append(figure.type.getValue()).append("(").append(figure.colorToString()).append(")").append("|");
            }
        }

        if (!board.isEmpty()) {
            board.setLength(board.length() - 1);
        }
        for (Client player : players) {
            player.WriteMessage(Util.dataToJson(Commands.SET_FIGURES.getValue(), board.toString()));
        }

    }

}
