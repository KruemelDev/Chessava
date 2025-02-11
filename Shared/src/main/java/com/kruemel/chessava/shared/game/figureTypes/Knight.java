package com.kruemel.chessava.shared.game.figureTypes;

import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;

import java.awt.Color;

public class Knight extends Figure {
    public Knight(Color color, int x, int y) {
        super(color, "/images/knight", x , y);
        this.type = FigureType.KNIGHT;

    }

    @Override
    public boolean CheckAttack(int x, int y, Figure[][] board) {
        Figure figure = board[y][x];

        if (figure != null) {
            if (this.color == figure.color) return false;
        }
        int xDiff = Math.abs(this.x - x);
        int yDiff = Math.abs(this.y - y);

        return (xDiff == 1 && yDiff == 2) || (xDiff == 2 && yDiff == 1);
    }

    @Override
    public boolean CheckMove(int x, int y, Figure[][] board) {
        boolean canMove = CheckAttack(x, y, board);
        boolean danger = HandleKingDanger(x, y, canMove, board);
        if(danger) System.out.println("King in danger: " + danger);
        return canMove && !danger;
    }
}
