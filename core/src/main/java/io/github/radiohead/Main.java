package io.github.radiohead;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import io.github.radiohead.screens.ScreenGame;
import io.github.radiohead.screens.ScreenMenu;
import io.github.radiohead.screens.ScreenRestart;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class  Main extends Game {

    public static final int SCR_WIDTH = 1280;
    public static final int SCR_HEIGHT = 720;


    public SpriteBatch batch;
    public OrthographicCamera camera; // поле для объекта нашей ортографической камеры

    public ScreenGame screenGame;
    public ScreenRestart screenRestart;

    public ScreenMenu screenMenu;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(); //создадим объект камеры и зададим размеры целевого экрана, которые мы ранее сохранили в константах SCR_WIDTH и SCR_HEIGHT.
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
        batch.setProjectionMatrix(camera.combined);

        screenGame = new ScreenGame(this);
        screenRestart = new ScreenRestart(this);
        screenMenu = new ScreenMenu(this);
        setScreen(screenMenu);


    }




    @Override
    public void dispose() {
        screenGame.dispose();
        batch.dispose();
        screenRestart.dispose();
        screenMenu.dispose();
    }
}
