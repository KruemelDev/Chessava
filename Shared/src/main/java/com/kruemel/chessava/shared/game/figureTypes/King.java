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
    public boolean CheckAttack(int x, int y, Figure[][] board) {
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

        return topMove || topLeftMove || topRightMove || downMove || downLeftMove || downRightMove || leftMove || rightMove;
    }

    @Override
    public boolean CheckMove(int x, int y, Figure[][] board) {
        Figure figure = board[y][x];
        boolean canMove = CheckAttack(x, y, board);

        boolean move = canMove && checkOpponentFigures(x, y, board);
        if(figure != null) return move && this.color != figure.color;
        else return move;

    }
    private boolean checkOpponentFigures(int destinationX, int destinationY, Figure[][] board) {
        return checkOpponentKingDistance(destinationX, destinationY, board) && !InDanger(destinationX, destinationY, board);
    }

    public boolean InDanger(int posX, int posY, Figure[][] board) {
        Figure[][] tempBoard = new Figure[board.length][];
        for (int i = 0; i < board.length; i++) {
            tempBoard[i] = board[i].clone();
        }
        tempBoard[posY][posX] = new King(this.color, posX, posY);

        for (Figure[] row : board) {
            for (Figure enemy : row) {
                if (enemy == null || enemy.type == FigureType.KING || enemy.color == this.color) continue;
                // TODO create temp king at move pos to check if king is in danger when he moves

                if (enemy.CheckAttack(posX, posY, tempBoard)) {
                    for (Figure[] allyRow : board) {
                        for (Figure ally : allyRow) {
                            if (ally == null || ally.type == FigureType.KING || ally.color != this.color) continue;
                            if (!ally.CheckAttack(enemy.x, enemy.y, board)) return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean checkOpponentKingDistance ( int destinationX, int destinationY, Figure[][] board){
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Figure figure = board[y][x];
                if (figure == null) continue;
                if (figure.type == FigureType.KING && figure.color != this.color) {
                    return Math.abs(destinationX - figure.x) > 1 || Math.abs(destinationY - figure.y) > 1;
                }

            }
        }
        return false;
    }
    public static King getKing (Color color, Figure[][]board){
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                Figure figure = board[y][x];
                if (figure == null) continue;
                if (figure.type == FigureType.KING && figure.color == color) return (King) figure;
            }
        }
        return null;
    }
}
