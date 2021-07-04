package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import fr.lernejo.navy_battle.entities.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import java.util.UUID;
import java.util.concurrent.Executors;

public class Api extends Client {
    private final BaseEntity<MapEntity> serverMap = new BaseEntity<>();
    private final BaseEntity<MapEntity> clientMap = new BaseEntity<>();
    private final BaseEntity<ApiEntity> clientEntity = new BaseEntity<>();
    private final BaseEntity<ApiEntity> serverEntity = new BaseEntity<>();

    public void start(int port, String url) throws IOException {
        serverEntity.set(new ApiEntity(
            UUID.randomUUID().toString(),
            "http://localhost:" + port,
            "OK"
        ));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.createContext("/ping", this::handlePing);
        server.createContext("/api/game/start", s -> handleStartGame(new MyHttpHandler(s)));
        server.start();
        if (url != null)
            this.requestStart(url);

    }

    private void handlePing(HttpExchange exchange) throws IOException {
        String body = "OK";
        exchange.sendResponseHeaders(200, body.length());
        try (OutputStream os = exchange.getResponseBody()) { // (1)
            os.write(body.getBytes());
        }
    }

    public void handleStartGame(MyHttpHandler handler) throws IOException {
        try {
            clientEntity.set(ApiEntity.fromJSON(handler.getJSONObject()));
            serverMap.set(new MapEntity(true));
            clientMap.set(new MapEntity(false));
            System.out.println("Server will fight against the following client: " + clientEntity.get().getUrl());
            handler.makeJsonResponse(202, serverEntity.get().toJSON());
        } catch (Exception e) {
            e.printStackTrace();
            handler.makeResponse(400, e.getMessage());
        }
    }

    public void requestStart(String url) {
        try {
            serverMap.set(new MapEntity(true));
            System.out.println(this.serverEntity.get().toJSON().toString());
            clientMap.set(new MapEntity(false));
            var response = sendPOSTRequest(url + "/api/game/start", this.serverEntity.get().toJSON());
            this.clientEntity.set(ApiEntity.fromJSON(response).withURL(url));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to start the game");
        }
    }

}

