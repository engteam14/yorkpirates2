package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Managers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mygdx.utils.Constants.INIT_CONSTANTS;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class SaveManagerTest {

    private Preferences prefs;

    @Before
    public void init(){
        ResourceManager.addTexture("ship.png");
        ResourceManager.addTileMap("Map.tmx");
        ResourceManager.addTextureAtlas("Boats.txt");
        ResourceManager.addTextureAtlas("UISkin/skin.atlas");
        ResourceManager.addTextureAtlas("Buildings.txt");
        ResourceManager.addTexture("menuBG.jpg");
        ResourceManager.addTexture("Chest.png");
        ResourceManager.loadAssets();

        INIT_CONSTANTS();
        PhysicsManager.Initialize(false);
        prefs = Gdx.app.getPreferences("pirate/GameSave_game_1");

    }
    @After
    public void dispose(){
        ResourceManager.dispose();
        GameManager.dispose();
    }


    @Test
    public void saveGame() {
        GameManager.Initialize(GameDifficulty.Regular);
        GameManager.CreatePlayer();
        GameManager.CreateCollege(1);
        SaveManager.SaveGame();

        assertFalse(prefs.get().isEmpty());
    }

    @Test
    public void loadGame() {
        GameManager.Initialize(GameDifficulty.Hard);
        GameManager.CreatePlayer();
        int playerHealth1 = GameManager.getPlayer().getComponent(Pirate.class).getHealth();
        GameManager.getPlayer().getComponent(Pirate.class).takeDamage(10);
        int playerHealth2 = GameManager.getPlayer().getComponent(Pirate.class).getHealth();
        GameManager.CreateCollege(1);
        GameManager.getCollege(1).killThisCollege();
        SaveManager.SaveGame();

        GameManager.Initialize(GameDifficulty.Hard);

        GameManager.CreatePlayer();
        int playerHealth3 = GameManager.getPlayer().getHealth();
;

        SaveManager.LoadGame();
        QuestManager.Initialize();
        GameManager.CreateCollege(1);
        assertTrue(GameManager.getCollege(1).isAlive());

        SaveManager.SpawnGame();
        assertFalse(GameManager.getCollege(1).isAlive());
        int playerHealth4 = GameManager.getPlayer().getHealth();


        assertEquals(playerHealth1, playerHealth3);
        assertEquals(playerHealth2, playerHealth4);



    }

    @Test
    public void spawnGame() {
    }
}