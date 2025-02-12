package com.kruemel.chessava.server;

import com.kruemel.chessava.server.game.Game;
import com.kruemel.chessava.shared.networking.Commands;
import com.kruemel.chessava.shared.networking.Packet;
import com.kruemel.chessava.shared.networking.Util;
import com.kruemel.chessava.server.clientHandling.Client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {

    ServerSocket server;
    int port;

    public ArrayList<Client> clients = new ArrayList<>();

    public Server(int port) {
        this.port = port;

    }

    @Override
    public void run() {
            while(true) {
                try {
                    Socket socket = server.accept();
                    DataInputStream in = new DataInputStream(socket.getInputStream());

                    String namePacket = in.readUTF();
                    Packet packet = Util.jsonToData(namePacket);
                    if(packet == null) continue;
                    String command = packet.getCommand();
                    String name = packet.getData();

                    ClearClosedClients();
                    if(command.equals(Commands.NAME.getValue()) && !ClientAvailable(name) && !name.contains("|") && !name.isEmpty()){
                        AddClient(name, socket);
                    }
                    else{
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        String json = Util.dataToJson(Commands.CLOSE_CONNECTION.getValue(), "Name is already in use. Or there are some unallowed chars in the name");
                        try {
                            out.writeUTF(json);
                        }catch(Exception e) {
                            continue;
                        }

                    }

                } catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
    }

    public void AddClient(String name, Socket socket) {
        Client client = new Client(name, socket, this);
        client.StartInstructionListener();
        this.clients.add(client);
        UpdateAvailablePlayer();
        System.out.println("Client " + name + " added to game");
        System.out.println("client amount" + this.clients.size());
    }
    public Client GetClient(String name) {
        for(Client client : this.clients) {
            if (client.name.equals(name)) return client;
        }
        return null;
    }

    public void UpdateAvailablePlayer(){
        StringBuilder players = BuildPlayerString();
        for(Client client : this.clients){
            client.WriteMessage(Util.dataToJson(Commands.PLAYER_AVAILABLE.getValue(), players.toString()));
        }
    }

    private StringBuilder BuildPlayerString(){
        StringBuilder players = new StringBuilder();
        for(Client client : this.clients){
            players.append(client.name).append("|");
        }
        return players;
    }

    private void ClearClosedClients() {
        for(Client client : this.clients) {
            if (client == null || client.socket.isClosed()) RemoveClient(client);
        }
    }

    public void RemoveClient(Client client) {
        if (client.inGame){
            for (Client player : client.game.players) {
                if (!player.name.equals(client.name)){
                    player.WriteMessage(Util.dataToJson(Commands.END_GAME.getValue(), "Opponent left the game"));
                    player.game = null;
                    player.inGame = false;
                }
            }
        }
        this.clients.remove(client);
        UpdateAvailablePlayer();
    }


    public boolean ClientAvailable(String name){
        for(Client client: clients){
            if(client.name.equals(name)) return true;
        }
        return false;

    }

    public void InitServer(){
        try {
            server = new ServerSocket();
            server.setReuseAddress(true);
            server.bind(new InetSocketAddress("0.0.0.0", this.port));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
