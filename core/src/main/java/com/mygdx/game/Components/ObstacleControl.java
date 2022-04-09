package com.mygdx.game.Components;

import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Entitys.Obstacle;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Physics.CollisionInfo;

/**
 * // Added for Assessment 2 Requirements //
 * Makes an Entity act as an Obstacle to the Player, causing damage upon collision in a variety of differing ways.
 */
public class ObstacleControl extends Component {


    private final float hitDamage;
    private final float hitRate;
    private final int hitLimit;

    private long lastHit;
    private int hitCount;

    /**
     * Generate an obstacle controller component.
     * @param damage        The damage that the obstacle does per 'hit'
     * @param hitRate       The rate at which 'hits' occur while colliding
     * @param hitLimit      The number of 'hits' required to break the obstacle
     */
    public ObstacleControl(float damage, float hitRate, int hitLimit) {
        this.hitDamage = damage;
        this.hitRate = hitRate * 1000f;
        this.hitLimit = hitLimit;

        lastHit = 0;
        hitCount = 0;
    }

    public void TryHit(CollisionInfo collision, boolean enter) {
        boolean doHit = (enter || (TimeUtils.timeSinceMillis(lastHit) >= hitRate && hitRate > 0));
        if (collision.a instanceof Player && doHit) {
            Player player = (Player) collision.a;
            player.getComponent(Pirate.class).takeDamage(hitDamage);
            lastHit = TimeUtils.millis();
            hitCount++;
            if (hitCount >= hitLimit && hitLimit > 0) {
                ((Obstacle) parent).kill();
            }
        }
    }

}
