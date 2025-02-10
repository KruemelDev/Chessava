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
        return false;
    }

    @Override
    public boolean CheckMove(int x, int y, Figure[][] board) {
        return false;
    }
}
