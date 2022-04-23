package com.mygdx.game.Entitys;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.PlayerController;
import com.mygdx.game.Faction;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.UI.EndScreen;
import com.mygdx.utils.Utilities;

/**
 * Player's ship entity.
 */
public class Player extends Ship {
    private long lastPointTime;

    /**
     * Adds ship with PlayerController component and sets its speed.
     *
     * @param speed of movement across map
     */
    private Player(float speed) {
        super();

        PlayerController pc = new PlayerController(this, speed);
        addComponent(pc);
        setName("Player");

        lastPointTime = TimeUtils.millis() / 1000;
    }

    /**
     * Adds ship with PlayerController component, loading its speed from GameManager settings.
     */
    public Player() {
        this(GameManager.getSettings().get("starting").getFloat("playerSpeed"));
    }

    /**
     * Is run once per frame
     */
    @Override
    public void update(){
        super.update();
        isAlive();
        long current = TimeUtils.millis() / 1000;
        if (current > lastPointTime) {
            points(1);
        }
        lastPointTime = current;
    }

    /**
     * Cleans up hanging data parts
     */
    @Override
    public void cleanUp() {
        super.cleanUp();
    }

    /**
     * @return the amount of ammo available to the player
     */
    public int getAmmo() {
        return getComponent(Pirate.class).getAmmo();
    }

    /**
     * Added for Assessment 2
     * @return the Faction attached to the pirate component of this object
     */
	public Faction getFaction() {
        return getComponent(Pirate.class).getFaction();
	}

}
