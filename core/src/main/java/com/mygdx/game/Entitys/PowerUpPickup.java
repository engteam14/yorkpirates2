package com.mygdx.game.Entitys;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Components.PowerUpAssigned;
import com.mygdx.game.Components.Renderable;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Managers.RenderLayer;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.Physics.CollisionCallBack;
import com.mygdx.game.Physics.CollisionInfo;
import com.mygdx.game.Physics.PhysicsBodyType;
import com.mygdx.game.PowerUps.PowerUp;

/**
 * Added for Assessment 2
 * Simple entity shown on locate quests origin
 */
public class PowerUpPickup extends Entity implements CollisionCallBack {

    private final PowerUp powerUp;
    private boolean hideToggle;
    private boolean showToggle;
    private final Vector2 position;
    private final long cooldown;
    private long lastHit;

    /**
     * Generate an obstacle.
     * @param powerUp       The Power Up being picked up
     * @param texName       The texture to show for the obstacle.
     * @param pos           The position of the power up
     * @param cooldown      The cooldown before the power up can be used again
     */
    public PowerUpPickup(PowerUp powerUp, String texName, Vector2 pos, int cooldown) {
        super(3);

        Transform t = new Transform();
        Renderable r = new Renderable(ResourceManager.getId("powerups.txt"), texName, RenderLayer.Transparent);
        RigidBody rb = new RigidBody(PhysicsBodyType.Kinematic, r, t, true);
        rb.setCallback(this);

        addComponents(t, r, rb);

        this.powerUp = powerUp;

        hideToggle = false;
        showToggle = false;

        position = pos;
        setPosition(pos);

        this.cooldown = 1000L * cooldown;
        lastHit = 0;
    }

    /**
     * @param pos   The position of the power up to be set
     */
    private void setPosition(Vector2 pos) {
        getComponent(RigidBody.class).setPosition(pos);
    }

    /**
     * Reveals the power-up if its cooldown has expired
     */
    private void tryShow() {
        if (TimeUtils.timeSinceMillis(lastHit) > cooldown) {
            setPosition(position);
            showToggle = false;
        }
    }

    /**
     * Hides the power-up
     */
    private void tryHide() {
        setPosition(new Vector2(1000, -1000));
        hideToggle = false;
        showToggle = true;
        lastHit = TimeUtils.millis();
    }

    /**
     * Called once per frame
     */
    @Override
    public void update() {
        super.update();
        if      (hideToggle)    tryHide();
        else if (showToggle)    tryShow();
    }

    /**
     * applies the attached power up to the colliding entity then sets the power up to be hidden next update
     * @param info the collision info used in the conjunction
     */
    @Override
    public void EnterTrigger(CollisionInfo info) {
        PowerUpAssigned playerPow = info.a.getComponent(PowerUpAssigned.class);
        playerPow.AssignPowerUp(powerUp);
        hideToggle = true;
    }

    /**
     * `unused`
     */
    @Override
    public void BeginContact(CollisionInfo info) {
    }

    /**
     * `unused`
     */
    @Override
    public void EndContact(CollisionInfo info) {
    }

    /**
     * `unused`
     */
    @Override
    public void ExitTrigger(CollisionInfo info) {
    }
}
