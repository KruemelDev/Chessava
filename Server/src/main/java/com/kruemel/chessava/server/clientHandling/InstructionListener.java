package com.kruemel.chessava.server.clientHandling;

import com.kruemel.chessava.server.game.Game;
import com.kruemel.chessava.server.Server;
import com.kruemel.chessava.shared.game.Figure;
import com.kruemel.chessava.shared.game.FigureType;
import com.kruemel.chessava.shared.networking.Commands;
import com.kruemel.chessava.shared.networking.Packet;
import com.kruemel.chessava.shared.networking.Util;

import java.util.Objects;


public class InstructionListener implements Runnable{

    volatile Client client;
    Server server;

    public InstructionListener(Client client, Server server) {
        this.client = client;
        this.server = server;
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

            Commands command = Commands.fromString(packet.getCommand());
            switch (command){
                case CLOSE_CONNECTION:
                    client.server.RemoveClient(client);
                    break;
                case BATTLE_REQUEST:
                    sendBattleRequest(packet);
                    break;
                case BATTLE_ACCEPT:
                    if(!acceptBattle(packet)) break;
                    Game game = new Game(this.client, this.client.acceptClient);
                    game.InitGame();
                    break;
                case BATTLE_DECLINE:
                   if(this.client.acceptClient == null) break;
                   this.client.acceptClient.WriteMessage(Util.dataToJson(Commands.ERROR.getValue(), "The request was declined"));
                   this.client.acceptClient = null;
                   break;
                case MOVE_FIGURE:
                    moveFigure(packet);
                    break;
                case END_GAME:
                    endGame();
                    break;
                case FIGURE_SELECT:
                    handleSelectFigure(packet);
                    break;
            }

        }
    }

    private void endGame(){
        if (this.client.game != null) {
            Client opponent = this.client.game.GetOpponentByPlayer(this.client);
            opponent.WriteMessage(Util.dataToJson(Commands.END_GAME.getValue(), "Game has ended!"));
            this.client.game = null;

        }
    }

    private void handleSelectFigure(Packet packet) {
        if(!this.client.figureSelectActive) return;
        if (this.client.selectFigure == null) return;
        String figureString = packet.getData();
        FigureType figureType = FigureType.valueOf(figureString.toUpperCase());
        if (checkSelectFigure(figureType)) {
            Figure selectFigure = this.client.selectFigure;
            Figure figure = figureType.createInstance(selectFigure.color, selectFigure.x, selectFigure.y);
            this.client.game.board.ApplyMove(figure, figure.x, figure.y);
            this.client.game.board.SendFigures();

            this.client.game.NextPlayer();
            this.client.game.SendCurrentPlayer();

            this.client.figureSelectActive = false;
            this.client.selectFigure = null;
        } else {
            this.client.game.SendFigureSelectionOffer(this.client);
        }
    }
    private boolean checkSelectFigure(FigureType figureType) {
        return figureType != FigureType.PAWN && figureType != FigureType.KING;
    }

    private void moveFigure(Packet packet) {
        String poss = packet.getData();
        String[] positions = poss.split("\\|");

        try{
            if(!this.client.game.currentPlayer.equals(this.client)) return;
            this.client.game.MoveFigure(Integer.parseInt(positions[0]), Integer.parseInt(positions[1]), Integer.parseInt(positions[2]), Integer.parseInt(positions[3]));
        } catch (Exception e){
            if(this.client.game != null) {
                this.client.WriteMessage(Util.dataToJson(Commands.ERROR.getValue(), "Error try again later"));
            }
        }
    }

    private boolean acceptBattle(Packet packet){
        String name = packet.getData();

        if(!Objects.equals(name, this.client.acceptClient.name)){
            this.client.acceptClient = null;
            return false;
        }
        return true;
    }

    private void sendBattleRequest(Packet packet) {
        String name = packet.getData();
        if(!server.ClientAvailable(name)){
            client.WriteMessage(Util.dataToJson(Commands.ERROR.getValue(), "Player does not exist"));
            return;
        }
        if(Objects.equals(client.name, name)){
            client.WriteMessage(Util.dataToJson(Commands.ERROR.getValue(), "You can not send a battle request to yourself"));
            return;
        }

        Client client = server.GetClient(name);
        if(client == null) return;
        System.out.println("client.gamem" + client.game);
        if(client.game != null){
            this.client.WriteMessage(Util.dataToJson(Commands.ERROR.getValue(), "The player is already in game"));
            return;
        }

        client.acceptClient = this.client;

        String json = Util.dataToJson(Commands.BATTLE_REQUEST.getValue(), this.client.name);
        client.WriteMessage(json);

    }
}
