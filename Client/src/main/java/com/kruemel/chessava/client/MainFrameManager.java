package com.kruemel.chessava.client;

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

        GameModeSelectionScreen();
    }

    public void StartGame(GameMode gameMode) {
        mainFrame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(gameMode);

        JPanel gameLeavePanel = AddGameLeaveOption(gamePanel);
        JPanel playerListPanel = AddPlayerList();

        JPanel optionsPanel = new JPanel();
        optionsPanel.add(gameLeavePanel);
        optionsPanel.add(playerListPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, optionsPanel, gamePanel);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.8);

        mainFrame.add(splitPane);
        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void UpdatePlayerList(String[] players){
        playerList.setListData(players);
        mainFrame.revalidate();
        mainFrame.repaint();
        System.out.println(players[0]);
    }
    JList<String> playerList = new JList<>();
    private JPanel AddPlayerList() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel playerListLabel = new JLabel("Player List: ");
        playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(playerList);
        scrollPane.setPreferredSize(new Dimension(180, 150));

        panel.add(playerListLabel);
        panel.add(scrollPane);

        return panel;
    }

    private JPanel AddGameLeaveOption(GamePanel gamePanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton leaveGameButton = new JButton("Leave Game");
        leaveGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaveGameButton.addActionListener(event -> {
            for (Player player : gamePanel.players) {
                if (player != null) {
                    player.connectionHandler.ResetToGameModeSelectionScreen("Player left the game");
                }
            }
        });

        panel.add(leaveGameButton);
        return panel;
    }


    private JPanel AddMultiPlayerDestinationFields(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JLabel destinationLabel = new JLabel("Destination: ");
        destinationLabel.setFont(new Font("Arial", Font.BOLD, 18));
        destinationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel ipLabel = new JLabel("Ip: ");
        ipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField ipField = new JTextField();
        ipField.setMaximumSize(new Dimension(100, 25));

        JLabel portLabel = new JLabel("Port: ");
        portLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField portField = new JTextField();
        portField.setText(Integer.toString(GamePanel.port));
        portField.setMaximumSize(new Dimension(100, 25));


        JButton applyButton = new JButton("Apply");
        applyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                GamePanel.setMultiPlayerDestination(ipField.getText(), Integer.parseInt(portField.getText()));
            }
        });


        panel.add(destinationLabel);
        panel.add(ipLabel);
        panel.add(ipField);
        panel.add(portLabel);
        panel.add(portField);
        panel.add(applyButton);

        return panel;
    }

    public void GameModeSelectionScreen(){
        mainFrame.getContentPane().removeAll();
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());

        JButton startSinglePlayerButton = new JButton("singlePlayer");
        startSinglePlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startSinglePlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                GameMode gameMode = GameMode.SINGLE_PLAYER;
                StartGame(gameMode);
            }
        });
        panel.add(startSinglePlayerButton);

        JButton startMultiPlayerButton = new JButton("multiPlayer");
        startMultiPlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startMultiPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                GameMode gameMode = GameMode.MULTI_PLAYER;
                StartGame(gameMode);
            }
        });
        panel.add(startMultiPlayerButton);


        JPanel destinationPanel = AddMultiPlayerDestinationFields();
        panel.add(destinationPanel);
        panel.add(Box.createVerticalGlue());

        mainFrame.add(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    public void ShowPopupInfo(String message) {
        JOptionPane.showMessageDialog(mainFrame, message, "Info", JOptionPane.INFORMATION_MESSAGE);

    }

}
