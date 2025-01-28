package com.kruemel.chessava.server;


public class Main {
    public static Server server;

    public static void main(String[] args) {
        server = new Server(Integer.parseInt(args[0]));
        server.InitServer();
        new Thread(server).start();

    }
    public Main(int port){
        server = new Server(port);
        server.InitServer();
        new Thread(server).start();
    }
}