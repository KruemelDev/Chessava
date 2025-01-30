package com.kruemel.chessava.server.clientHandling;

import com.kruemel.chessava.shared.Commands;
import com.kruemel.chessava.shared.Packet;
import com.kruemel.chessava.shared.Util;



public class InstructionListener implements Runnable{

    volatile Client client;

    public InstructionListener(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        String json;
        while (client != null) {
            System.out.println("start");
            try {
                json = client.in.readUTF();
            } catch (Exception e) {
                client.server.RemoveClient(client);
                break;
            }
            Packet packet = Util.jsonToData(json);
            if(packet == null) continue;


            Commands command = Commands.fromString(packet.getCommand());
            System.out.println("KOMMANDO" + command);
            switch (command){
                case CLOSE_CONNECTION:
                    System.out.println("remove client");
                    client.server.RemoveClient(client);
                    break;

            }

        }
    }
}
