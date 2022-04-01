package com.mygdx.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Components.Component;
import com.mygdx.game.Components.ComponentType;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Entitys.College;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Entitys.Ship;
import com.mygdx.game.PirateGame;

import java.util.ArrayList;

/**
 * Added for assessment 2
 * Manges the saving of the game
 */
public final class SaveManager {
    static Preferences prefs;
    public static void SaveManager() {}

    /**
     * Added for assessment 2
     * Saves the state of the game. Added for assesment 2
     */
    public static void SaveGame(){
        System.out.println("Save ");
        prefs = Gdx.app.getPreferences("pirate/GameSave_game_1");
        prefs.putString("Difficulty", GameManager.getSettings().name);
//
//        prefs.putFloat("playerX", GameManager.getPlayer().getPosition().x);
//        prefs.putFloat("playerY", GameManager.getPlayer().getPosition().y);
//        prefs.putFloat("loot", GameManager.getPlayer().getPlunder());
//        prefs.putFloat("health", GameManager.getPlayer().getHealth());
//        prefs.putFloat("ammo", GameManager.getPlayer().getAmmo());
        ArrayList<Ship> ships = GameManager.getShips();
        // save the stats of each ship where the first one is the player
        for (int i = 0; i < ships.size(); i++) {
            Ship ship = ships.get(i);
            prefs.putFloat("ship"+i+"X", ship.getPosition().x);
            prefs.putFloat("ship"+i+"Y", ship.getPosition().y);
            prefs.putFloat("ship" + i + "H", ship.getHealth());
            prefs.putInteger("ship" + i + "A", ship.getComponent(Pirate.class).getAmmo());
            prefs.putInteger("ship" + "F", ship.getComponent(Pirate.class).getFaction().id);
        }
        ArrayList<College> colleges = GameManager.getColleges();
        //saves wether each college is alive of not
        for (int i = 0; i<colleges.size(); i++){
            College college = colleges.get(i);
            prefs.putBoolean(college.getName() + "Alive", college.isAlive());
        }
        prefs.flush();

    }

    /**
     * Added for assessment 2
     * loads the preferences file and sets the difficulty of the saved game
     */
    public static void LoadGame(){
        System.out.println("Loading");
        prefs = Gdx.app.getPreferences("pirate/GameSave_game_1");
        System.out.println(prefs.get().isEmpty());
        String diff = prefs.getString("Difficulty", "Regular");
        GameManager.changeDifficulty(diff);

    }
    /**
     * Added for assessment 2
     * loads the state of the ships and colleges and sets the current quest
     */
    public static void SpawnGame(){
        ArrayList<Ship> shipsAll = GameManager.getShips();
        int i=0;
        for (i = 0; i< shipsAll.size(); i++){
            Ship ship = shipsAll.get(i);
            System.out.println(ship.getName());
            float shipX = prefs.getFloat("ship" + i + "X", ship.getPosition().x);
            float shipY = prefs.getFloat("ship" + i + "Y", ship.getPosition().y);
            float shipH = prefs.getFloat("ship" + i + "H", ship.getHealth());
            int shipP = (int) prefs.getFloat("ship" + i + "P", ship.getPlunder());
            int shipA = prefs.getInteger("ship" + i + "A", ship.getComponent(Pirate.class).getAmmo());
            int shipF = prefs.getInteger("ship" + i + "F", ship.getComponent(Pirate.class).getFaction().id);

            ship.getComponent(Pirate.class).addPlunder(ship.getPlunder() - shipP);
            ship.getComponent(Pirate.class).setAmmo(shipA);
            ship.getComponent(Transform.class).setPosition(shipX, shipY);
            ship.getComponent(Pirate.class).takeDamage(ship.getHealth() - shipH);
            ship.getComponent(Pirate.class).setFactionId(shipF);

        }
        ArrayList<College> Colleges = GameManager.getColleges();
        for (i = 0; i<Colleges.size(); i++){
            College college = Colleges.get(i);
            boolean isAlive = prefs.getBoolean(college.getName(), true);
            if (!isAlive){
                college.killThisCollege();
            }
        }
        QuestManager.setCurrentQuest(prefs.getString("currentQuest"));


    }
}

