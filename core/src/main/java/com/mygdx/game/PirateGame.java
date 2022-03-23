package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Managers.EntityManager;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.RenderingManager;
import com.mygdx.game.Managers.ResourceManager;
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

    /**
     * Create instances of game stage and UI screens.
     */
    @Override
    public void create() {
        RenderingManager.Initialize(); // added for assesment 2 due to rendering refactoring for testing
        // load resources
        int id_ship = ResourceManager.addTexture("ship.png");
        id_map = ResourceManager.addTileMap("Map.tmx");
        int atlas_id = ResourceManager.addTextureAtlas("Boats.txt");
        int extras_id = ResourceManager.addTextureAtlas("UISkin/skin.atlas");
        int buildings_id = ResourceManager.addTextureAtlas("Buildings.txt");

        ResourceManager.addTexture("menuBG.jpg");
        ResourceManager.addTexture("pauseBG.png");

        ResourceManager.addTexture("Chest.png");
        ResourceManager.loadAssets();
        // cant load any more resources after this point (just functionally I choose not to implement)
        stage = new Stage(new ScreenViewport());
        createSkin();

        menu = new MenuScreen(this);
        game = new GameScreen(this, id_map);
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
    public void restartGane(){
        // cant load any more resources after this point (just functionally I choose not to implement)
        RenderingManager.Initialize();
        EntityManager.Initialize();
        GameManager.Initialize();

        game = new GameScreen(this, id_map);

        setScreen(menu);


    }
}
