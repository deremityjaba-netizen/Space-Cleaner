package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.MyGdxGame;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game.GameResources;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.view.ButtonView;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.view.ImageView;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.view.MovingBackgroundView;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.view.TextView;

public class SettingsScreen extends ScreenAdapter {
    TextView titleTextView;
    TextView musicSettingView;
    TextView soundSettingView;
    TextView clearSettingView;
    MovingBackgroundView movingBackgroundView;
    ImageView middleBlackout;
    ButtonView  returnToMenuButton;
    MyGdxGame myGdxGame;

    private String translateStateToText(boolean state){
        return state ? "ON" : "OFF";
    }
    public SettingsScreen(MyGdxGame myGdxGame){
        this.myGdxGame = myGdxGame;

        returnToMenuButton = new ButtonView(280, 447, 160, 70, myGdxGame.commonBlackFont, GameResources.PAUSE_MENU_BUTTONS_IMG_PATH, "return");

        movingBackgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);

        musicSettingView = new TextView(myGdxGame.commonWhileFont, 173, 717, "music: " + "ON");

        soundSettingView = new TextView(myGdxGame.commonWhileFont, 173, 658, "sound: " + "ON");

        clearSettingView = new TextView(myGdxGame.commonWhileFont, 173, 599, "clear records");

        titleTextView = new TextView(myGdxGame.largeWhiteFont, 256, 956, "Settings");

        middleBlackout = new ImageView(85, 365, GameResources.BLACKOUT_MIDDLE_IMG_PATH);


    }


    @Override
    public void render(float delta) {

        handleInput();

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        movingBackgroundView.draw(myGdxGame.batch);
        titleTextView.draw(myGdxGame.batch);
        middleBlackout.draw(myGdxGame.batch);
        returnToMenuButton.draw(myGdxGame.batch);
        musicSettingView.draw(myGdxGame.batch);
        soundSettingView.draw(myGdxGame.batch);
        clearSettingView.draw(myGdxGame.batch);

        myGdxGame.batch.end();
    }
    @Override
    public void dispose(){
        movingBackgroundView.dispose();
        titleTextView.dispose();
        middleBlackout.dispose();
        returnToMenuButton.dispose();
        musicSettingView.dispose();
        soundSettingView.dispose();
        clearSettingView.dispose();
    }

    void handleInput(){
        if(Gdx.input.justTouched()){
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if(returnToMenuButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)){
                myGdxGame.setScreen(myGdxGame.menuScreen);
            }
            if(clearSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)){
                clearSettingView.setText("clear record (cleared)");
            }
            if(musicSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)){
                myGdxGame.audioManager.isMusicOn = !myGdxGame.audioManager.isMusicOn;
                musicSettingView.setText("music:" + translateStateToText(myGdxGame.audioManager.isMusicOn));
                myGdxGame.audioManager.updateMusicFlag();
            }
            if(soundSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)){
                myGdxGame.audioManager.isSoundOn = !myGdxGame.audioManager.isSoundOn;
                soundSettingView.setText("sound: " + translateStateToText(myGdxGame.audioManager.isSoundOn));
            }

        }
    }
}
