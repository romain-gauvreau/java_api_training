package fr.lernejo.navy_battle;

import org.junit.jupiter.api.Test;

class LauncherTest {

    @Test
    public void LaunchClient() {
        Launcher.main(new String[]{"8765", "http://localhost:9976"});
    }
}
