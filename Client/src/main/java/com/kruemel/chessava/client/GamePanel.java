package com.kruemel.chessava.client;

import com.kruemel.chessava.client.player.Player;
import com.kruemel.chessava.server.Main;
import com.kruemel.chessava.server.Server;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    public String ip;
    public int port = 45565;

    Player[] players = new Player[2];

    private static Server server;


    public GamePanel(GameMode gameMode) {
        setPreferredSize(new Dimension(MainFrameManager.instance.screenWidth, MainFrameManager.instance.screenHeight));
        setDoubleBuffered(true);
        setBackground(Color.GRAY);
        this.setFocusable(true);

        handleGameMode(gameMode);
    }

    private void handleGameMode(GameMode gameMode) {
        switch (gameMode) {
            case singlePlayer:
                ip = "127.0.0.1";
                int singlePlayerPort = port + 2;
                if(server == null || !server.running){
                    System.out.println(server);
                    server = new Server(singlePlayerPort);
                    server.InitServer();
                    new Thread(server).start();
                }
                players[0] = new Player(ip, singlePlayerPort, this);
                players[1] = new Player(ip, singlePlayerPort, this);
                break;
            case multiPlayer:
                ip = "192.168.66.66";
                players[0] = new Player(ip, port, this);
                break;
        }
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        g2d.fillRect(0, 0, 30, 30);

        g2d.dispose();
    }


}
