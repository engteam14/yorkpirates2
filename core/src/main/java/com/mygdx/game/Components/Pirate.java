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
    private int factionId;
    private HashMap<String,Float> defaults;
    private HashMap<String,Float> values;
    private int plunder;
    protected boolean isAlive;

    /**
     * // Assessment 2 Change: Refactored to use map for storing values which will be modified by powerups.
     * The enemy that is being targeted by the AI.
     */
    private final QueueFIFO<Ship> targets;

    public Pirate() {
        super();
        targets = new QueueFIFO<>();
        type = ComponentType.Pirate;
        plunder = GameManager.getSettings().get("starting").getInt("plunder");
        factionId = 1;
        isAlive = true;

        values = new HashMap<>();
        JsonValue starting = GameManager.getSettings().get("starting");
        values.put("health", (float) starting.getInt("health"));
        values.put("damage", (float) starting.getInt("damage"));
        values.put("ammo", (float) starting.getInt("ammo"));
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
     * Set the default for a Pirate value.
     * @param key       The value to set to
     * @param value     The default to apply
     */
    public void setDefault(String key, float value) {
        values.replace(key, value);
        defaults.replace(key, value);
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

    public void addTarget(Ship target) {
        targets.add(target);
    }

    public int getPlunder() {
        return plunder;
    }

    public void addPlunder(int money) {
        // Assessment 2 change: Plunder additions are now multiplied by plunderRate value
        plunder += Math.round(money * values.get("plunderRate"));
    }

    public Faction getFaction() {
        return GameManager.getFaction(factionId);
    }

    public void setFactionId(int factionId) {
        this.factionId = factionId;
    }

    public void takeDamage(float dmg) {
        // Assessment 2 change: Refactored to include key for health instead of variable, as well as factor in new defense value
        dmg *= (1f/values.get("defense"));
        values.replace("health", getHealth() - dmg);
        if (getHealth() <= 0) kill();
    }

    /**
     * Will shoot a cannonball assigning this.parent as the cannonball's parent (must be Ship atm)
     *
     * @param dir the direction to shoot in
     */
    public void shoot(Vector2 dir) {
        // Assessment 2 change: Refactored to include key for ammo instead of variable
        if (getAmmo() == 0) {
            return;
        }
        values.replace("ammo", getAmmo()-1f);
        GameManager.shoot(parent, dir); // Changed for Assessment 2, casting from Entity to ship removed
    }

    /**
     * Adds ammo
     *
     * @param ammo amount to add
     */
    public void reload(int ammo) {
        // Assessment 2 change: Refactored to use key for ammo instead of variable
        values.replace("ammo", (float) getAmmo()+ammo);
    }

    public int getHealth() {
        // Assessment 2 change: Refactored to use key for health instead of variable
        return Math.round(values.get("health"));
    }

    /**
     * if dst to target is less than attack range
     * target will be null if not in agro range
     */
    public boolean canAttack() {
        if (targets.peek() != null) {
            final Ship p = (Ship) parent;
            final Vector2 pos = p.getPosition();
            final float dst = pos.dst(targets.peek().getPosition());
            // withing attack range
            return dst < Ship.getAttackRange();
        }
        return false;
    }

    /**
     * if dst to target is >= attack range
     * target will be null if not in agro range
     */
    public boolean isAgro() {
        if (targets.peek() != null) {
            final Ship p = (Ship) parent;
            final Vector2 pos = p.getPosition();
            final float dst = pos.dst(targets.peek().getPosition());
            // out of attack range but in agro range
            return dst >= Ship.getAttackRange();
        }
        return false;
    }

    public Ship getTarget() {
        return targets.peek();
    }

    public void removeTarget() {
        targets.pop();
    }

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

    public void setAmmo(int ammo) {
        // Assessment 2 change: Refactored to use key for ammo instead of variable
        values.replace("ammo", (float) ammo);
    }

    public int getAmmo() {
        // Assessment 2 change: Refactored to use key for ammo instead of variable
        return Math.round(values.get("ammo"));
    }

    public int targetCount() {
        return targets.size();
    }

    public QueueFIFO<Ship> getTargets() {
        return targets;
    }
}
