package com.kruemel.chessava.shared.game.figureTypes;

import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;

import java.awt.Color;

public class Queen extends Figure {
    public Queen(Color color, int x, int y) {
        super(color, "/images/queen", x, y);
        this.type = FigureType.QUEEN;

    }
    @Override
    public boolean CheckMove(int x, int y, Figure[][] board) {
        return false;
    }
}
