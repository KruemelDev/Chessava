package com.kruemel.chessava.shared.game.figureTypes;

import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;
import java.awt.Color;

public class King extends Figure {
    public King(Color color) {
        super(color);
        type = FigureType.KING;

    }
    @Override
    public boolean CheckMove(int row, int col) {
        return false;
    }
}
