package com.kruemel.chessava.server.clientHandling;

import com.kruemel.chessava.dto.Packet;
import com.kruemel.chessava.dto.Util;

import java.io.IOException;

public class InstructionListener implements Runnable{

    volatile Client client;

    public InstructionListener(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        String json;
        while (client != null) {
            try {
                json = client.in.readUTF();
            } catch (Exception e) {
                client.server.RemoveClient(client);
                break;
            }
            Packet packet = Util.jsonToData(json);
            if(packet == null) continue;
            String command = packet.getCommand();

            switch (command){
                case "CloseConnection":
                    client.server.RemoveClient(client);
                    break;

            }

        }
    }
}
