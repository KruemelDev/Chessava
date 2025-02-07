package com.kruemel.chessava.shared.game.figureTypes;

import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;

import java.awt.Color;

public class Knight extends Figure {
    public Knight(Color color) {
        super(color, "/images/knight");
        this.type = FigureType.KNIGHT;

    }

    @Override
    public boolean CheckMove(int row, int col) {
        return false;
    }
}
