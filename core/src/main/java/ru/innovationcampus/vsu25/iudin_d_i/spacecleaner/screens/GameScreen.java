package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.ButtonView;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.ContactManager;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.ImageView;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.LiveView;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.MovingBackgroundView;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.TextView;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game.GameResources;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game.GameSession;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game.GameSettings;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.MyGdxGame;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.objects.BulletObject;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.objects.ShipObject;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.objects.TrashObject;

public class GameScreen extends ScreenAdapter {
    GameSession gameSession;
    MyGdxGame myGdxGame;
    ShipObject shipObject;
    TextView scoreTextView;
    ButtonView pauseButton;
    ArrayList<TrashObject> trashArray;
    ArrayList<BulletObject> bulletArray;
    ContactManager contactManager;
    MovingBackgroundView backgroundView;
    ImageView topBlackoutView;
    LiveView liveView;
    public GameScreen(MyGdxGame myGdxGame){
        this.myGdxGame = myGdxGame;

        scoreTextView = new TextView(myGdxGame.commonWhileFont, 50, 1215);

        liveView = new LiveView(305, 1215);

        pauseButton = new ButtonView(605, 1200, 46, 54, GameResources.PAUSE_ICON_IMG_PATH);

        topBlackoutView = new ImageView(0, 1180, GameResources.BLACKOUT_TOP_IMG_PATH);

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
      gameSession.startGame();
    }

    public  void  render(float delta){

        if(gameSession.shouldSpawnTrash()){
            TrashObject trashObject = new TrashObject(
                GameSettings.TRASH_WIDTH, GameSettings.TRASH_HEIGHT,
                GameResources.TRASH_IMG_PATH,
                myGdxGame.world
            );
            trashArray.add(trashObject);
        }
        if(shipObject.needToShot()){
            BulletObject laserBullet = new BulletObject(
                shipObject.getX(), shipObject.getY() + shipObject.height / 2,
                GameSettings.BULLET_WIDTH, GameSettings.BULLET_HEIGHT,
                GameResources.BULLET_IMG_PATH,
                myGdxGame.world);
            bulletArray.add(laserBullet);
        }
        if(!shipObject.isAlive()){
            System.out.println("Game over!");
        }
        liveView.setLeftLives(shipObject.getLivesLeft());
        scoreTextView.setText("Score : " + 100);

        backgroundView.move();

        updateBullets();
        updateTrash();
        myGdxGame.stepWorld();
        handleInput();
        draw();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            shipObject.move(myGdxGame.touch);
        }
    }
    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        backgroundView.draw(myGdxGame.batch);
        topBlackoutView.draw(myGdxGame.batch);

        liveView.draw(myGdxGame.batch);
        scoreTextView.draw(myGdxGame.batch);
        pauseButton.draw(myGdxGame.batch);
        for(TrashObject trash : trashArray) trash.draw(myGdxGame.batch);
        for(BulletObject bullet : bulletArray) bullet.draw(myGdxGame.batch);
        shipObject.draw(myGdxGame.batch);



        myGdxGame.batch.end();
    }
    private void updateTrash() {
        for (int i = 0; i < trashArray.size(); i++){
            if(!trashArray.get(i).isInFrame() || !trashArray.get(i).isAlive()){
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
}
