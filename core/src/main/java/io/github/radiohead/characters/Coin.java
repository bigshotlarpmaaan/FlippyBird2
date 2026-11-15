package io.github.radiohead.characters;

import com.badlogic.gdx.math.Rectangle;
public class Coin {
    private static final float RADIUS = 15f;

    private float x, y;              // координаты ЦЕНТРА монетки
    private Rectangle bounds;        // хитбокс для проверки столкновений
    private float rotation = 0f;     // текущий угол поворота (для анимации)

    public Coin(float x, float y) {
        this.x = x;
        this.y = y;
        bounds = new Rectangle(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
    }

    public void update(float delta, float speed) {
        x -= speed * delta;          // едет влево вместе с колоннами
        bounds.setPosition(x - RADIUS, y - RADIUS);
        rotation += 120f * delta;    // крутится со скоростью 120 градусов в секунду
    }
    public Rectangle getBounds() { return bounds; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getRotation() { return rotation; }
}

