package com.kruemel.chessava.shared.game.figureTypes;

import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;
import java.awt.Color;

public class Queen extends Figure {
    public Queen(Color color) {
        super(color);
        type = FigureType.QUEEN;

    }
    @Override
    public boolean CheckMove(int row, int col) {
        return false;
    }
}
