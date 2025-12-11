package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner;

import static ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game.GameSettings.POSITION_ITERATIONS;
import static ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game.GameSettings.SCREEN_HEIGHT;
import static ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game.GameSettings.SCREEN_WIDTH;
import static ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game.GameSettings.STEP_TIME;
import static ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game.GameSettings.VELOCITY_ITERATIONS;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;

import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game.GameResources;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.managers.AudioManager;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.screens.GameScreen;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.screens.MenuScreen;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.screens.SettingsScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MyGdxGame extends Game {
    public SpriteBatch batch;
    public MenuScreen menuScreen;
    public SettingsScreen settingsScreen;
    public AudioManager audioManager;
    public OrthographicCamera camera;
    public GameScreen gameScreen;
    public World world;
    public BitmapFont commonWhileFont;
    public BitmapFont commonBlackFont;
    public BitmapFont largeWhiteFont;
    float accumulator = 0;
    public Vector3 touch ;


    @Override
    public void create() {

        largeWhiteFont = FontBuilder.generate(48, Color.WHITE, GameResources.FONT_PATH);
        commonBlackFont = FontBuilder.generate(30, Color.BLACK, GameResources.FONT_PATH);
        commonWhileFont = FontBuilder.generate(24, Color.WHITE, GameResources.FONT_PATH);
        Box2D.init();
        world = new World(new Vector2(0,0), true);
        batch = new SpriteBatch();
        audioManager = new AudioManager();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        gameScreen = new GameScreen(this);
        menuScreen = new MenuScreen(this);
        settingsScreen = new SettingsScreen(this);
        setScreen(menuScreen);
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
    public void dispose() {
        batch.dispose();

    }
}
