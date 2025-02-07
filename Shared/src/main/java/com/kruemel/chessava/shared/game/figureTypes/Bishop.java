package com.kruemel.chessava.shared.game.figureTypes;

import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;
import java.awt.Color;

public class Bishop extends Figure {
    public Bishop(Color color) {
        super(color);
        type = FigureType.BISHOP;

    }
    @Override
    public boolean CheckMove(int row, int col) {
        return false;
    }
}
