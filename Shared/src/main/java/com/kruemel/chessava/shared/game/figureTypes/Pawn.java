package com.kruemel.chessava.shared.game.figureTypes;

import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;

import java.awt.Color;

public class Pawn extends Figure {
    public Pawn(Color color, int x, int y) {
        super(color, "/images/pawn", x, y);
        this.type = FigureType.PAWN;
    }
    
    public boolean OnOpponentSide(){
        if(this.color == Color.BLACK && this.y == 7) return true;
        else if(this.color == Color.WHITE && this.y == 0) return true;
        return false;
    } 
    
    
    @Override
    public boolean CheckAttack(int x, int y, Figure[][] board) {
        Figure figure = board[y][x];
        if(color == Color.BLACK) {
            if(figure != null) {
                return (this.y + 1 == y && (this.x + 1 == figure.x || this.x - 1 == figure.x)) && figure.color != color;
            }
        }
        else if(color == Color.WHITE) {
            if(figure != null) {
                return (this.y - 1 == y && (this.x + 1 == figure.x || this.x - 1 == figure.x)) && figure.color != color;
            }
        }
        return false;
    }

    private boolean CheckStraightMove(int x, int y, Figure[][] board, Figure figure) {
        if(color == Color.BLACK) {
            return ((this.y + 1 == y) || (this.y + 2 == y && !moved && board[this.y + 1][x] == null)) && figure == null && this.x == x;
        } else if(color == Color.WHITE) {
            return ((this.y - 1 == y) || (this.y - 2 == y && !moved && board[this.y - 1][x] == null)) && figure == null && this.x == x;
        }
        return false;
    }

    @Override
    public boolean CheckMove(int x, int y, Figure[][] board) {
        boolean straightMove;
        boolean diagonalMove;
        Figure figure = board[y][x];

        straightMove = CheckStraightMove(x, y, board, figure);
        diagonalMove = CheckAttack(x, y, board);

        boolean canMove = straightMove || diagonalMove;

        boolean danger = HandleKingDanger(x, y, canMove, board);
        if(danger) System.out.println("King in danger: " + danger);
        return canMove && !danger;

    }
}
