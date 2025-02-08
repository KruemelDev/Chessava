package com.kruemel.chessava.shared.game.figureTypes;

import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;

import java.awt.*;

public class Bishop extends Figure {
    public Bishop(Color color, int x, int y) {
        super(color, "/images/bishop", x, y);
        this.type = FigureType.BISHOP;

    }
    @Override
    public boolean CheckMove(int x, int y, Figure[][] board) {
        return false;
    }
}
