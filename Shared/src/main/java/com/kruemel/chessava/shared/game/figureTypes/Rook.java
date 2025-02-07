package com.kruemel.chessava.shared.game.figureTypes;

import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;

import java.awt.Color;

public class Rook extends Figure {

    public Rook(Color color) {
        super(color, "/images/rook");
        this.type = FigureType.ROOK;

    }
    @Override
    public boolean CheckMove(int row, int col) {
        return false;
    }
}
