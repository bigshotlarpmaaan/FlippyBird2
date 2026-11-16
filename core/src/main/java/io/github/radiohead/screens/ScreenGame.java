package io.github.radiohead.screens;


import static io.github.radiohead.Main.SCR_HEIGHT;
import static io.github.radiohead.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.radiohead.Main;
import io.github.radiohead.characters.Bird;
import io.github.radiohead.characters.Coin;
import io.github.radiohead.characters.Tube;
import io.github.radiohead.components.MovingBackground;
import io.github.radiohead.components.PointCounter;


public class ScreenGame implements Screen {

    final int pointCounterMarginTop = 60;
    final int pointCounterMarginRight = 400;


    int gamePoints;
    public static boolean isGameOver;
    Main main;
    Bird bird;
    PointCounter pointCounter;
    MovingBackground background;
    int tubeCount = 3;
    private Tube[] tubes;

    private Array<Coin> coins; // Список монеток, которые сейчас летят по экрану. Array, потому что их число меняется в любой момент.
    private Texture coinTexture;
    private TextureRegion coinRegion;
    private float coinSpawnTimer = 0f;
    private static final float COIN_SPAWN_RATE = 9f;



    private boolean shieldActive = false;  // Включён ли щит
    private float shieldTimer = 0f; // Сколько секунд щита осталось.
    private static final float SHIELD_TIME = 6f;     // Полная длительность щита —  6 секунд.

    private static final float COIN_SPEED = 600f;

    private int lives;
    private static final int START_LIVES = 3;
    private Texture heartTexture;
    private float invulnerableTimer = 0f;
    private static final float INVULN_TIME = 1.5f;

    public ScreenGame(Main main) {
        this.main = main;

        background = new MovingBackground("MovingBackground/game_bg.png");
        bird = new Bird(20, SCR_HEIGHT / 2, 10, 180, 144);

        pointCounter = new PointCounter(SCR_WIDTH - pointCounterMarginRight, SCR_HEIGHT - pointCounterMarginTop);

        initTubes();   // Заполняем массив колонн

        coins = new Array<>(); //пустой список монеток
        coinTexture = new Texture(Gdx.files.internal("coin.png"));
        coinRegion = new TextureRegion(coinTexture); // Оборачиваем текстуру в регион — он нужен для draw с вращением
        heartTexture = new Texture(Gdx.files.internal("heart.png"));
        lives = START_LIVES;
    }


    @Override
    public void show() {
        gamePoints = 0;
        isGameOver = false;
        bird.setY(SCR_HEIGHT / 2);
        bird.jump = false;
        bird.jumpHeight = 0;
        initTubes();

        coins.clear();
        shieldActive = false;
        shieldTimer = 0;
        coinSpawnTimer = 0;

        lives = START_LIVES;
        invulnerableTimer = 0;
    }

