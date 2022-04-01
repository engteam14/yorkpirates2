package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Managers.*;
import com.mygdx.game.UI.EndScreen;
import com.mygdx.game.UI.GameScreen;
import com.mygdx.game.UI.MenuScreen;
import com.mygdx.game.UI.PauseScreen;

/**
 * Contains class instances of game UI screens.
 */
public class PirateGame extends Game {
    public MenuScreen menu;
    public GameScreen game;
    public EndScreen end;
    public Stage stage;
    public Skin skin;
    public PauseScreen pause;
    public int id_map;
    public GameDifficulty difficulty;

    /**
     * Create instances of game stage and UI screens.
     */
    @Override
    public void create() {
        RenderingManager.Initialize(); // added for assessment 2 due to rendering refactoring for testing
        // load resources
        int id_ship = ResourceManager.addTexture("ship.png");
        id_map = ResourceManager.addTileMap("Map.tmx");
        int atlas_id = ResourceManager.addTextureAtlas("Boats.txt");
        int extras_id = ResourceManager.addTextureAtlas("UISkin/skin.atlas");
        int buildings_id = ResourceManager.addTextureAtlas("Buildings.txt");

        ResourceManager.addTexture("menuBG.jpg");

        ResourceManager.addTexture("Chest.png");
        ResourceManager.loadAssets();
        // can't load any more resources after this point (just functionally I choose not to implement)
        stage = new Stage(new ScreenViewport());
        createSkin();

        menu = new MenuScreen(this);
        //game = new GameScreen(this, id_map); moved to game screen for assessment 2
        end = new EndScreen(this);
        pause = new PauseScreen(this);

        setScreen(menu);

    }

    /**
     * Clean up prevent memory leeks
     */
    @Override
    public void dispose() {
        menu.dispose();
        game.dispose();
        stage.dispose();
        skin.dispose();
    }

    /**
     * load ui skin from assets
     */
    private void createSkin() {
        skin = new Skin(Gdx.files.internal("UISkin/skin.json"));
    }

    /**
     * New for assessment 2
     * new version of setScreen to stop the game screen being deleted before a new screen is set if the new screen is pause.
     * @param screen the screen to be changed to
     */
    @Override
    public void setScreen (Screen screen) {
        if (this.screen != null && screen != pause) this.screen.hide();
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    /**
     * New for assessment 2
     * Restarts the game by reinitialising the Managers and creating a new instance of GameScreen
     */
    public void restartGame(){
        // can't load any more resources after this point (just functionally I choose not to implement)
        RenderingManager.Initialize();
        EntityManager.Initialize();
        GameManager.Initialize(difficulty);

        game = new GameScreen(this, id_map);

        setScreen(menu);


    }

    /**
     * New for assesment 2
     * Changes the difficulty for the game by changing the enum and calling GameMananger.getSettings()
     */
    public void setDifficulty(String selected){
        if (selected == "Easy"){
            difficulty = GameDifficulty.Easy;
        }
        else if (selected == "Regular") {
            difficulty = GameDifficulty.Regular;
        }
        else if (selected == "Hard") {
            difficulty = GameDifficulty.Hard;
        }
        else{
            difficulty = GameDifficulty.Regular;

        }
    }
    /**
    Added for assessment 2 so that the game doesn't start until after the difficulty has been chosen by the player
     */
    public void StartGame(){
        //GameManager.Initialize(difficulty);
        PhysicsManager.Initialize();
        GameManager.Initialize(difficulty);
        game = new GameScreen(this, id_map);
        setScreen(game);


    }
}

