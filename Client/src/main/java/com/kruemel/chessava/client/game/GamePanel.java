package com.kruemel.chessava.client.game;

import com.kruemel.chessava.client.MainFrameManager;
import com.kruemel.chessava.client.player.Player;
import com.kruemel.chessava.server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel {

    public static int port = 45565;
    public String singlePlayerIp = "127.0.0.1";
    public static String multiPlayerIp;

    public int gamePanelSize = 800 - MainFrameManager.instance.mainFrame.getInsets().top;

    public Player[] players = new Player[2];

    private static Server server;

    public GameMode gameMode;
    public Board board = new Board(this);

    public GamePanel(GameMode gameMode) {
        setPreferredSize(new Dimension(gamePanelSize, gamePanelSize));
        setMaximumSize(new Dimension(gamePanelSize, gamePanelSize));
        setDoubleBuffered(true);
        setBackground(Color.GRAY);
        this.setFocusable(true);

        handleGameMode(gameMode);

    }

    public void addMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println("pressed at x: " + e.getX() + "pressed at y: " + e.getY());
            }
        });
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
            if (player == null) continue;
            player.connectionHandler.SendCloseConnection("Destroy");
            player = null;
        }
    }

    private void initNewPlayer(String ip, int singlePlayerPort, int amount) {
        for(int i = 0; i < amount; i++){
            String name = askForName();
            if(name == null) {
                DestroyPlayer();
                MainFrameManager.instance.GameModeSelectionScreen();
                break;
            }
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
    public boolean BattleRequest(String name){
        int result = JOptionPane.showConfirmDialog(
                MainFrameManager.instance.mainFrame,
                "Battle " + name + "?",
                "Battle Request",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }

    public static void setMultiPlayerDestination(String ip, int port){
        GamePanel.port = port;
        GamePanel.multiPlayerIp = ip;
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        board.Paint(g2d);

        g2d.dispose();
    }


}
