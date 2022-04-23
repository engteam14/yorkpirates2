package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Managers.GameDifficulty;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.Managers.SaveManager;
import com.mygdx.game.PirateGame;

import static com.mygdx.utils.Constants.VIEWPORT_HEIGHT;

/**
 * Contains widgets defining the start-of-game menu screen.
 */
public class MenuScreen extends Page {
    private SelectBox<String> hardness;

    /**
     * Creates a Menu Screen
     * @param parent The game object this screen is viewing
     */
    public MenuScreen(PirateGame parent) {
        super(parent);
    }

    /**
     * Create menu widgets such as start button, labels, etc.
     */
    @Override
    protected void CreateActors() {
        Table t = new Table();
        t.setFillParent(true);

        float space = VIEWPORT_HEIGHT * 0.15f;

        t.setBackground(new TextureRegionDrawable(ResourceManager.getTexture("menuBG.jpg")));
        Label l = new Label("Pirates the movie the game", parent.skin);
        l.setFontScale(2);
        t.add(l).top().spaceBottom(space);
        t.row();

        hardness = new SelectBox<>(parent.skin);


        String[] values = new String[]{"Easy", "Regular", "Hard"};
        hardness.setItems(values);
        hardness.setSelected(values[1]);
        t.add(hardness);
        t.row();
        t.add().pad(20);
        t.row();
        TextButton play = new TextButton("Play", parent.skin);
        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // start of assessment 2 change
                parent.setDifficulty(hardness.getSelected());
                parent.StartGame();
                //end of assessment 2 change
            }
        });
        t.add(play).top().size(100, 25).spaceBottom(space);
        t.row();
        t.add().pad(20);
        t.row();
        Preferences prefs = Gdx.app.getPreferences("pirate/GameSave_game_1");
        String resume = "Resume";
        if (prefs.get().isEmpty()) {
            resume = "";
        }
        TextButton playLoad = new TextButton(resume, parent.skin);
        playLoad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // start of assessment 2 change
                parent.LoadGame();
                //end of assessment 2 change
            }
        });
        t.add(playLoad).top().size(100, 25).spaceBottom(space);
        t.row();
        t.add().pad(20);
        t.row();
        TextButton quit = new TextButton("Quit", parent.skin);
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
                System.exit(0);
            }
        });
        t.add(quit).size(100, 25).top().spaceBottom(space);

        t.top();

        actors.add(t);
    }

    /**
     * Shows the Menu Screen
     */
    @Override
    public void show() {
        super.show();
    }

    /**
     * Hides the Menu Screen
     */
    @Override
    public void hide() {
        super.hide();
    }

    /**
     * Resizes the viewport
     * @param width The horizontal dimension of the viewport
     * @param height The vertical dimension of the viewport
     */
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Table t = (Table) actors.get(0);
        t.setBackground(new TextureRegionDrawable(ResourceManager.getTexture("menuBG.jpg"))); // prevent the bg being stretched
    }
}
