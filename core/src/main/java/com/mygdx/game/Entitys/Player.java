package com.mygdx.game.Entitys;

import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.PlayerController;
import com.mygdx.game.Faction;
import com.mygdx.game.Managers.GameManager;

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
}
