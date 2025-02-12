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
        return CheckStraightInfiniteMovement(x, y, board);
    }

    @Override
    public boolean CheckMove(int x, int y, Figure[][] board) {
        boolean canMove = CheckAttack(x, y, board);
        boolean danger = HandleKingDanger(x, y, canMove, board);
        if(danger) System.out.println("King in danger: " + danger);

        return canMove && !danger;
    }
}
