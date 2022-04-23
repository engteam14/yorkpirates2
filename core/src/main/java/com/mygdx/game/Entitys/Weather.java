package com.mygdx.game.Entitys;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.utils.Utilities;

/**
 * Added for Assessment 2 in order to meet weather condition requirements
 */
public class Weather extends Obstacle {

    private final float speed;
    private final long freq;

    private long lastDir;
    private Vector2 dir;

    /**
     * Creates a Weather Object
     * @param damage The damage dealt to an object which collides with this weather effect
     * @param hitRate The rate at which damage is dealt
     */
    public Weather(float damage, float hitRate) {
        super("storm", true, damage, hitRate, -1);

        speed = GameManager.getSettings().get("Weather").getFloat("maxSpeed");
        freq = GameManager.getSettings().get("Weather").getInt("moveFreq");

        lastDir = 0;
        newDir();
    }

    /**
     * Called once every frame
     */
    @Override
    public void update() {
        super.update();

        if (TimeUtils.timeSinceMillis(lastDir) > freq) newDir();
        getComponent(RigidBody.class).setVelocity(dir);
    }

    /**
     * Determines a new direction for the weather effect to move in
     */
    private void newDir() {
        dir = Utilities.randomPos(-1, 1);
        dir = new Vector2(dir.x * speed, dir.y * speed);
        lastDir = TimeUtils.millis();
    }

}
