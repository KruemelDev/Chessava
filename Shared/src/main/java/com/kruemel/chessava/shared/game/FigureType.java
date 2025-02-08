package com.kruemel.chessava.shared.game;

import com.kruemel.chessava.shared.game.figureTypes.Pawn;
import com.kruemel.chessava.shared.game.figureTypes.Knight;
import com.kruemel.chessava.shared.game.figureTypes.Bishop;
import com.kruemel.chessava.shared.game.figureTypes.Rook;
import com.kruemel.chessava.shared.game.figureTypes.Queen;
import com.kruemel.chessava.shared.game.figureTypes.King;

import java.awt.*;


public enum FigureType {
    PAWN(Pawn.class, "Pawn"),
    KNIGHT(Knight.class, "Knight"),
    BISHOP(Bishop.class, "Bishop"),
    ROOK(Rook.class, "Rook"),
    QUEEN(Queen.class, "Queen"),
    KING(King.class, "King");

    private final Class<? extends Figure> figureClass;
    private final String name;

    FigureType(Class<? extends Figure> figureClass, String name) {
        this.figureClass = figureClass;
        this.name = name;
    }

    public String getValue() {
        return name;
    }

    public Figure createInstance(Color color, int x, int y) {
        try {
            return figureClass.getDeclaredConstructor(Color.class, int.class, int.class).newInstance(color, x, y);
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Erstellen der Instanz", e);
        }
    }

}
