package com.kruemel.chessava.server;

import com.kruemel.chessava.dto.Packet;
import com.kruemel.chessava.dto.Util;
import com.kruemel.chessava.server.clientHandling.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {

    public boolean running = true;
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
                    running = true;
                    Socket socket = server.accept();
                    DataInputStream in = new DataInputStream(socket.getInputStream());

                    String namePacket = in.readUTF();
                    Packet packet = Util.jsonToData(namePacket);

                    if(packet == null) continue;

                    String command = packet.getCommand();
                    String name = packet.getData();
                    System.out.println(name + " " + command);
                    if(command.equals("Name") && !clientAvailable(name) && !name.contains("|")){
                        AddClient(name, socket);
                    }
                    else{
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        String json = Util.dataToJson("CloseConnection");
                        try {
                            out.writeUTF(json);
                        }catch(Exception e) {
                            continue;
                        }

                    }

                } catch (Exception e){
                    running = false;
                    break;
                }
            }
    }

    public void StartGame(Client client1, Client client2) {
        Game game = new Game(client1, client2);
    }

    public void AddClient(String name, Socket socket) {
        Client client = new Client(name, socket, this);
        client.StartInstructionListener();
        this.clients.add(client);
    }

    public void RemoveClient(Client client) {
        this.clients.remove(client);
        client = null;
    }

    private boolean clientAvailable(String name){
        // TODO check other server with redis request etc.

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
