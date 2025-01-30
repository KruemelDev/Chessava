package com.kruemel.chessava.shared;

public enum Commands {
    NAME("Name"),
    CLOSE_CONNECTION("CloseConnection");


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