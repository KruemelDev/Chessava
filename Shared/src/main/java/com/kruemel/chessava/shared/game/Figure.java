package com.kruemel.chessava.shared.game;

import java.awt.*;

public abstract class Figure {

    public Color color;
    public int row, col;
    public FigureType type;

    public Image image;

    public Figure(Color color) {
        this.color = color;

    }

    public String colorToString() {
        if (color == Color.BLACK) {
            return "black";
        }
        else if (color == Color.WHITE) {
            return "white";
        }
        return null;
    }

    public abstract boolean CheckMove(int row, int col);
}
