package io.github.radiohead.screens;


import static io.github.radiohead.Main.SCR_HEIGHT;
import static io.github.radiohead.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.utils.ScreenUtils;

import io.github.radiohead.Main;
import io.github.radiohead.characters.Bird;
import io.github.radiohead.characters.Tube;
import io.github.radiohead.components.MovingBackground;
import io.github.radiohead.components.PointCounter;


public class ScreenGame implements Screen{

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



    public ScreenGame(Main main) {
        this.main = main;

        background = new MovingBackground("MovingBackground/game_bg.png");
        bird = new Bird(20, SCR_HEIGHT / 2, 10, 180, 144);

        pointCounter = new PointCounter(SCR_WIDTH - pointCounterMarginRight, SCR_HEIGHT - pointCounterMarginTop);

        initTubes();
    }


    @Override
    public void show() {
        gamePoints = 0;
        isGameOver = false;
        bird.setY(SCR_HEIGHT / 2);
        bird.jump = false;      // <- гасим незавершённый прыжок
        bird.jumpHeight = 0;    // <- сбрасываем цель прыжка
        initTubes();//Вызывается, когда наш экран становится текущим экраном игры. Например, когда игрок переходит из главного меню непосредственно к игровому процессу.
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
        bird.fly();

        if (!bird.isInField()) {
            System.out.println("not in field");
            isGameOver = true;
        }

        for (Tube tube : tubes) {
            tube.move();
            if (tube.isHit(bird)) {
                System.out.println("hit");
                isGameOver = true;
            } else if (tube.needAddPoint(bird)) {
                gamePoints += 1;
                tube.setPointReceived();
                System.out.println(gamePoints);
            }
        } // <- здесь закрылся первый цикл


        background.move();

        background.draw(main.batch);
        for (Tube tube : tubes) tube.draw(main.batch);
        bird.draw(main.batch);
        pointCounter.draw(main.batch, gamePoints);


        //Обрати внимание на параметр delta. Он содержит время в секунды, прошедшее с момента предыдущего вызова метода render(). Позже мы обязательно воспользуемся этим значением!
    } // <- здесь закрылся render()

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
        //Можно сказать, что это противоположность метода pause(). Он вызывается, когда приложение возобновляет работу после приостановки.
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
    }

    private void initTubes() {
        tubes = new Tube[tubeCount];

        for (int i = 0; i < tubeCount; i++) {
            tubes[i] = new Tube(tubeCount, i);
        }
    }
}