    @Override
    public void render(float delta) {//Вызывается при отрисовке каждого нового кадра. Именно здесь мы будем обновлять состояние игровых объектов и отрисовывать их на экране.

        //main.setScreen(main.screenRestart);
        if (isGameOver) {
            main.screenRestart.gamePoints = gamePoints;
            main.setScreen(main.screenRestart);
            // игра остановлена: ждём тапа для рестарта
            if (Gdx.input.justTouched()) {
                isGameOver = false;
                bird.y = SCR_HEIGHT / 2;
                bird.jumpHeight = 0;
                bird.jump = false;
                initTubes();
            }

            return;

        }


        if (Gdx.input.justTouched()) {
            bird.onClick();
        }

        if (shieldActive) {
            shieldTimer -= delta;
            if (shieldTimer <= 0) {
                shieldActive = false;
            }

        }

        if (invulnerableTimer > 0) {       // Тикаем неуязвимость вниз, если она включена
            invulnerableTimer -= delta;
        }

        coinSpawnTimer += delta;
        if (coinSpawnTimer >= COIN_SPAWN_RATE) {
            coinSpawnTimer = 0;
            float y = MathUtils.random(120, SCR_HEIGHT - 120);
            coins.add(new Coin(SCR_WIDTH + 50, y));
        }

        for (int i = coins.size - 1; i >= 0; i--) {
            Coin coin = coins.get(i);
            coin.update(delta, COIN_SPEED);

            if (coin.getX() < -50) {
                coins.removeIndex(i);
            } else if (coin.getBounds().overlaps(getBirdBounds())) {
                coins.removeIndex(i);
                activateShield();
                break;
            }
        }

        bird.fly();

        if (!bird.isInField()) {
            System.out.println("not in field");
            isGameOver = true;
        }

        for (Tube tube : tubes) {
            tube.move();
            if (tube.isHit(bird)) {
                if (!shieldActive && invulnerableTimer <= 0) {   // Умираем только если нет щита И неуязвимости
                    System.out.println("hit");
                    hitByTube();
                }
            } else if (tube.needAddPoint(bird)) {
                gamePoints += 1;
                tube.setPointReceived();
                System.out.println(gamePoints);
            }
        }


        background.move();

        background.draw(main.batch);
        for (Tube tube : tubes) tube.draw(main.batch);

        main.batch.begin();
        main.batch.setColor(Color.GOLD);
        for (Coin coin : coins) {
            main.batch.draw(coinRegion,
                coin.getX() - 15, coin.getY() - 15,
                15, 15,
                30, 30,
                1f, 1f,
                coin.getRotation());
        }
        main.batch.setColor(Color.WHITE);
        main.batch.end();

        // Птица мигает во время неуязвимости: (int)(timer*10)%2 даёт 0 или 1 попеременно
        boolean birdBlinking = invulnerableTimer > 0 && (int) (invulnerableTimer * 10) % 2 == 0;
        if (!birdBlinking) {
            bird.draw(main.batch);
        }

        if (shieldActive) {
            boolean blink = shieldTimer < 2f && (int) (shieldTimer * 10) % 2 == 0;
            if (!blink) {
                main.batch.begin();
                main.batch.setColor(0.3f, 0.8f, 1f, 0.4f);
                main.batch.draw(coinRegion,
                    bird.x - 10, bird.y - 10,
                    bird.width + 20, bird.height + 20);
                main.batch.setColor(Color.WHITE);
                main.batch.end();
            }
        }

        drawHearts();

        pointCounter.draw(main.batch, gamePoints);


        //Обрати внимание на параметр delta. Он содержит время в секундах, прошедшее с момента предыдущего вызова метода render(). Позже мы обязательно воспользуемся этим значением!
    }

    private void hitByTube() {        // Обработка удара: минус жизнь, при нуле — проигрыш
        lives--;
        System.out.println("lives left: " + lives);

        if (lives <= 0) {
            isGameOver = true;
        } else {
            invulnerableTimer = INVULN_TIME;   // Даём 1,5 секунды неуязвимости
        }
    }

    private void drawHearts() {       // Три сердечка в левом верхнем углу
        main.batch.begin();
        for (int i = 0; i < START_LIVES; i++) {
            if (i < lives) {
                main.batch.setColor(Color.WHITE);              // целое сердце — как есть
            } else {
                main.batch.setColor(0.25f, 0.25f, 0.25f, 0.6f); // потерянное — тёмное и полупрозрачное
            }
            main.batch.draw(heartTexture,
                10 + i * 50,                 // отступ слева 10, шаг между сердцами 50 пикселей
                SCR_HEIGHT - 60,             // у верхнего края
                40, 40);
        }
        main.batch.setColor(Color.WHITE);
        main.batch.end();
    }

    private void activateShield() {
        shieldActive = true;
        shieldTimer = SHIELD_TIME;
    }

    private Rectangle getBirdBounds() {
        return new Rectangle(bird.x, bird.y, bird.width, bird.height);
    }

    @Override
    public void resize(int width, int height) {
        main.camera.setToOrtho(false, Main.SCR_WIDTH, Main.SCR_HEIGHT);
        main.batch.setProjectionMatrix(main.camera.combined);
        //Вызывается сразу после show(), когда экран становится текущим, а затем — при изменении размеров области отображения.
        //Параметры width и height содержат её новые ширину и высоту в пикселях соответственно.
    }

    @Override
    public void pause() {
        //Вызывается, когда приложение приостанавливается — например, при сворачивании окна.
    }

    @Override
    public void resume() {
        //Можно сказать, что это противоположность методу pause(). Он вызывается, когда приложение возобновляет работу после приостановки.
        //
        //Например, когда пользователь возвращается в ранее свёрнутую игру.
    }

    @Override
    public void hide() {
        //А это уже противоположность методу show(). Он вызывается, когда наш экран перестаёт быть текущим.
        //
        //Например, если после завершения игры мы переходим с игрового экрана на экран рестарта.
    }

    @Override
    public void dispose() {
        bird.dispose();
        background.dispose();
        pointCounter.dispose();
        for (Tube tube : tubes) {
            tube.dispose();
        }
        coinTexture.dispose();
        heartTexture.dispose();     // Освобождаем текстуру сердечка
        coins.clear();
    }

    private void initTubes() {
        tubes = new Tube[tubeCount];

        for (int i = 0; i < tubeCount; i++) {
            tubes[i] = new Tube(tubeCount, i);
        }
    }
}
