package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;

import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.MyGdxGame;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;

    public GameScreen(MyGdxGame myGdxGame){
        this.myGdxGame = myGdxGame;
    }

}
