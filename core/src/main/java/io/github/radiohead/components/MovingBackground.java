package io.github.radiohead.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import io.github.radiohead.Main;

public class MovingBackground {
    Texture texture;

    int texture1X;
    int texture2X;
    int speed = 2;


    public MovingBackground(String pathToTexture) {                  // <- новый конструктор
        texture = new Texture(pathToTexture);
        texture1X = 0;
        texture2X = Main.SCR_WIDTH;
    }


    public void move() {
        texture1X -= speed;
        texture2X -= speed;

        if (texture1X <= -Main.SCR_WIDTH) {
            texture1X = Main.SCR_WIDTH;
        }
        if (texture2X <= -Main.SCR_WIDTH) {
            texture2X = Main.SCR_WIDTH;
        }
    }

    public void draw(Batch batch) {
        batch.begin();
        batch.draw(texture, texture1X, 0, Main.SCR_WIDTH, Main.SCR_HEIGHT);
        batch.draw(texture, texture2X, 0, Main.SCR_WIDTH, Main.SCR_HEIGHT);
        batch.end();
    }

    public void dispose() {
        texture.dispose();
    }
}
