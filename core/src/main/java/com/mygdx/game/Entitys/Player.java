package com.mygdx.game.Entitys;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.PlayerController;
import com.mygdx.game.Faction;
import com.mygdx.game.Managers.GameManager;
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

    @Override
    public void cleanUp() {
        super.cleanUp();
    }

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

    /**
     * Added for Assessment 2
     * Checks if the player is alive and kills it if it is dead
     * @return the boolean containing the player's current life status
     */
    @Override
    public boolean isAlive() {
        boolean alive = (getComponent(Pirate.class).getHealth() > 0);
        if (!alive) {
            lose();
        }
        return alive;
    }

    /**
     * Added for Assessment 2
     * Ends the game when player has lost
     */
    public void lose(){
        System.out.println("Game Over");
    }
}
