package io.github.radiohead.characters;

import static io.github.radiohead.Main.SCR_HEIGHT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Bird {

    int x;
    public float y;
    int width, height;

    float speed;
    public float jumpHeight;
    final int maxHeightOfJump = 200;
    public boolean jump;

    int frameCounter;
    Texture[] framesArray;

    public Bird(int x, int y, int speed, int width, int height) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        frameCounter = 0;

        framesArray = new Texture[]{
            new Texture("birdTiles/bird0.png"),
            new Texture("birdTiles/bird1.png"),
            new Texture("birdTiles/bird2.png"),
            new Texture("birdTiles/bird1.png"),
        };
    }

    public void onClick() {
        jump = true;
        jumpHeight = maxHeightOfJump + y;
    }

    public void fly() {
        float dt = Gdx.graphics.getDeltaTime() * 60f;

        if (jump) {
            y += speed * dt;
            if (y >= jumpHeight) {
                jump = false;
            }
        } else {
            y -= speed * dt;
        }
    }

    public boolean isInField() {
        if (y + height < 0) return false;
        if (y > SCR_HEIGHT) return false;
        return true;
    }

    public void draw(Batch batch) {
        int frameMultiplier = 10;
        batch.begin();
        batch.draw(framesArray[frameCounter / frameMultiplier], x, y, width, height);
        if (frameCounter++ == framesArray.length * frameMultiplier - 1) frameCounter = 0;
        batch.end();
    }

    public void dispose() {
        for (Texture texture : framesArray) {
            texture.dispose();
        }
    }

    public void setY(float y) {
        this.y = y;
    }
}
