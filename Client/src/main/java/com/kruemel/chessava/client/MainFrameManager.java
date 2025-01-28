package com.kruemel.chessava.client;

import com.kruemel.chessava.client.player.ConnectionHandler;
import com.kruemel.chessava.client.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrameManager {
    public JFrame mainFrame;
    private final String mainFrameTitle;
    public int screenWidth, screenHeight;


    public static MainFrameManager instance;

    public MainFrameManager(String mainFrameTitle, int screenWidth, int screenHeight) {
        instance = this;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.mainFrameTitle = mainFrameTitle;


        mainFrame = new JFrame(this.mainFrameTitle);
        mainFrame.setSize(this.screenWidth, this.screenHeight);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setResizable(true);

        AddGameModeSelectionScreen();
    }

    public void StartGame(GameMode gameMode) {
        mainFrame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(gameMode);
        JPanel buttonPanelSiteBar = AddGameLeaveOption(gamePanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonPanelSiteBar, gamePanel);
        splitPane.setDividerLocation(200);

        mainFrame.add(splitPane);
        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private JPanel AddGameLeaveOption(GamePanel gamePanel) {

        JPanel panel = new JPanel();

        JButton leaveGameButton = new JButton("LeaveGame");
        leaveGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaveGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                for(Player player : gamePanel.players) {
                    player.connectionHandler.CloseConnection();
                }
                AddGameModeSelectionScreen();
            }
        });
        panel.add(leaveGameButton);
        mainFrame.add(panel);
        return panel;
    }

    public void AddGameModeSelectionScreen(){
        mainFrame.getContentPane().removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalGlue());


        JButton startSinglePlayerButton = new JButton("singlePlayer");
        startSinglePlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startSinglePlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                GameMode gameMode = GameMode.singlePlayer;
                StartGame(gameMode);
            }
        });
        panel.add(startSinglePlayerButton);

        JButton startMultiPlayerButton = new JButton("multiPlayer");
        startMultiPlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startMultiPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                GameMode gameMode = GameMode.multiPlayer;
                StartGame(gameMode);
            }
        });
        panel.add(startMultiPlayerButton);

        panel.add(Box.createVerticalGlue());

        mainFrame.add(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

}
