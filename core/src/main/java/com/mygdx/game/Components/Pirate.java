package com.mygdx.game.Components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.Entitys.Ship;
import com.mygdx.game.Faction;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.utils.QueueFIFO;

import java.util.HashMap;

/**
 * Gives the concepts of health plunder, etc. Allows for firing of cannonballs, factions, death, targets
 */
public class Pirate extends Component {
    /**
     * // Assessment 2 Change: Refactored to use map for storing values which will be modified by power-ups.
     * The enemy that is being targeted by the AI.
     */
    private int factionId;
    private final HashMap<String,Float> defaults;
    private final HashMap<String,Float> values;
    private int points;
    protected boolean isAlive;

    private boolean infiniteAmmo; // Added for Assessment 2 for power-ups and colleges
    private final QueueFIFO<Ship> targets;
    //private int plunder;

    /**
     * Creates the base Pirate Component, setting up its initial values
     */
    public Pirate() {
        super();
        targets = new QueueFIFO<>();
        type = ComponentType.Pirate;
        factionId = 1;
        isAlive = true;

        values = new HashMap<>();
        JsonValue starting = GameManager.getSettings().get("starting");
        values.put("health", (float) starting.getInt("health"));
        values.put("damage", (float) starting.getInt("damage"));
        values.put("ammo", (float) starting.getInt("ammo"));
        values.put("plunder", (float) starting.getInt("plunder"));

        values.put("plunderRate", 1f);
        values.put("defense", 1f);

        defaults = new HashMap<>(values);
    }

    /**
     * // New for assessment 2 //
     * Get a Pirate value.
     * @param key   The value to get
     * @return      The value
     */
    public float getValue(String key) {
        return values.get(key);
    }

    /**
     * // New for assessment 2 //
     * Set a new value for Pirate while holding reference to what it was originally.
     * @param key       The value to set to
     * @param value     The float to apply
     */
    public void setValue(String key, float value) {
        values.replace(key, value);
    }

    /**
     * // New for assessment 2 //
     * Multiply a value for Pirate while holding reference to what it was originally.
     * @param key       The value to multiply
     * @param mult      The multiplication factor
     */
    public void multValue(String key, float mult) {
        values.replace(key, values.get(key) * mult);
    }

    /**
     * // New for assessment 2 //
     * Reset a Pirate value to what it originally was.
     * @param key       The value to reset
     */
    public void resetToDefault(String key) {
        values.replace(key, defaults.get(key));
    }

    /**
     * @param target The Ship to be added to the list of potential targets
     */
    public void addTarget(Ship target) {
        targets.add(target);
    }

    /**
     * @return the plunder value attached to this pirate
     */
    public int getPlunder() {
        return values.get("plunder").intValue();
    }

    /**
     * @return the points value attached to this pirate
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param money The integer value to be added to the pirate's stored plunder amount
     */
    public void addPlunder(int money) {
        // Assessment 2 change: Plunder additions are now multiplied by plunderRate value
        values.replace("plunder", values.get("plunder")+ Math.round(money * values.get("plunderRate")));
    }

    /**
     * @param increment The integer value to be added to the pirate's stored point amount
     */
    public void addPoints(int increment) {
        points += increment;
    }

    /**
     * @return the Faction value attached to this pirate
     */
    public Faction getFaction() {
        return GameManager.getFaction(factionId);
    }

    /**
     * @param factionId The ID of the faction to set this pirate to
     */
    public void setFactionId(int factionId) {
        this.factionId = factionId;
    }

    /**
     * Added for Assessment 2, sets whether the pirate can ignore ammo costs for firing
     * @param status boolean value to set infiniteAmmo to
     */
    public void setInfiniteAmmo(boolean status) {
        infiniteAmmo = status;
    }

    /**
     * Added for Assessment 2
     * @return the damage dealt by attacks from this unit
     */
    public Float getAttackDmg() {
        return values.get("damage");
    }

    /**
     * Deducts damage taken from Pirate's health and kills the pirate if that reduces health to less than or equal to 0
     * @param dmg The float value to be removed from this pirate's health total
     */
    public void takeDamage(float dmg) {
        // Assessment 2 change: Refactored to include key for health instead of variable, as well as factor in new defense value
        dmg *= (1f/values.get("defense"));
        values.replace("health", getHealth() - dmg);
        if (getHealth() <= 0) kill();
    }

    /**
     * Changed for Assessment 2:
     *  - Removed functionality and replaced with function call to a different function,
     *    to allow for differentiation between when a start position is or isn't provided.
     * @param direction the direction to shoot in
     */
    public void shoot(Vector2 direction) {
        shoot(parent.getComponent(Transform.class).getPosition().cpy(),direction);
    }

    /**
     * Added for Assessment 2
     * Will shoot a cannonball from the startPos in the direction, assigning this.Parent as the cannonball's parent
     * @param direction the direction to shoot in
     */
    public void shoot(Vector2 startPos, Vector2 direction){
        if(!infiniteAmmo && (getAmmo() == 0)){
            return;
        }
        if(!infiniteAmmo){
            values.replace("ammo", getAmmo()-1f);
        }
        GameManager.shoot(parent, startPos, direction);
    }

    /**
     * @return the health value attached to this pirate
     */
    public int getHealth() {
        // Assessment 2 change: Refactored to use key for health instead of variable
        return Math.round(values.get("health"));
    }

    /**
     * if dst to target is less than attack range
     * target will be null if not in agro range
     */
    public boolean canAttack() {
        final Ship p = (Ship) parent;
        final Vector2 pos = p.getPosition();
        // within attack range
        if (targets.peek() != null) {
            final float dst = pos.dst(targets.peek() != null ? targets.peek().getPosition() : null); // Assessment 2 change: Fixed to not error on null
            return dst < Ship.getAttackRange();
        }else{
            return false;
        }
    }

    /**
     * if dst to target is >= attack range
     * target will be null if not in agro range
     */
    public boolean isAggro() {
        final Ship p = (Ship) parent;
        final Vector2 pos = p.getPosition();
        final float dst = pos.dst(targets.peek() != null ? targets.peek().getPosition() : null);
        // out of attack range but in aggro range
        if (targets.peek() != null) {
            return dst >= Ship.getAttackRange();
        }else{
            return false;
        }
    }

    /**
     * @return the current target value attached to this pirate
     */
    public Ship getTarget() {
        return targets.peek();
    }

    /**
     * @return the boolean alive value attached to this pirate
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Kill its self
     */
    public void kill() {
        // Assessment 2 change: Refactored to use key for health instead of variable
        values.replace("health", 0f);
        isAlive = false;
    }

    /**
     * @param ammo The integer to be added to the amount of ammo this pirate has access to
     */
    public void setAmmo(int ammo) {
        // Assessment 2 change: Refactored to use key for ammo instead of variable
        values.replace("ammo", (float) ammo);
    }

    /**
     * @return the ammo value attached to this pirate
     */
    public int getAmmo() {
        // Assessment 2 change: Refactored to use key for ammo instead of variable
        return Math.round(values.get("ammo"));
    }

    public QueueFIFO<Ship> getTargets() {
        return targets;
    }

    //public int targetCount() {
    //        return targets.size();
    //    }

    //public void removeTarget() {
    //        targets.pop();
    //    }

    //public void reload(int ammo) {
    //        // Assessment 2 change: Refactored to use key for ammo instead of variable
    //        values.replace("ammo", (float) getAmmo()+ammo);
    //    }
}
