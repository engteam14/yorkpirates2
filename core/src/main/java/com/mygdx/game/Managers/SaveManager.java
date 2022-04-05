package com.mygdx.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Entitys.College;
import com.mygdx.game.Entitys.Ship;

import java.util.ArrayList;

/**
 * Added for assessment 2
 * Manges the saving of the game
 */
public final class SaveManager {
    static Preferences prefs;

    /**
     * Added for assessment 2
     * Saves the state of the game. Added for assesment 2
     */
    public static void SaveGame(){
        prefs = Gdx.app.getPreferences("pirate/GameSave_game_1");
        prefs.putString("Difficulty", GameManager.getSettings().name);

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
        for (College college : colleges) {
            prefs.putBoolean(college.f.id + "Alive", college.isAlive());
        }
        prefs.flush();

    }

    /**
     * Added for assessment 2
     * loads the preferences file and sets the difficulty of the saved game
     */
    public static void LoadGame(){
        prefs = Gdx.app.getPreferences("pirate/GameSave_game_1");
        String diff = prefs.getString("Difficulty", "Regular");
        GameManager.changeDifficulty(diff);

    }
    /**
     * Added for assessment 2
     * loads the state of the ships and colleges and sets the current quest
     */
    public static void SpawnGame(){
        ArrayList<Ship> shipsAll = GameManager.getShips();
        for (int i = 0; i< shipsAll.size(); i++){
            Ship ship = shipsAll.get(i);
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
        for (College college : Colleges) {
            boolean isAlive = prefs.getBoolean(college.f.id+"Alive", true);
            if (!isAlive) {
                college.killThisCollege(null);
            }
        }
        QuestManager.setCurrentQuest(prefs.getString("currentQuest"));


    }
}

