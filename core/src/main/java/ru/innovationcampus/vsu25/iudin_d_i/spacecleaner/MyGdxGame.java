package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner;

import static ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.GameSettings.POSITION_ITERATIONS;
import static ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.GameSettings.SCREEN_HEIGHT;
import static ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.GameSettings.SCREEN_WIDTH;
import static ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.GameSettings.STEP_TIME;
import static ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.GameSettings.VELOCITY_ITERATIONS;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.screens.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MyGdxGame extends Game {
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public GameScreen gameScreen;
    public World world;
    float accumulator = 0;


    @Override
    public void create() {
        Box2D.init();
        world = new World(new Vector2(0,0), true);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    public  void stepWorld(){
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += delta;

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

    @Override
    public void render() {
        world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();

    }
}
