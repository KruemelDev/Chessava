package com.kruemel.chessava.server.game;

import com.kruemel.chessava.server.clientHandling.Client;
import com.kruemel.chessava.shared.networking.Commands;
import com.kruemel.chessava.shared.networking.Util;
import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.figureTypes.*;

import java.awt.Color;

public class Board {
    public Figure[][] figures = {
            {new Rook(Color.BLACK, 0, 1), new Knight(Color.BLACK, 1, 0), new Bishop(Color.BLACK, 2, 0), new Queen(Color.BLACK, 3, 0), new King(Color.BLACK, 4, 0), new Bishop(Color.BLACK, 5, 0), new Knight(Color.BLACK, 6, 0), new Rook(Color.BLACK, 7, 0)},
            {new Pawn(Color.BLACK, 0, 1), new Pawn(Color.BLACK, 1, 1), new Pawn(Color.BLACK, 2,1), new Pawn(Color.BLACK, 3, 1), new Pawn(Color.BLACK, 4, 1), new Pawn(Color.BLACK, 5, 1), new Pawn(Color.BLACK, 6, 1), new Pawn(Color.BLACK, 7, 1)},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {new Pawn(Color.WHITE, 0, 6), new Pawn(Color.WHITE, 1, 6), new Pawn(Color.WHITE, 2, 6), new Pawn(Color.WHITE, 3, 6), new Pawn(Color.WHITE, 4, 6), new Pawn(Color.WHITE, 5, 6), new Pawn(Color.WHITE, 6, 6), new Pawn(Color.WHITE, 7, 6)},
            {new Rook(Color.WHITE, 0, 7), new Knight(Color.WHITE, 1, 7), new Bishop(Color.WHITE, 2, 7), new Queen(Color.WHITE, 3, 7), new King(Color.WHITE, 4, 7), new Bishop(Color.WHITE, 5, 7), new Knight(Color.WHITE, 6, 7), new Rook(Color.WHITE, 7, 7)}
    };

    Client[] players;
    public Board(Client[] players) {
        this.players = players;
    }

    public void ApplyMove(Figure figure, int destinationX, int destinationY) {
        figures[figure.y][figure.x] = null;
        figures[destinationY][destinationX] = figure;
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
