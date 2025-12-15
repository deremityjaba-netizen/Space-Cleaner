package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game.GameSettings;

public class ShipObject extends  GameObject{
    private int livesLeft;
    long lastShotTime;
    public ShipObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.SHIP_BIT, world);
        body.setLinearDamping(10);
        livesLeft = 3;
    }
    public boolean needToShot(){
        if(TimeUtils.millis() - lastShotTime >= GameSettings.SHOOTING_COOL_DOWN){
            lastShotTime = TimeUtils.millis();
            return true;
        }
        return false;
    }

    private void putInFrame(){

        if(getY() > (GameSettings.SCREEN_HEIGHT / 2f - height / 2f)){
            setY(GameSettings.SCREEN_HEIGHT / 2 - height / 2);
        }
        if(getY() <= (height / 2f)) {
            setY(height / 2);
        }
        if(getX() < (-width / 2f)){
            setX(GameSettings.SCREEN_WIDTH);
        }
        if(getX() > (GameSettings.SCREEN_WIDTH + width / 2f)){
            setX(0);
        }
    }
    @Override
    public  void draw(SpriteBatch batch){
        putInFrame();
        super.draw(batch);
    }
    public void move(Vector3 vector3){
        float fx = (vector3.x - getX()) * GameSettings.SHIP_FORCE_RATIO;
        float fy = (vector3.y - getY()) * GameSettings.SHIP_FORCE_RATIO;

        body.applyForceToCenter(
            new Vector2(
                (vector3.x - getX()) * GameSettings.SHIP_FORCE_RATIO,
                (vector3.y - getY()) * GameSettings.SHIP_FORCE_RATIO
            ),
            true
            );
    }
    @Override
    public void hit(){
        livesLeft -= 1;
    }
    public void heal(){livesLeft += 1;}
    public boolean isAlive(){
        return livesLeft > 0;
    }
    public int getLivesLeft(){
        return livesLeft;
    }
}
