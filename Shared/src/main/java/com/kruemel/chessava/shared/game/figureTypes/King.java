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
        // TODO implement castling

        Figure figure = board[y][x];
        boolean canMove = CheckAttack(x, y, board);
        if (canMove) {
            try{
                Figure dest = board[y][x];
            } catch (IndexOutOfBoundsException e){
                return false;
            }

        }

        boolean move = canMove && checkOpponentFigures(x, y, canMove, board);
        if(figure != null) return move && this.color != figure.color;
        else return move;

    }
    private boolean checkOpponentFigures(int destinationX, int destinationY, boolean canMove, Figure[][] board) {
        return checkOpponentKingDistance(destinationX, destinationY, board) && !HandleKingDanger(destinationX, destinationY, canMove, board);
    }

    private boolean checkOpponentKingDistance(int destinationX, int destinationY, Figure[][] board){
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


    public boolean InDanger(Figure[][] board) {
        if (!isValidPosition(this.x, this.y, board)) {
            return false;
        }
        return isAttacked(board);
    }


    private boolean isValidPosition(int x, int y, Figure[][] board) {
        return x >= 0 && x < board[0].length && y >= 0 && y < board.length;
    }

    private boolean isAttacked(Figure[][] board) {


        for (Figure[] row : board) {
            for (Figure enemy : row) {
                if (enemy == null || enemy.color == this.color || enemy.type == FigureType.KING) {
                    continue;
                }
                if (enemy.CheckAttack(this.x, this.y, board)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean CheckChessMate(Figure[][] board) {
        Figure[][] copy = DeepCopyBoard(board);

        if (!InDanger(copy)) {
            return false;
        }

        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newX = this.x + dx[i];
            int newY = this.y + dy[i];

            if (isValidPosition(newX, newY, copy)) {
                Figure target = copy[newY][newX];
                if (target == null || target.color != this.color) {
                    int oldX = this.x;
                    int oldY = this.y;

                    this.x = newX;
                    this.y = newY;
                    if (!InDanger(copy)) {
                        return false;
                    }
                    this.x = oldX;
                    this.y = oldY;
                }
            }
        }

        return true;
    }


    public static King GetKing(Color color, Figure[][]board){
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
