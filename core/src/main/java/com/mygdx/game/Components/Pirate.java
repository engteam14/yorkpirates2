package com.mygdx.game.Components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.Entitys.Ship;
import com.mygdx.game.Faction;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.utils.QueueFIFO;

/**
 * Gives the concepts of health plunder, etc. Allows for firing of cannonballs, factions, death, targets
 */
public class Pirate extends Component {
    private int factionId;
    private int plunder;
    private int points;
    protected boolean isAlive;
    private int health;
    private int ammo;
    private boolean infiniteAmmo; // Added for Assessment 2 for power-ups and colleges
    private float buffer; // Added for Assessment 2 to shift projectile spawn area
    private final int attackDmg;


    /**
     * The enemy that is being targeted by the AI.
     */
    private final QueueFIFO<Ship> targets;

    public Pirate() {
        super();
        targets = new QueueFIFO<>();
        type = ComponentType.Pirate;
        factionId = 1;
        isAlive = true;
        JsonValue starting = GameManager.getSettings().get("starting");
        plunder = starting.getInt("plunder");
        points = starting.getInt("points");
        health = starting.getInt("health");
        attackDmg = starting.getInt("damage");
        ammo = starting.getInt("ammo");
        infiniteAmmo = false;
        buffer = 0;
    }

    public void addTarget(Ship target) {
        targets.add(target);
    }

    public int getPlunder() {
        return plunder;
    }

    public int getPoints() {
        return points;
    }

    public void addPlunder(int money) {
        plunder += money;
    }

    public void addPoints(int increment) {
        points += increment;
    }

    public Faction getFaction() {
        return GameManager.getFaction(factionId);
    }

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
     * Added for Assessment 2, sets the buffer radius of this pirate
     * @param radius float value to set the buffer to
     */
    public void setBuffer(float radius) {
        buffer = radius;
    }

    public void takeDamage(float dmg) {
        health -= dmg;
        if (health <= 0) {
            health = 0;
            isAlive = false;
        }
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
     * Will shoot a cannonball from the startPos in the direction, assigning this.parent as the cannonball's parent
     * @param direction the direction to shoot in
     */
    public void shoot(Vector2 startPos, Vector2 direction){
        if(!infiniteAmmo && (ammo == 0)){
            return;
        }
        if(!infiniteAmmo){
            ammo--;
        }
        GameManager.shoot(parent, startPos, direction);
    }

    /**
     * Adds ammo
     * @param ammo amount to add
     */
    public void reload(int ammo) {
        this.ammo += ammo;
    }

    public int getHealth() {
        return health;
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
        health = 0;
        isAlive = false;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public int getAmmo() {
        return ammo;
    }

    public int targetCount() {
        return targets.size();
    }

    public QueueFIFO<Ship> getTargets() {
        return targets;
    }
}
