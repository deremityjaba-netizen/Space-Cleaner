package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner;

import static ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.GameSettings.SCREEN_HEIGHT;
import static ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.GameSettings.SCREEN_WIDTH;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.screens.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MyGdxGame extends Game {
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public GameScreen gameScreen;


    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();

    }
}
