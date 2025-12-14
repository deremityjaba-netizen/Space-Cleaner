package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.game;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.GameState;
import ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.managers.MemoryManager;

public class GameSession {
    public static GameState state;
    long nextTrashSpawnTime;
    static long sessionStartTime;
    long pauseStartTime;
    private static int score;
    static int destructedTrashNumber;

    public void startGame() {
        state = GameState.PLAYING;
        sessionStartTime = TimeUtils.millis();
        nextTrashSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_TRASH_APPEARANCE_COOL_DOWN
            * getTrashPeriodCoolDown());
    }
    public static int getScore(){
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

    public void  destructionRegistration(){
        destructedTrashNumber += 1;
    }
    public static void updateScore(){
        score = (int) (TimeUtils.millis() - sessionStartTime) / 100 + destructedTrashNumber * 100;
    }

    private float getTrashPeriodCoolDown() {
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime) / 500);
    }
    public static void endGame(){
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
