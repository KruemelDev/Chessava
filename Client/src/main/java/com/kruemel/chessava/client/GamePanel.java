package com.kruemel.chessava.client;

import com.kruemel.chessava.client.player.Player;
import com.kruemel.chessava.server.Server;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public static int port = 45565;
    public String singlePlayerIp = "127.0.0.1";
    public static String multiPlayerIp;

    public Player[] players = new Player[2];

    private static Server server;

    public GameMode gameMode;
    public GamePanel(GameMode gameMode) {
        setPreferredSize(new Dimension(MainFrameManager.instance.screenWidth, MainFrameManager.instance.screenHeight));
        setDoubleBuffered(true);
        setBackground(Color.GRAY);
        this.setFocusable(true);

        handleGameMode(gameMode);
    }

    private void handleGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        switch (gameMode) {
            case SINGLE_PLAYER:
                int singlePlayerPort = port + 2;

                startServerIfPossible(singlePlayerPort);
                initNewPlayer(singlePlayerIp, singlePlayerPort, 2);
                break;
            case MULTI_PLAYER:
                initNewPlayer(singlePlayerIp, port, 1);
                break;
        }
    }

    private void startServerIfPossible(int port) {
        if(server == null){
            server = new Server(port);
            server.InitServer();
            new Thread(server).start();
        }
    }

    public void DestroyPlayer() {
        for (Player player : players) {
            player.connectionHandler.SendCloseConnection("Destroy");
            player = null;
        }
    }

    private void initNewPlayer(String ip, int singlePlayerPort, int amount) {
        for(int i = 0; i < amount; i++){
            String name = askForName();
            if(name == null && gameMode == GameMode.SINGLE_PLAYER) {
                DestroyPlayer();
                MainFrameManager.instance.GameModeSelectionScreen();
                break;
            } else if(name == null && gameMode == GameMode.MULTI_PLAYER) break;
            players[i] = new Player(ip, singlePlayerPort, name, this);
            players[i].InitPlayer();
        }
    }



    private String askForName() {
        while(true){
            String name = JOptionPane.showInputDialog(
                    MainFrameManager.instance.mainFrame, "How should your friends find you? ", "Player"
            );
            if(name == null) return null;
            if(!name.isEmpty() && !name.contains("|"))return name;
        }


    }

    public static void setMultiPlayerDestination(String ip, int port){
        GamePanel.port = port;
        GamePanel.multiPlayerIp = ip;
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
