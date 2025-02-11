package com.kruemel.chessava.shared.game.figureTypes;

import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;

import java.awt.Color;

public class Rook extends Figure {

    public Rook(Color color, int x, int y) {
        super(color, "/images/rook", x, y);
        this.type = FigureType.ROOK;

    }
    @Override
    public boolean CheckAttack(int x, int y, Figure[][] board) {
        if (this.x != x && this.y != y) {
            return false;
        }
        Figure figure = board[y][x];
        if (figure != null) {
            if(this.color == figure.color) return false;
        }

        int stepX = Integer.compare(x, this.x);
        int stepY = Integer.compare(y, this.y);

        int currentX = this.x + stepX;
        int currentY = this.y + stepY;
        while (currentX != x || currentY != y) {
            if (board[currentY][currentX] != null) {
                return false;
            }
            currentX += stepX;
            currentY += stepY;
        }
        return true;
    }

    @Override
    public boolean CheckMove(int x, int y, Figure[][] board) {
        boolean canMove = CheckAttack(x, y, board);
        boolean danger = HandleKingDanger(x, y, canMove, board);
        if(danger) System.out.println("King in danger: " + danger);

        return canMove && !danger;
    }
}
