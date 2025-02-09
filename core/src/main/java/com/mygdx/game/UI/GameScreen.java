package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Components.ComponentEvent;
import com.mygdx.game.Components.PlayerController;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Managers.*;
import com.mygdx.game.PirateGame;
import com.mygdx.game.PowerUps.PowerUp;
import com.mygdx.game.Quests.Quest;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mygdx.utils.Constants.*;

public class GameScreen extends Page {
    private Label healthLabel;
    private Label money;
    private Label points;
    private Label ammo;
    private final Label questDesc;
    private final Label questName;

    public ArrayList<TextButton> powerUpButtons; // Added for Assessment 2, keep track of Shop Buttons
    public Pixmap pixmap;
    private float accumulator;

    /**
     * Boots up the actual game: starts PhysicsManager, GameManager, EntityManager,
     * loads texture atlases into ResourceManager. Draws quest and control info.
     *
     * @param parent PirateGame UI screen container
     * @param id_map the resource id of the tile map
     */
    public GameScreen(PirateGame parent, int id_map) {
        super(parent);
        INIT_CONSTANTS();
        PhysicsManager.Initialize(false);

        /*int id_ship = ResourceManager.addTexture("ship.png");
        int id_map = ResourceManager.addTileMap("Map.tmx");
        int atlas_id = ResourceManager.addTextureAtlas("Boats.txt");
        int extras_id = ResourceManager.addTextureAtlas("UISkin/skin.atlas");
        int buildings_id = ResourceManager.addTextureAtlas("Buildings.txt");
        ResourceManager.loadAssets();*/

        GameManager.SpawnGame(id_map);
        //QuestManager.addQuest(new KillQuest(c));

        EntityManager.raiseEvents(ComponentEvent.Awake, ComponentEvent.Start);

        Window questWindow = new Window("Current Quest", parent.skin);

        Quest q = QuestManager.currentQuest();
        Table t = new Table();
        questName = new Label("NAME", parent.skin);
        t.add(questName);
        t.row();
        questDesc = new Label("DESCRIPTION", parent.skin);
        if (q != null) {
            questName.setText(q.getName());
            questDesc.setText(q.getDescription());
        }

        t.add(questDesc).left();
        questWindow.add(t);
        actors.add(questWindow);

        Table t1 = new Table();
        t1.top().right();
        t1.setFillParent(true);
        actors.add(t1);

        Window tutWindow = new Window("Controls", parent.skin);
        Table table = new Table();
        tutWindow.add(table);
        t1.add(tutWindow);

        table.add(new Label("Move with", parent.skin)).top().left();
        table.add(new Image(parent.skin, "key-w"));
        table.add(new Image(parent.skin, "key-s"));
        table.add(new Image(parent.skin, "key-a"));
        table.add(new Image(parent.skin, "key-d"));
        table.row();
        table.add(new Label("Shoot in direction of mouse", parent.skin)).left();
        //table.add(new Image(parent.skin, "space"));
        table.add(new Image(parent.skin, "mouse"));
        table.row();
        table.add(new Label("Shoot in direction of ship", parent.skin)).left();
        table.add(new Image(parent.skin, "space"));
        table.row();
        table.add(new Label("Pause", parent.skin)).left(); // changed to pause for assessment 2
        table.add(new Image(parent.skin, "key-esc"));

        HashMap<String, PowerUp> powerUps = new HashMap<>();

        //create power ups
        for (JsonValue powData : GameManager.getSettings().get("powerups")) {
            PowerUp pow = new PowerUp(powData);
            powerUps.put(powData.getString("id"), pow);
        }

        // start of addition for assessment 2 to add a shop to the UI
        Table shop = new Table();
        Window shopWin = new Window("Shop", parent.skin);
        Table shopTable = new Table();

        shop.bottom().right();
        shop.setFillParent(true);
        shop.add(shopWin);
        shopWin.add(shopTable);
        shopTable.pad(10);

        powerUpButtons = new ArrayList<>();

        //power up 1
        PowerUp pow1 = powerUps.get("1");
        TextButton powerUp1 = new TextButton(String.format("$%s - %s", pow1.getCost(), pow1.getName()), parent.skin);
        powerUp1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pow1.buyPowerUp();
            }
        });
        powerUpButtons.add(powerUp1);

        //power up 2
        PowerUp pow2 = powerUps.get("2");
        TextButton powerUp2 = new TextButton(String.format("$%s - %s", pow2.getCost(), pow2.getName()), parent.skin);
        powerUp2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pow2.buyPowerUp();
            }
        });
        powerUpButtons.add(powerUp2);

        //power up 3
        PowerUp pow3 = powerUps.get("3");
        TextButton powerUp3 = new TextButton(String.format("$%s - %s", pow3.getCost(), pow3.getName()), parent.skin);
        powerUp3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pow3.buyPowerUp();
            }
        });
        powerUpButtons.add(powerUp3);

        //power up 4
        PowerUp pow4 = powerUps.get("4");
        TextButton powerUp4 = new TextButton(String.format("$%s - %s", pow4.getCost(), pow4.getName()), parent.skin);
        powerUp4.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pow4.buyPowerUp();
            }
        });
        powerUpButtons.add(powerUp4);

        //power up 5
        PowerUp pow5 = powerUps.get("5");
        TextButton powerUp5 = new TextButton(String.format("$%s - %s", pow5.getCost(), pow5.getName()), parent.skin);
        powerUp5.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pow5.buyPowerUp();
            }
        });
        powerUpButtons.add(powerUp5);

        //power up 6
        PowerUp pow6 = powerUps.get("6");
        TextButton powerUp6 = new TextButton(String.format("$%s - %s", pow6.getCost(), pow6.getName()), parent.skin);
        powerUp6.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pow6.buyPowerUp();
            }
        });
        powerUpButtons.add(powerUp6);

        //power up 7
        PowerUp pow7 = powerUps.get("7");
        TextButton powerUp7 = new TextButton(String.format("$%s - %s", pow7.getCost(), pow7.getName()), parent.skin);
        powerUp7.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pow7.buyPowerUp();
            }
        });
        powerUpButtons.add(powerUp7);

        //add power ups to shop table
        for(TextButton button : powerUpButtons) {
            shopTable.add(button).pad(10);
            shopTable.row();
        }

        Player player = GameManager.getPlayer();
        player.getComponent(PlayerController.class).setButtons(powerUpButtons);
        actors.add(shop);
        // end of addition for assessment 2
    }

    /**
     * Called every frame calls all other functions that need to be called every frame by raising events and update methods
     *
     * @param delta delta time
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(BACKGROUND_COLOUR.x, BACKGROUND_COLOUR.y, BACKGROUND_COLOUR.z, 1);
        EntityManager.raiseEvents(ComponentEvent.Update, ComponentEvent.Render);

        accumulator += EntityManager.getDeltaTime();

        // fixed update loop so that physics manager is called regally rather than somewhat randomly
        while (accumulator >= PHYSICS_TIME_STEP) {
            PhysicsManager.update();
            accumulator -= PHYSICS_TIME_STEP;
        }

        GameManager.update();
        // show end screen if esc is pressed
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            parent.pause();
            pixmap = Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            parent.setScreen(parent.pause);
        }
        super.render(delta);
    }

    /**
     * disposed of all stuff it something is missing from this method you will get memory leaks
     */
    @Override
    public void dispose() {
        super.dispose();
        ResourceManager.cleanUp();
        EntityManager.cleanUp();
        RenderingManager.cleanUp();
        PhysicsManager.cleanUp();
    }

    /**
     * Resize camera, effectively setting the viewport to display game assets
     * at pixel ratios other than 1:1.
     *
     * @param width  of camera viewport
     * @param height of camera viewport
     */
    @Override
    public void resize(int width, int height) {
        //((Table) actors.get(0)).setFillParent(false);
        super.resize(width, height);
        OrthographicCamera cam = RenderingManager.getCamera();
        cam.viewportWidth = width / ZOOM;
        cam.viewportHeight = height / ZOOM;
        cam.update();

        // ((Table) actors.get(0)).setFillParent(true);
    }

    /**
     * Update the UI with new values for health, quest status, etc.
     * also called once per frame but used for actors by my own convention
     */
    //private String prevQuest = "";
    @Override
    protected void update() {
        super.update();
        Player p = GameManager.getPlayer();

        healthLabel.setText(String.valueOf(p.getHealth()));
        money.setText(String.valueOf(p.getPlunder()));
        points.setText(String.valueOf(p.getPoints()));
        ammo.setText(String.valueOf(p.getAmmo()));
        if (!QuestManager.anyQuests()) {
            parent.end.win();
            parent.setScreen(parent.end);

        } else {
            Quest q = QuestManager.currentQuest();
            /*if(Objects.equals(prevQuest, "")) {
                prevQuest = q.getDescription();
            }
            if(!Objects.equals(prevQuest, q.getDescription())) {
                questComplete.setText("Quest Competed");
                prevQuest = "";
            }*/
            assert q != null;
            questName.setText(q.getName());
            questDesc.setText(q.getDescription());
        }
        if (!p.isAlive()) {
            parent.setScreen(parent.end);
        }
        /*if(!questComplete.getText().equals("")) {
            showTimer += EntityManager.getDeltaTime();
        }
        if(showTimer >= showDuration) {
            showTimer = 0;
            questComplete.setText("");
        }*/
    }

    /**
     * Draw UI elements showing player health, plunder, and ammo.
     */
    @Override
    protected void CreateActors() {
        Table table = new Table();
        table.setFillParent(true);
        actors.add(table);

        table.add(new Image(parent.skin.getDrawable("heart"))).top().left().size(1.25f * TILE_SIZE);
        healthLabel = new Label("N/A", parent.skin);
        table.add(healthLabel).top().left().size(50);

        table.row();
        table.setDebug(false);

        table.add(new Image(parent.skin.getDrawable("coin"))).top().left().size(1.25f * TILE_SIZE);
        money = new Label("N/A", parent.skin);
        table.add(money).top().left().size(50);

        table.row();

        table.add(new Image(parent.skin.getDrawable("point"))).top().left().size(1.25f * TILE_SIZE);
        points = new Label("N/A", parent.skin);
        table.add(points).top().left().size(50);

        table.row();

        table.add(new Image(parent.skin.getDrawable("ball"))).top().left().size(1.25f * TILE_SIZE);
        ammo = new Label("N/A", parent.skin);
        table.add(ammo).top().left().size(50);

        table.top().left();
    }
}
