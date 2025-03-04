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
    public boolean HandleKingDanger(int x, int y, boolean canMove, Figure[][] board) {
        King king = King.GetKing(this.color, board);
        if (king == null) return false;
        if (!canMove) return false;

        Figure[][] tempBoard = DeepCopyBoard(board);
        int oldX = this.x;
        int oldY = this.y;
        Figure targetFigure = tempBoard[y][x];
        King tempKing = King.GetKing(this.color, tempBoard);
        if (tempKing == null) return false;

        if (targetFigure != null && this.color == targetFigure.color) {
            return false;
        }
        tempBoard[oldY][oldX] = null;
        tempBoard[y][x] = this;

        this.x = x;
        this.y = y;

        boolean kingStillInDanger = king.InDanger(tempBoard);

        this.x = oldX;
        this.y = oldY;

        return kingStillInDanger;
    }

    public static Figure[][] DeepCopyBoard(Figure[][] board){
        Figure[][] copy = new Figure[board.length][board[0].length];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                Figure figure = board[y][x];
                if (figure == null) {
                    copy[y][x] = null;
                    continue;
                }
                copy[y][x] = figure.type.createInstance(figure.color, figure.x, figure.y);
            }
        }
        return copy;
    }

    public boolean CheckStraightInfiniteMovement(int x, int y, Figure[][] board){
        if (this.x != x && this.y != y) {
            return false;
        }
        Figure figure = board[y][x];
        if (figure != null) if(this.color == figure.color) return false;

        int stepX = Integer.compare(x, this.x);
        int stepY = Integer.compare(y, this.y);

        int currentX = this.x + stepX;
        int currentY = this.y + stepY;
        while (currentX != x || currentY != y) {
            if (board[currentY][currentX] != null) {
                return false;
            }
            currentX += stepX;
            currentY += stepY;
        }
        return true;
    }

    public boolean CheckDiagonalInfiniteMovement(int x, int y, Figure[][] board){
        int yDiff = Math.abs(this.y - y);
        int xDiff = Math.abs(this.x - x);
        if (yDiff != xDiff) return false;

        Figure figure = board[y][x];
        if (figure != null) if (this.color == figure.color) return false;

        int stepX = Integer.compare(x, this.x);
        int stepY = Integer.compare(y, this.y);

        int currentX = this.x + stepX;
        int currentY = this.y + stepY;
        while(currentX != x && currentY != y) {
            if (board[currentY][currentX] != null) {
                return false;
            }
            currentX += stepX;
            currentY += stepY;
        }
        return true;
    }


    // Do not use CheckAttack outside this package
    public abstract boolean CheckAttack(int x, int y, Figure[][] board);
    public abstract boolean CheckMove(int x, int y, Figure[][] board);
}
