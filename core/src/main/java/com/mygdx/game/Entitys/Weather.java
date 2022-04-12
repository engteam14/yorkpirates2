package com.mygdx.game.Entitys;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.utils.Utilities;

public class Weather extends Obstacle {

    private final float speed;
    private final long freq;

    private long lastDir;
    private Vector2 dir;

    public Weather(float damage, float hitRate) {
        super("storm", true, damage, hitRate, -1);

        speed = GameManager.getSettings().get("Weather").getFloat("maxSpeed");
        freq = GameManager.getSettings().get("Weather").getInt("moveFreq");

        lastDir = 0;
        newDir();
    }

    @Override
    public void update() {
        super.update();

        if (TimeUtils.timeSinceMillis(lastDir) > freq) newDir();
        getComponent(RigidBody.class).setVelocity(dir);
    }

    private void newDir() {
        dir = Utilities.randomPos(-1, 1);
        dir = new Vector2(dir.x * speed, dir.y * speed);
        lastDir = TimeUtils.millis();
    }

}
