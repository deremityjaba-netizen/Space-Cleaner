package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.GameSettings;

public class BulletObject extends GameObject{
    public BulletObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.BULLET_BIT, world);
        body.setLinearVelocity(new Vector2(0, GameSettings.BULLET_VELOCITY));
        body.setBullet(true);
    }
    public boolean hasToBeDestroyed(){

        return getY() + height / 2 > GameSettings.SCREEN_HEIGHT;
    }

}
