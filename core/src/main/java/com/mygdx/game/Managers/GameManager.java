package com.mygdx.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.AI.TileMapGraph;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Entitys.*;
import com.mygdx.game.Faction;
import com.mygdx.utils.QueueFIFO;
import com.mygdx.utils.Utilities;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Responsible for creating most entity's associated with the game. Also the cached chest and cannonballs
 */
public final class GameManager {
    private static boolean initialized = false;
    private static ArrayList<Faction> factions;
    private static ArrayList<Ship> ships;
    private static ArrayList<College> colleges;

    private static final int cacheSize = 20;
    private static ArrayList<CannonBall> ballCache;
    private static int currentElement;

    private static JsonValue settings;

    private static TileMapGraph mapGraph;


    /**
     * facilitates creation of the game
     * @param difficulty contains the ENUM for the difficulty that has been selected
     */
    public static void Initialize(GameDifficulty difficulty) {
        initialized = true;
        currentElement = 0;
        // start of change for assessment 2, adds functionality for changing difficulty
        changeDifficulty(difficulty.toString());
        // end of change
        
        factions = new ArrayList<>();
        ships = new ArrayList<>();
        ballCache = new ArrayList<>(cacheSize);
        colleges = new ArrayList<>();

        for (int i = 0; i < cacheSize; i++) {
            ballCache.add(new CannonBall());
        }

        for (JsonValue v : settings.get("factions")) {
            String name = v.getString("name");
            String col = v.getString("colour");
            Vector2 pos = new Vector2(v.get("position").getFloat("x"), v.get("position").getFloat("y"));
            pos = Utilities.tilesToDistance(pos);
            Vector2 spawn = new Vector2(v.get("shipSpawn").getFloat("x"), v.get("shipSpawn").getFloat("y"));
            spawn = Utilities.tilesToDistance(spawn);
            factions.add(new Faction(name, col, pos, spawn, factions.size() + 1));
        }
    }

    /**
     * added for assessment 2
     * loads the part of the json file for the chosen difficulty and overwrites the settings values with these values
     * @param difficulty the chosen difficulty as a string
     */
    public static void changeDifficulty(String difficulty){
        JsonValue settingsAll = new JsonReader(). //change for assessment 2 for multiple difficulties
                parse(Gdx.files.internal("GameSettings.json"));
        settings = settingsAll.get("Regular");

        if (!Objects.equals(difficulty, "Regular")) {
            JsonValue editSet = settingsAll.get(difficulty);
            JsonValue.JsonIterator it = editSet.iterator();
            while (it.hasNext()) {
                JsonValue x = it.next();
                JsonValue.JsonIterator it2 = x.iterator();
                while (it2.hasNext()) {
                    JsonValue value = it2.next();
                    System.out.println(value);
                    System.out.println("." + difficulty+ ".");
                    settings.get(x.name).get(value.name).set(value.asDouble(), null);
                }
            }
        }
    }
    /**
     * called every fram checks id the quests are completed
     */
    public static void update() {
        QuestManager.checkCompleted();
    }

    /**
     * Player is always in ships at index 0
     *
     * @return the ship
     */
    public static Player getPlayer() {
        return (Player) ships.get(0);
    }

    /**
     * Get an NPCShip in the current game
     * @param id the ship's ID
     * @return the NPCShip instance
     */
    public static NPCShip getNPCShip(int id) {
        return (NPCShip) ships.get(id);
    }

    /**
     * Creates the game with player maps, NPCs, colleges
     * // Change for Assessment 2  Start //
     * Only creates world map if mapId is non-negative
     * For test purposes
     * // Change for Assessment 2  End //
     * @param mapId the resource id of the tilemap
     */
    public static void SpawnGame(int mapId) {

        if (mapId >= 0) CreateWorldMap(mapId);
        CreatePlayer();
        final int cnt = settings.get("factionDefaults").getInt("shipCount");
        for (int i = 0; i < factions.size(); i++) {
            CreateCollege(i + 1);
            for (int j = 0; j < cnt; j++) {
                // prevents halifax from having ship count + player
                if (i == 0 && j > cnt - 2) {
                    break;
                }
                NPCShip s = CreateNPCShip(i + 1);
                s.getComponent(Transform.class).setPosition(getFaction(i + 1).getSpawnPos());
            }
        }
        QuestManager.Initialize(); // added for assessment 2 to stop tryInit being used (testing)
        QuestManager.createRandomQuests(); // ""
    }

    /**
     * Creates player that belongs the faction with id 1
     */
    public static void CreatePlayer() {
        Player p = new Player();
        p.setFaction(1);
        ships.add(p);
    }

    /**
     * Creates an NPC ship with the given faction
     *
     * @param factionId desired faction
     * @return the created ship
     */
    public static NPCShip CreateNPCShip(int factionId) {
        NPCShip e = new NPCShip();
        e.setFaction(factionId);
        ships.add(e);
        return e;
    }

    /**
     * Creates the world map
     *
     * @param mapId resource id
     */
    public static void CreateWorldMap(int mapId) {
        WorldMap map = new WorldMap(mapId);
        mapGraph = new TileMapGraph(map.getTileMap());
    }

    /**
     * Creates the college with it's building for the desired college
     *
     * @param factionId desired faction
     */
    public static void CreateCollege(int factionId) {
        College c = new College(factionId);
        colleges.add(c);
    }


    public static Faction getFaction(int factionId) {
        return factions.get(factionId - 1);
    }

    /**
     * Gets the setting object from the GameSetting.json
     *
     * @return the JSON representation fo settings
     */
    public static JsonValue getSettings() {
        return settings;
    }

    public static College getCollege(int factionId) {
        return colleges.get(factionId - 1);
    }

    /**
     * Added for Assessment 2
     * @return Returns the next cannonball that would be fired from the cache
     */
    public static CannonBall getCurrentCannon() {
        return ballCache.get(currentElement);
    }

    /**
     * Utilises the cached cannonballs to fire one
     * Changed for Assessment 2, separated incrementer for visual clarity and parameterised startPos
     * @param p     parent
     * @param pos   position projectile is spawned at
     * @param dir   shoot direction
     */
    public static void shoot(Entity p, Vector2 pos, Vector2 dir) { // Changed for Assessment 2, type switched from Ship to Entity
        ballCache.get(currentElement).fire(p,pos, dir);
        currentElement++;
        currentElement %= cacheSize;
    }

    /**
     * uses a* not sure if it works but i think it does
     *
     * @param loc src
     * @param dst dst
     * @return queue of delta positions
     */
    public static QueueFIFO<Vector2> getPath(Vector2 loc, Vector2 dst) {
        return mapGraph.findOptimisedPath(loc, dst);
    }

    public static void dispose(){
        initialized = true;
        currentElement = 0;

        factions = new ArrayList<>();
        ships = new ArrayList<>();
        ballCache = new ArrayList<>(cacheSize);
        colleges = new ArrayList<>();

    }
}
