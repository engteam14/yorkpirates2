package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.mygdx.game.Managers.SaveManager;
import com.mygdx.game.PirateGame;

import static com.mygdx.utils.Constants.VIEWPORT_HEIGHT;

/**
 * New for Assessment 2, added to allow the addition of a restart button without causing screen clutter.
 * Contains widgets defining the pause screen.
 */
public class PauseScreen extends Page{

    /**
     * Creates a Pause Screen
     * @param game The game object this screen is viewing
     */
    public PauseScreen(PirateGame game) {
        super(game);
    }

    @Override
    protected void CreateActors() {
        Table t = new Table();
      //  t.debug();

        float space = VIEWPORT_HEIGHT * 0.10f;
        t.setFillParent(true);
        actors.add(t);
        t.add();
        t.row();
        Label title = new Label("Paused", parent.skin);
        title.setFontScale(2);
        t.top();
        t.add(title).top().spaceTop(space).spaceBottom(space);
        t.row();
        TextButton resume = new TextButton("Resume", parent.skin);
        resume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.setScreen(parent.game);
            }
        });
        t.add(resume).fill().spaceBottom(space);
        t.row();
        TextButton restart = new TextButton("Restart", parent.skin);
        restart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.game.hide();
                parent.restartGame();
            }
        });
        t.add(restart).fill().spaceBottom(space);
        t.row();

        TextButton SExit = new TextButton("Save + Exit", parent.skin);
        SExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SaveManager.SaveGame();

                Gdx.app.exit();
                System.exit(0);

            }
        });
        t.add(SExit).fill().spaceBottom(space);
        t.row();
        TextButton exit = new TextButton("Exit", parent.skin);
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                Gdx.app.exit();
                System.exit(0);

            }
        });
        t.add(exit).fill().spaceBottom(space);
    }

    /**
     * Called Once per frame
     */
    @Override
    protected void update() {
        super.update();
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            parent.setScreen(parent.game);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            System.exit(0);
        }
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
        TextureRegion tex = new TextureRegion(new Texture(parent.game.pixmap));
        tex.flip(false, true);
        t.setBackground((new TextureRegionDrawable(tex)).tint(Color.GRAY));
    }
}

