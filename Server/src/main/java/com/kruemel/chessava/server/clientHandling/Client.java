package com.kruemel.chessava.server.clientHandling;

import com.kruemel.chessava.server.Server;
import com.kruemel.chessava.server.game.Game;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public String name;

    public Socket socket;
    public DataInputStream in;
    public DataOutputStream out;

    public Client acceptClient;

    private InstructionListener instructionListener;

    Server server;

    public boolean figureSelect = false;
    public boolean inGame;
    public Game game;
    public Color gameColor;

    public Client(String name, Socket socket, Server server) {
        this.name = name;
        this.socket = socket;

        this.server = server;

        setupStreams();
    }

    private void setupStreams(){
        try {
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            server.RemoveClient(this);
        }
    }
    public void WriteMessage(String json) {
        try {
            if (out != null) {
                out.writeUTF(json);
                out.flush();
            }
        } catch (IOException e) {
            server.RemoveClient(this);
        }

    }

    public void StartInstructionListener(){
        instructionListener = new InstructionListener(this, server);
        new Thread(instructionListener).start();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Client client = (Client) obj;
        return this.name != null && this.name.equals(client.name);
    }

}
