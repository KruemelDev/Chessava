package com.kruemel.chessava.shared.game.figureTypes;

import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;

import java.awt.Color;

public class Pawn extends Figure {
    String imagePath;
    public Pawn(Color color) {
        super(color, "/images/pawn");
        this.type = FigureType.PAWN;
    }

    @Override
    public boolean CheckMove(int row, int col) {
        return false;
    }
}
