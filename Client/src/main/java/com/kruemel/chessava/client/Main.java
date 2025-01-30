package com.kruemel.chessava.client;


import com.kruemel.chessava.shared.Commands;
import com.kruemel.chessava.shared.Util;
public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");

        new MainFrameManager("Chessava", 1280, 720);
        System.out.println(Commands.NAME.getValue());
    }
}