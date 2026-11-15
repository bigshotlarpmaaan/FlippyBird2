package io.github.radiohead.screens;


import com.badlogic.gdx.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import io.github.radiohead.Main;
import io.github.radiohead.components.MovingBackground;
import io.github.radiohead.components.TextButton;

import com.badlogic.gdx.utils.ScreenUtils;

public class ScreenMenu implements Screen {
    Main main;

    MovingBackground background;
    TextButton buttonStart;
    TextButton buttonExit;



    public ScreenMenu(Main main) {
        this.main = main;

        buttonStart = new TextButton(100, 400, "Start");
        buttonExit = new TextButton(100, 200, "Exit");
        background = new MovingBackground("MovingBackground/menu_bg.png"); // картинка должна лежать в assets
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (Gdx.input.justTouched()) {

            Vector3 touch = main.camera.unproject(
                new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)
            );

            if (buttonStart.isHit((int) touch.x, (int) touch.y)) {
                main.setScreen(main.screenGame);
            }

            if (buttonExit.isHit((int) touch.x, (int) touch.y)) {
                Gdx.app.exit();
            }
        }

        ScreenUtils.clear(1, 0, 0, 1);

        background.move();
        background.draw(main.batch);
        buttonStart.draw(main.batch);
        buttonExit.draw(main.batch);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
        buttonStart.dispose();
        buttonExit.dispose();
    }
}

