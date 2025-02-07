package com.kruemel.chessava.shared.game.figureTypes;

import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;

import java.awt.Color;

public class Queen extends Figure {
    String imagePath;
    public Queen(Color color) {
        super(color, "/images/queen");
        this.type = FigureType.QUEEN;

    }
    @Override
    public boolean CheckMove(int row, int col) {
        return false;
    }
}
