package io.github.radiohead.characters;

import static io.github.radiohead.Main.SCR_HEIGHT;
import static io.github.radiohead.Main.SCR_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.Random;

public class Tube {
    Texture textureUpperTube;
    Texture textureDownTube;
    Random random;

    private int x;
    private int gapY;
    int gapHeight;
    int distanceBetweenTubes;

    boolean isPointReceived;

    final int speed = 10;
    final int width = 200;
    final int height = 700;
    final int minGapHeight = 380;
    final int maxGapHeight = 550;
    final int padding = 50;

    public Tube(int tubeCount, int tubeIdx) {
        random = new Random();

        gapHeight = generateGapHeight();
        gapY = generateGapY();
        distanceBetweenTubes = (SCR_WIDTH + width) / (tubeCount - 1);
        x = distanceBetweenTubes * tubeIdx + SCR_WIDTH;

        textureUpperTube = new Texture("tubes/tube_flipped.png");
        textureDownTube = new Texture("tubes/tube.png");

        isPointReceived = false;
    }

    int generateGapHeight() {
        return minGapHeight + random.nextInt(maxGapHeight - minGapHeight);
    }

    int generateGapY() {
        int minGapY = padding + gapHeight / 2;
        int maxGapY = SCR_HEIGHT - padding - gapHeight / 2;

        if (maxGapY <= minGapY) {
            return SCR_HEIGHT / 2; // разброса нет — зазор всегда по центру
        }
        return minGapY + random.nextInt(maxGapY - minGapY);
    }

    public void draw(Batch batch) {
        batch.begin();
        batch.draw(textureUpperTube, x, gapY + gapHeight / 2, width, height);
        batch.draw(textureDownTube, x, gapY - gapHeight / 2 - height, width, height);

        batch.end();
    }

    public void move() {
        x -= speed;

        if (x < -width) {
            isPointReceived = false;
            x = SCR_WIDTH + distanceBetweenTubes;
            gapHeight = generateGapHeight();
            gapY = generateGapY();
        }
    }

    public boolean isHit(Bird bird) {
        // down tube collision
        if (bird.y <= gapY - gapHeight / 2 && bird.x + bird.width >= x && bird.x <= x + width)
            return true;
        // upper tube collision
        if (bird.y + bird.height >= gapY + gapHeight / 2 && bird.x + bird.width >= x && bird.x <= x + width)
            return true;

        return false;
    }

    public boolean needAddPoint(Bird bird) {
        return !isPointReceived && bird.x > x + width;
    }

    public void setPointReceived() {
        isPointReceived = true;
    }

    public void dispose() {
        textureUpperTube.dispose();
        textureDownTube.dispose();
    }
}
