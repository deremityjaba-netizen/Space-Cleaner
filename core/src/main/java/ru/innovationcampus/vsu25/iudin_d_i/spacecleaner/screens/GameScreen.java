package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.managers.AudioManager;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.managers.MemoryManager;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.view.ButtonView;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.managers.ContactManager;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.GameState;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.view.ImageView;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.view.LiveView;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.view.MovingBackgroundView;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.view.RecordsListViews;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.view.TextView;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game.GameResources;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game.GameSession;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game.GameSettings;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.MyGdxGame;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.objects.BulletObject;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.objects.ShipObject;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.objects.TrashObject;

public class GameScreen extends ScreenAdapter {
    GameSession gameSession;
    RecordsListViews recordsListViews;
    TextView recordsTextView;
    ButtonView homeButton2;
    MyGdxGame myGdxGame;
    ShipObject shipObject;
    TextView scoreTextView;
    TextView pauseTextView;
    ButtonView pauseButton;
    ButtonView homeButton;
    ButtonView continueButton;
    ArrayList<TrashObject> trashArray;
    ArrayList<BulletObject> bulletArray;
    ContactManager contactManager;
    MovingBackgroundView backgroundView;
    ImageView topBlackoutView;
    ImageView fullBlackoutView;
    AudioManager audioManager;
    LiveView liveView;
    public GameScreen(MyGdxGame myGdxGame){
        this.myGdxGame = myGdxGame;

        audioManager = new AudioManager();

        recordsListViews = new RecordsListViews(myGdxGame.commonWhiteFont, 690);

        recordsTextView = new TextView(myGdxGame.largeWhiteFont, 206, 842, "Last records");

        homeButton2 = new ButtonView(280, 365, 160, 70, myGdxGame.commonBlackFont, GameResources.PAUSE_MENU_BUTTONS_IMG_PATH, "Home");

        scoreTextView = new TextView(myGdxGame.commonWhiteFont, 50, 1215);

        pauseTextView = new TextView(myGdxGame.largeWhiteFont, 282, 842);

        liveView = new LiveView(305, 1215);

        pauseButton = new ButtonView(605, 1200, 46, 54, GameResources.PAUSE_ICON_IMG_PATH);

        continueButton = new ButtonView(393, 695, 200, 70, myGdxGame.commonBlackFont, GameResources.PAUSE_MENU_BUTTONS_IMG_PATH, "continue");

        homeButton = new ButtonView(138, 695, 200, 70, myGdxGame.commonBlackFont, GameResources.PAUSE_MENU_BUTTONS_IMG_PATH, "home");

        topBlackoutView = new ImageView(0, 1180, GameResources.BLACKOUT_TOP_IMG_PATH);

        fullBlackoutView = new ImageView(0, 0, GameResources.BLACK_FULL_IMG_PATH);

        gameSession = new GameSession();

        contactManager = new ContactManager(myGdxGame.world);

        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);

        trashArray = new ArrayList<>();

        bulletArray = new ArrayList<>();

        shipObject = new ShipObject(
            GameSettings.SCREEN_WIDTH / 2, 150,
            GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
            GameResources.SHIP_IMG_PATH,
            myGdxGame.world
        );
    }
    public void show(){
      restartGame();
    }

    public  void  render(float delta){
        handleInput();




        if(GameSession.state == GameState.PLAYING) {



            if (gameSession.shouldSpawnTrash()) {
                TrashObject trashObject = new TrashObject(
                    GameSettings.TRASH_WIDTH, GameSettings.TRASH_HEIGHT,
                    GameResources.TRASH_IMG_PATH,
                    myGdxGame.world
                );
                trashArray.add(trashObject);
            }
            if (shipObject.needToShot()) {

                BulletObject laserBullet = new BulletObject(
                    shipObject.getX(), shipObject.getY() + shipObject.height / 2,
                    GameSettings.BULLET_WIDTH, GameSettings.BULLET_HEIGHT,
                    GameResources.BULLET_IMG_PATH,
                    myGdxGame.world);
                bulletArray.add(laserBullet);
                if(myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.shootSound.play(0.1f);
            }

            if (!shipObject.isAlive()) {
                GameSession.endGame();
                recordsListViews.setRecords(MemoryManager.loadRecordsTable());
            }
            liveView.setLeftLives(shipObject.getLivesLeft());
            gameSession.updateScore();
            scoreTextView.setText("Score : " + gameSession.getScore());
            pauseTextView.setText("PAUSE");

            backgroundView.move();

            updateBullets();
            updateTrash();
            myGdxGame.stepWorld();
            handleInput();

        }

        draw();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            switch (GameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.pauseGame();
                    }
                    shipObject.move(myGdxGame.touch);
                    break;

                case PAUSED:
                    if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.resumeGame();
                    }
                    if (homeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;
                case ENDED:
                    if(homeButton2.isHit(myGdxGame.touch.x, myGdxGame.touch.y)){
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;
            }

        }
    }
    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        backgroundView.draw(myGdxGame.batch);

        for(TrashObject trash : trashArray) trash.draw(myGdxGame.batch);
        for(BulletObject bullet : bulletArray) bullet.draw(myGdxGame.batch);
        shipObject.draw(myGdxGame.batch);

        topBlackoutView.draw(myGdxGame.batch);



        liveView.draw(myGdxGame.batch);
        scoreTextView.draw(myGdxGame.batch);
        pauseButton.draw(myGdxGame.batch);





        if(GameSession.state == GameState.PAUSED){
            fullBlackoutView.draw(myGdxGame.batch);
            pauseTextView.draw(myGdxGame.batch);
            homeButton.draw(myGdxGame.batch);
            continueButton.draw(myGdxGame.batch);
        } else if(gameSession.state == GameState.ENDED){
            fullBlackoutView.draw(myGdxGame.batch);
            recordsTextView.draw(myGdxGame.batch);
            recordsListViews.draw(myGdxGame.batch);
            homeButton2.draw(myGdxGame.batch);
        }






        myGdxGame.batch.end();
    }
    private void updateTrash() {
        for (int i = 0; i < trashArray.size(); i++){


            boolean hasToBeDestroyed = !trashArray.get(i).isAlive() || !trashArray.get(i).isInFrame();

            if(!trashArray.get(i).isAlive()){
                if(myGdxGame.audioManager.isSoundOn) {
                    myGdxGame.audioManager.explosionSound.play(0.2f);

                }
                gameSession.destructionRegistration();

            }

            if(hasToBeDestroyed){
                myGdxGame.world.destroyBody(trashArray.get(i).body);
                trashArray.remove(i--);
            }

        }
    }
    private void updateBullets(){
        for(int i = 0; i< bulletArray.size(); i++){
            if(bulletArray.get(i).hasToBeDestroyed()){
                myGdxGame.world.destroyBody(bulletArray.get(i).body);
                bulletArray.remove(i--);
            }
        }
    }
    private void  restartGame(){
        for(int i = 0; i < trashArray.size(); i++){
            myGdxGame.world.destroyBody(trashArray.get(i).body);
            trashArray.remove(i--);

        }
        if(shipObject != null){
            myGdxGame.world.destroyBody(shipObject.body);
        }
        shipObject = new ShipObject(
            GameSettings.SCREEN_WIDTH / 2, 150,
            GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
            GameResources.SHIP_IMG_PATH,
            myGdxGame.world
        );

        bulletArray.clear();
        gameSession.startGame();
    }
}
