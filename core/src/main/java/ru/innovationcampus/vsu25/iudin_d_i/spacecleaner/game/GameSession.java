package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.GameState;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.managers.MemoryManager;

public class GameSession {
    public GameState state;
    long nextTrashSpawnTime;
    long nextAidSpawnTime;
     long sessionStartTime;
    long pauseStartTime;
    private  int score;
     int destructedTrashNumber;

    public void startGame() {
        state = GameState.PLAYING;
        sessionStartTime = TimeUtils.millis();
        nextTrashSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_TRASH_APPEARANCE_COOL_DOWN
            * getTrashPeriodCoolDown());
        nextAidSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_AID_APPEARANCE_COOL_DOWN
            * getAidPeriodCoolDown());
        destructedTrashNumber = 0;
    }
    public  int getScore(){
        return  score;
    }
    public void pauseGame(){
        state = GameState.PAUSED;
        pauseStartTime = TimeUtils.millis();
    }
    public void resumeGame(){
        state = GameState.PLAYING;
        sessionStartTime += TimeUtils.millis() - pauseStartTime;
    }
    public boolean shouldSpawnTrash(){
        if(nextTrashSpawnTime <= TimeUtils.millis()){
            nextTrashSpawnTime = TimeUtils.millis() + (long) (GameSettings.STARTING_TRASH_APPEARANCE_COOL_DOWN
            * getTrashPeriodCoolDown());
            return true;
        }
        return false;
    }
    public boolean shouldSpawnAid(){
        if(nextAidSpawnTime <= TimeUtils.millis()){
            nextAidSpawnTime = TimeUtils.millis() + (long) (GameSettings.STARTING_AID_APPEARANCE_COOL_DOWN
                * getAidPeriodCoolDown());
            return true;
        }
        return false;
    }

    public void  destructionRegistration(){
        destructedTrashNumber += 1;
    }
    public  void updateScore(){
        score = (int) (TimeUtils.millis() - sessionStartTime) / 100 + destructedTrashNumber * 100;
    }

    private float getTrashPeriodCoolDown() {
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime) / 100);
    }
    private float getAidPeriodCoolDown() {
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime) / 10000);
    }

    public  void endGame(){
        updateScore();
        state = GameState.ENDED;
        ArrayList<Integer> recordsTable = MemoryManager.loadRecordsTable();
        if(recordsTable == null){
            recordsTable = new ArrayList<>();
        }
        int foundIdx = 0;
        for(; foundIdx < recordsTable.size(); foundIdx++){
            if(recordsTable.get(foundIdx) < getScore()) break;
        }
        recordsTable.add(foundIdx, getScore());
        MemoryManager.saveTableOfRecords(recordsTable);
    }

}
