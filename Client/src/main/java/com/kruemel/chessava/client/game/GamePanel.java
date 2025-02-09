package com.kruemel.chessava.client.game;

import com.kruemel.chessava.client.MainFrameManager;
import com.kruemel.chessava.client.player.Player;
import com.kruemel.chessava.server.Server;
import com.kruemel.chessava.shared.networking.Commands;
import com.kruemel.chessava.shared.networking.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel {

    public static int port = 45565;
    public String singlePlayerIp = "127.0.0.1";
    public static String multiPlayerIp;

    public int gamePanelSize = 800 - MainFrameManager.instance.mainFrame.getInsets().top;
    public int tileSize = gamePanelSize / 8;

    public Player[] players = new Player[2];
    public String currentPlayerName;

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
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                handleMouseEvent(e);

            }
        });
    }
    private void handleMouseEvent(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
            e.consume();
            float x = getClickedPos(e.getX());
            float y = getClickedPos(e.getY());

            markField((int) x,(int) y);

        }
        else if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
            e.consume();
            placeFigure((int) getClickedPos(e.getX()), (int) getClickedPos(e.getY()));
        }
    }
    public Player GetCurrentPlayer() {
        if (currentPlayerName == null) return null;
        for (Player player : players) {
            if (player.name.equals(currentPlayerName)) {
                return player;
            }
        }
        return null;
    }

    private void placeFigure(int x, int y) {
        if(board.selectedFigure == null) return;
        Player player = GetCurrentPlayer();
        if(player == null) return;
        if (board.selectedFigure.color != player.color) return;
        if (board.selectedFigure.CheckMove(x, y, board.figures)) {
            player.connectionHandler.WriteMessage(Util.dataToJson(Commands.MOVE_FIGURE.getValue(), board.selectedFigure.x + "|" + board.selectedFigure.y + "|" + x + "|" + y));
        }
    }
    private void markField(int x, int y){
        board.clickedFieldX = x;
        board.clickedFieldY = y;
        board.selectedFigure = board.figures[y][x];
        repaint();
    }

    private float getClickedPos(float pos) {
        float clickedPos = (pos / gamePanelSize) * ((float) gamePanelSize / 100);
        float rounded = Math.round(clickedPos);
        if (rounded == 8) rounded = 7;
        return rounded;
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
