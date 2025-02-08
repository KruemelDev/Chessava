package com.kruemel.chessava.shared.game.figureTypes;

import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;

import java.awt.Color;

public class Pawn extends Figure {
    String imagePath;
    public Pawn(Color color, int x, int y) {
        super(color, "/images/pawn", x, y);
        this.type = FigureType.PAWN;
    }

    @Override
    public boolean CheckMove(int x, int y, Figure[][] board) {
        Figure figure = board[y][x];
        // TODO fix logic for outest pawns
        boolean straightMove = false;
        boolean diagonalMove = false;
        if(color == Color.BLACK) {
            straightMove = (this.y + 1 == y || this.y + 2 == y) && figure == null && this.x == x;

            if(figure != null) {
                diagonalMove = (this.y + 1 == y && (this.x + 1 == figure.x || this.x - 1 == figure.x)) && figure.color != color;
            }
        }
        else if (color == Color.WHITE) {
            straightMove = (this.y - 1 == y || this.y - 2 == y) && figure == null && this.x == x;

            if(figure != null) {
                diagonalMove = (this.y - 1 == y && (this.x + 1 == figure.x || this.x - 1 == figure.x)) && figure.color != color;
            }

        }
        return straightMove || diagonalMove;

    }
}
