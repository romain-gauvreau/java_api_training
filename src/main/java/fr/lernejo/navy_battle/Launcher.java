package fr.lernejo.navy_battle;

import java.io.IOException;
import java.util.logging.Logger;

public class Launcher {
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("You need to respect the following syntax: Launcher [port] [url]");
                System.exit(-1);
            }
            int port = Integer.parseInt(args[0]);
            System.out.println("Application is starting, port " + port);
            new Api().start(port, args.length > 1 ? args[1] : null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
