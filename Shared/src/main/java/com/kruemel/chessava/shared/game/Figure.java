package com.kruemel.chessava.shared.game;

import com.kruemel.chessava.shared.game.figureTypes.Bishop;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public abstract class Figure {

    public Color color;
    public int row, col;
    public FigureType type;

    public Image image;
    public String imageHalfPath;

    public Figure(Color color, String imagePath) {
        this.color = color;
        this.imageHalfPath = imagePath + "_" + colorToString() + ".png";

        loadImage();

    }
    public void loadImage() {
        try {
            this.image = ImageIO.read(Objects.requireNonNull(Bishop.class.getResource(imageHalfPath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
