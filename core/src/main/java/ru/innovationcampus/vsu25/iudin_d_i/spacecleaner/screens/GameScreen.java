package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.screens;

import static com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable.draw;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.GameResources;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.GameSession;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.GameSettings;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.MyGdxGame;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.objects.BulletObject;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.objects.ShipObject;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.objects.TrashObject;

public class GameScreen extends ScreenAdapter {
    GameSession gameSession;
    MyGdxGame myGdxGame;
    ShipObject shipObject;
    ArrayList<TrashObject> trashArray;
    ArrayList<BulletObject> bulletArray;
    public GameScreen(MyGdxGame myGdxGame){
        this.myGdxGame = myGdxGame;

        gameSession = new GameSession();

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
        for(TrashObject trash : trashArray) trash.draw(myGdxGame.batch);
        for(BulletObject bullet : bulletArray) bullet.draw(myGdxGame.batch);
        shipObject.draw(myGdxGame.batch);
        myGdxGame.batch.end();
    }
    private void updateTrash() {
        for (int i = 0; i < trashArray.size(); i++){
            if(!trashArray.get(i).isInFrame()){
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
