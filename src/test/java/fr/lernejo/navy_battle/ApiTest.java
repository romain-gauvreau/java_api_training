package fr.lernejo.navy_battle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class ApiTest {
    private final HttpClient client = HttpClient.newHttpClient();

    @Test
    public void
    handlePingTest() throws Exception {
        int port = 9976;
        String[] args = new String[]{String.valueOf(port)};
        Launcher.main(args);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:" + port + "/ping")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals("OK", response.body());
    }
}
