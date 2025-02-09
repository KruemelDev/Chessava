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
        Figure figure = board[y][x];
        boolean topMove;
        boolean topLeftMove;
        boolean topRightMove;
        boolean downMove;
        boolean downLeftMove;
        boolean downRightMove;
        boolean leftMove;
        boolean rightMove;

        topMove = this.y - 1 == y && this.x == x;
        topLeftMove = this.y - 1 == y && this.x - 1 == x;
        topRightMove = this.y - 1 == y && this.x + 1 == x;
        downMove = this.x == x && this.y + 1 == y;
        downLeftMove = this.y + 1 == y && this.x - 1 == x;
        downRightMove = this.y + 1 == y && this.x + 1 == x;
        leftMove = this.x - 1 == x && this.y == y;
        rightMove = (this.x + 1 == x && this.y == y);

        boolean move = (topMove || topLeftMove || topRightMove || downMove || downLeftMove || downRightMove || leftMove || rightMove) && checkOpponentFigures(x, y, board);

        if(figure != null) return move && this.color != figure.color;
        else return move;

    }
    private boolean checkOpponentFigures(int destinationX, int destinationY, Figure[][] board) {
        return checkOpponentKingDistance(destinationX, destinationY, board) && !InDanger(destinationX, destinationY, board);
    }

    public boolean InDanger(int posX, int posY, Figure[][] board) {
        boolean kingInDanger = true;
        for(int y = 0; y < board.length; y++) {
            for(int x = 0; x < board.length; x++) {
                Figure figure = board[y][x];
                if(figure == null) continue;
                if (figure.type == FigureType.KING) continue;

                if(figure.type == FigureType.PAWN){
                    Pawn pawn = (Pawn) figure;
                    King tempKing = new King(this.color, posX, posY);
                    if(pawn.CheckDiagonal(posX, posY, tempKing)) return true;
                }
                else{
                    if(figure.CheckMove(posX, posY, board)) return true;
                }
                kingInDanger = false;
            }
        }
        return kingInDanger;
    }

    private boolean checkOpponentKingDistance(int destinationX, int destinationY, Figure[][] board){
        for(int y = 0; y < 8; y++){
            for (int x = 0; x < 8; x++){
                Figure figure = board[y][x];
                if (figure == null) continue;
                if (figure.type == FigureType.KING && figure.color != this.color) {
                    return Math.abs(destinationX - figure.x) > 1 || Math.abs(destinationY - figure.y) > 1;
                }

            }
        }
        return false;
    }
    public static King getKing(Color color, Figure[][] board) {
        for (int y = 0; y < board.length; y++){
            for (int x = 0; x < board.length; x++){
                Figure figure = board[y][x];
                if (figure == null) continue;
                if (figure.type == FigureType.KING && figure.color == color) return (King) figure;
            }
        }
        return null;
    }
}
