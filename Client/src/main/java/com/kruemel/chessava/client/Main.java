package com.kruemel.chessava.client;

public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");

        new MainFrameManager("Chessava", 1280, 720);
    }
}