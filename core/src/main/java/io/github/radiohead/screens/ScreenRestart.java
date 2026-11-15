
package io.github.radiohead.screens;



import static io.github.radiohead.Main.SCR_HEIGHT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;

import io.github.radiohead.Main;
import io.github.radiohead.components.MovingBackground;
import io.github.radiohead.components.PointCounter;
import io.github.radiohead.components.TextButton;   // <- было: com.badlogic.gdx.scenes.scene2d.ui.TextButton

import com.badlogic.gdx.utils.ScreenUtils;

public class ScreenRestart implements Screen {
    Main main;

    MovingBackground background;
    TextButton buttonRestart;
    PointCounter pointCounter;
    TextButton buttonMenu;

    public int gamePoints;


    public ScreenRestart(Main main) {
        this.main = main;



        pointCounter = new PointCounter(750, 530);
        buttonRestart = new TextButton(100, 440, "Restart");
        buttonMenu = new TextButton(100, 180, "Menu");
        background = new MovingBackground("MovingBackground/restart_bg.png");
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

            if (buttonRestart.isHit((int) touch.x, (int) touch.y)) {
                main.setScreen(main.screenGame);

            }
            if (buttonMenu.isHit((int) touch.x, (int) touch.y)) {
                main.setScreen(main.screenMenu);
            }


        }



        ScreenUtils.clear(1, 0, 0, 1);
        main.camera.update();
        main.batch.setProjectionMatrix(main.camera.combined);


        background.draw(main.batch);
        buttonRestart.draw(main.batch);
        buttonMenu.draw(main.batch);
        pointCounter.draw(main.batch, gamePoints);


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
        buttonRestart.dispose();
        buttonMenu.dispose();
    }
}
