package com.mygdx.game.Entitys;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Faction;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.utils.Utilities;

import java.util.ArrayList;

/**
 * Defines a college and its associated buildings.
 */
public class College extends Entity {
    private static ArrayList<String> buildingNames;
    private final ArrayList<Building> buildings;
    public Faction f;
    private Faction mostRecentAttacker; //Added for Assessment 2, used to determine ownership after capturing
    private Building flag; //Added for Assessment 2, allows flag to be referenced independently of other buildings.
    private long lastShootTime;
    private float buffer;


    public College() {
        super();
        buildings = new ArrayList<>();
        buildingNames = new ArrayList<>();
        buildingNames.add("big");
        buildingNames.add("small");
        buildingNames.add("clock");
        Transform t = new Transform();
        Pirate p = new Pirate();
        addComponents(t, p);
        lastShootTime = TimeUtils.millis() / 1000;
    }

    /**
     * Creates a college at the location associated with the given faction id.
     * Changed for Assessment 2:
     *  - Set infinite ammo to true in Pirate Component
     * @param factionId numerical id of the faction
     */
    public College(int factionId) {
        this();
        f = GameManager.getFaction(factionId);
        Transform t = getComponent(Transform.class);
        t.setPosition(f.getPosition());
        Pirate p = getComponent(Pirate.class);
        p.setFactionId(factionId);
        p.setInfiniteAmmo(true);
        spawn(f.getColour());
    }

    /**
     * Randomly populates the college radius with buildings.
     * Changed for Assessment 2:
     *  - Set buffer in Pirate Component to the radius of the college
     * @param colour used to pull the appropriate flag sprite
     */
    private void spawn(String colour) {
        JsonValue collegeSettings = GameManager.getSettings().get("college");
        float radius = collegeSettings.getFloat("spawnRadius");
        buffer = Utilities.tilesToDistance(radius+1f) ;
        getComponent(Pirate.class).setBuffer(buffer);
        // radius = Utilities.tilesToDistance(radius) * BUILDING_SCALE;
        final Vector2 origin = getComponent(Transform.class).getPosition();
        ArrayList<Vector2> posList = new ArrayList<>();
        posList.add(new Vector2(0, 0));

        for (int i = 0; i < collegeSettings.getInt("numBuildings"); i++) {
            Vector2 pos = Utilities.randomPos(-radius, radius);
            pos = Utilities.floor(pos);

            if (!posList.contains(pos)) {
                posList.add(pos);

                pos = Utilities.tilesToDistance(pos).add(origin);

                Building b = new Building(this);
                buildings.add(b);

                String b_name = Utilities.randomChoice(buildingNames);

                b.create(pos, b_name);
            }
        }
        flag = new Building(this,true);
        buildings.add(flag);
        flag.create(origin, colour);
    }

    /**
     * True as long as unharmed buildings remain, false otherwise.
     * Changed for Assessment 2:
     *  - Added boolean return for Kill Quest functionality.
     *  - Renamed boolean for readability
     *  - Added functionality for changing Flag upon capture.
     *  @return the status of this college
     */
    public boolean isAlive() {
        boolean buildingsRemain = false;
        for (int i = 0; i < buildings.size() - 1; i++) {
            Building b = buildings.get(i);
            if (b.isAlive()) {
                buildingsRemain = true;
            }
        }
        if (!buildingsRemain) {
            getComponent(Pirate.class).kill();
            //Changes flag to that of conqueror upon defeat
            if(mostRecentAttacker != null){
                final Vector2 origin = getComponent(Transform.class).getPosition();
                flag.create(origin,mostRecentAttacker.getColour());
                getComponent(Pirate.class).setFactionId(mostRecentAttacker.getID());
            }
            //End of conqueror flag update changes
        }
        return buildingsRemain;
    }

    /**
     * Added for Assessment 2
     * @return The Faction of the Pirate Component attached to this entity
     */
    public Faction getFaction() {
        return getComponent(Pirate.class).getFaction();
    }

    /**
     * Added for Assessment 2
     * Sets the Faction which most recently attacked this College
     * @param conqueror the Faction which most recently attacked this college
     */
    public void setMostRecentAttacker(Faction conqueror){
        mostRecentAttacker = conqueror;
    }

    /**
     * Added for Assessment 2
     * @return copy of the transform's position
     */
    public Vector2 getPosition() {
        return getComponent(Transform.class).getPosition().cpy();
    }

    /**
     * Added for Assessment 2
     * @param ship the Ship being checked
     * @return displacement in form [magnitude,direction] between parsed ship and this college
     */
    public ArrayList<Vector2> displacementFromShip(Ship ship) {
        Vector2 shipLocale = ship.getPosition();
        Vector2 thisPosition = getPosition();

        float xDiff = shipLocale.x-thisPosition.x;
        float yDiff = shipLocale.y-thisPosition.y;

        Vector2 magnitude = new Vector2(Math.abs(xDiff),Math.abs(yDiff));
        Vector2 direction = new Vector2(xDiff,yDiff);

        ArrayList<Vector2> displacement = new ArrayList<>();
        displacement.add(magnitude);
        displacement.add(direction);

        return displacement;
    }

    /**
     * Added for Assessment 2 to meet requirements
     * Calls shoot function of internal component
     * @param startPos the position the cannonball is to be spawned at
     * @param direction the direction the cannonball is to be fired in
     */
    public void shoot(Vector2 startPos, Vector2 direction) {
        getComponent(Pirate.class).shoot(startPos, direction);
    }

    /**
     * Added for Assessment 2
     * Systematically kills each building attached to this college, then marks the college as dead.
     */
    public void killThisCollege() {
        for (int i = 0; i < buildings.size() - 1; i++) {
            Building b = buildings.get(i);
            if (b.isAlive()) {
                b.destroy(null);
            }
        }
        isAlive();
    }

    /**
     * Runs once per frame
     * Changed for Assessment 2:
     *  - Added checks for if the college is in a specific range of the player
     *  - Added functionality where the college shoots at the player if on opposing factions and in range.
     */
    @Override
    public void update() {
        super.update();
        isAlive();

        Player p = GameManager.getPlayer();
        long current = TimeUtils.millis() / 1000;
        if ( (current > lastShootTime) && !getFaction().equals(p.getFaction()) ) {

            ArrayList<Vector2> displacementToPlayer = displacementFromShip(p);
            Vector2 distanceToPlayer = displacementToPlayer.get(0);

            if( (distanceToPlayer.x < buffer*5) && (distanceToPlayer.y < buffer*5) ) {
                float scaleFactor = Math.max( Math.abs(displacementToPlayer.get(1).x) , Math.abs(displacementToPlayer.get(1).y) );
                float xBuffer = (displacementToPlayer.get(1).x / scaleFactor)*buffer;
                float yBuffer = (displacementToPlayer.get(1).y / scaleFactor)*buffer;
                Vector2 bufferShift = new Vector2(xBuffer,yBuffer);
                Vector2 bufferedStart = getPosition().add(bufferShift);

                shoot(bufferedStart, displacementToPlayer.get(1));
            }
        }
        lastShootTime = current;
    }
}
