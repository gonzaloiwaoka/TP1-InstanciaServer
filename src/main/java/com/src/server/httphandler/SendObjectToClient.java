package com.src.server.httphandler;

import ConsoleCleaner.ClearConsole;
import com.src.entity.DataClient;
import com.src.manager.ClientManager;
import com.src.server.gson.Mapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;


public class SendObjectToClient implements HttpHandler {

    private ClientManager clientManager;

    public SendObjectToClient(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        DataClient dataClient = Mapper.fromJson(exchange.getRequestBody(), DataClient.class);

        switch (dataClient.getActionType()) {
            case 0:
                clientManager.modifyNinja(dataClient);
                break;
            case 1:
                clientManager.attackTerrain(dataClient);
                break;
            case 2:
                clientManager.moveNinja(dataClient);
                break;
            case 3:
                clientManager.attackedTerrain(dataClient);
                break;
        }
        if (dataClient.getActionType() != 3) {
            clientManager.ShowGrid();
        }

        if (dataClient.getActionType() == 100) {
            ClearConsole.clearScreen();
            System.out.println("Espera mientras el otro juega.");
        }

        exchange.sendResponseHeaders(200,0);
        OutputStream os = exchange.getResponseBody();
        os.write(0);

        os.flush();
        os.close();
    }
}
