package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.objects;

import com.badlogic.gdx.physics.box2d.World;

public class BulletObject extends GameObject{
    public BulletObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, world);
    }
}
