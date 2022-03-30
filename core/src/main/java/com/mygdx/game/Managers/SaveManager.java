package com.mygdx.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public final class SaveManager {
    static Preferences prefs;
    public static void SaveManager() {}


    public static void SaveGame(){
        System.out.println("Save ");
        prefs = Gdx.app.getPreferences("Save_game_1");

        prefs.putFloat("playerX", GameManager.getPlayer().getPosition().x);
        prefs.putFloat("playerY", GameManager.getPlayer().getPosition().y);

    }

    public static void LoadGame(){
        System.out.println("Loading");
        String X = prefs.getString("playerX", "0");
        String Y = prefs.getString("playerY", "0");



    }
}
