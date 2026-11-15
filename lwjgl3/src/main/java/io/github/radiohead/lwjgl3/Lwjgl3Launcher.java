package io.github.radiohead.lwjgl3;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import io.github.radiohead.Main;


public class Lwjgl3Launcher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setForegroundFPS(60);
        config.setWindowedMode(Main.SCR_WIDTH, Main.SCR_HEIGHT); //настроить размер окна, в котором наша игра будет запускаться на компьютере.
        config.setTitle("FlappyBird");

        new Lwjgl3Application(new Main(), config);
    }
}

