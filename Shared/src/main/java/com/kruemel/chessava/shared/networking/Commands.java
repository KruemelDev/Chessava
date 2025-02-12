package com.kruemel.chessava.shared.networking;

public enum Commands {
    NAME("Name"),
    CLOSE_CONNECTION("CloseConnection"),
    PLAYER_AVAILABLE("PlayerAvailable"),
    BATTLE_REQUEST("BattleRequest"),
    BATTLE_ACCEPT("BattleAccept"),
    BATTLE_DECLINE("BattleDecline"),
    START_GAME("StartGame"),
    GAME_COLOR("GameColor"),
    SET_FIGURES("SetFigures"),
    MOVE_FIGURE("MoveFigure"),
    FIGURE_SELECT("FigureSelect"),
    NEXT_PLAYER("NextPlayer"),
    END_GAME("EndGame"),
    ERROR("Error");

    private final String command;

    Commands(String command) {
        this.command = command;

    }

    public String getValue() {
        return command;
    }

    public static Commands fromString(String text) {
        for (Commands cmd : Commands.values()) {
            if (cmd.command.equalsIgnoreCase(text)) {
                return cmd;
            }
        }
        throw new IllegalArgumentException("Unbekannter Befehl: " + text);
    }

}