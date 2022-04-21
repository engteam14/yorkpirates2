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
 * Simple entity shown on locate quests origin
 */
public class PowerUpPickup extends Entity implements CollisionCallBack {

    private final PowerUp powerUp;
    private boolean hideToggle;
    private boolean showToggle;
    private final Vector2 position;
    private final long cooldown;
    private long lastHit;

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

    private void setPosition(Vector2 pos) {
        getComponent(RigidBody.class).setPosition(pos);
    }

    private void tryShow() {
        if (TimeUtils.timeSinceMillis(lastHit) > cooldown) {
            setPosition(position);
            showToggle = false;
        }
    }

    private void tryHide() {
        setPosition(new Vector2(1000, -1000));
        hideToggle = false;
        showToggle = true;
        lastHit = TimeUtils.millis();
    }

    @Override
    public void update() {
        super.update();
        if      (hideToggle)    tryHide();
        else if (showToggle)    tryShow();
    }

    @Override
    public void BeginContact(CollisionInfo info) {
    }

    @Override
    public void EndContact(CollisionInfo info) {
    }

    @Override
    public void EnterTrigger(CollisionInfo info) {
        PowerUpAssigned playerPow = info.a.getComponent(PowerUpAssigned.class);
        playerPow.AssignPowerUp(powerUp);
        hideToggle = true;
    }

    @Override
    public void ExitTrigger(CollisionInfo info) {
    }
}
