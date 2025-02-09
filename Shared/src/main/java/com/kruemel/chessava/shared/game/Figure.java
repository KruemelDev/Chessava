package com.kruemel.chessava.shared.game;

import com.kruemel.chessava.shared.game.figureTypes.Bishop;
import com.kruemel.chessava.shared.game.figureTypes.King;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public abstract class Figure {

    public Color color;
    public int x, y;
    public FigureType type;

    public Image image;
    public String imageHalfPath;

    public boolean moved = false;

    public Figure(Color color, String imagePath, int x, int y) {
        this.color = color;
        this.imageHalfPath = imagePath + "_" + colorToString() + ".png";
        this.x = x;
        this.y = y;

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
    public boolean HandleKingDanger(boolean canMove, Figure[][] board){
        King king = King.getKing(this.color, board);
        if(king == null) return false;
        if (king.InDanger(king.x, king.y, board)) {
            if(canMove) {
                Figure[][] tempBoard = board.clone();
                tempBoard[y][x] = this;
                return king.InDanger(king.x, king.y, tempBoard);
            }
        }
        return false;
    }
    public abstract boolean CheckMove(int x, int y, Figure[][] board);
}
