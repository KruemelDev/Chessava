package com.kruemel.chessava.shared.game.figureTypes;

import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;

import java.awt.Color;


public class King extends Figure {
    public King(Color color, int x, int y) {
        super(color, "/images/king", x, y);
        this.type = FigureType.KING;
    }

    @Override
    public boolean CheckMove(int x, int y, Figure[][] board) {
        return false;
    }
}
