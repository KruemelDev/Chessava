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
    public boolean CheckAttack(int targetX, int targetY, Figure[][] board) {
        if (this.x != targetX && this.y != targetY) {
            return false;
        }
        Figure figure = board[targetY][targetX];
        if (figure != null) {
            if(this.color == figure.color) return false;
        }

        int stepX = Integer.compare(targetX, this.x);
        int stepY = Integer.compare(targetY, this.y);

        int currentX = this.x + stepX;
        int currentY = this.y + stepY;
        while (currentX != targetX || currentY != targetY) {
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

        return canMove && !danger;
    }
}
