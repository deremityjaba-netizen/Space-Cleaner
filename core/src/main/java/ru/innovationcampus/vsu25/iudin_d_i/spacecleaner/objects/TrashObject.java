package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.objects;

import com.badlogic.gdx.physics.box2d.World;

public class TrashObject extends  GameObject{

    TrashObject(String texturePath, int x, int y, int width, int height, World world) {
        super(texturePath, x, y, width, height, world);
    }
}
