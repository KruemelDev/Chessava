package com.kruemel.chessava.shared.networking;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


public class Util {
    private static final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static String dataToJson(String command, String data){
        Packet packet = new Packet(command, data);
        String json = "";
        try {
            json = ow.writeValueAsString(packet);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public static String dataToJson(String command){
        Packet packet = new Packet(command);
        String json = "";
        try {
            json = ow.writeValueAsString(packet);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public static Packet jsonToData(String json){

        try {
            return objectMapper.readValue(json, Packet.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
