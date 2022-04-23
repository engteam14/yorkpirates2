package com.mygdx.game.Entitys;

import com.mygdx.game.Components.ObstacleControl;
import com.mygdx.game.Components.Renderable;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Managers.RenderLayer;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.Physics.CollisionCallBack;
import com.mygdx.game.Physics.CollisionInfo;
import com.mygdx.game.Physics.PhysicsBodyType;


/**
 * // Added for Assessment 2 Requirements //
 * A class for obstacles in the game world, both trigger or contact.
 */
public class Obstacle extends Entity implements CollisionCallBack {

    private boolean doKill;
    private boolean colliding;
    private CollisionInfo info;

    /**
     * Generate an obstacle which only triggers a hit on initial collision and does not break.
     * @param texName       The texture to show for the obstacle.
     * @param trigger       True if the obstacle is trigger, otherwise is contact.
     * @param damage        The damage that the obstacle does per 'hit'
     */
    public Obstacle(String texName, boolean trigger, float damage) {
        this(texName, trigger, damage, -1f, -1);
    }

    /**
     * Generate an obstacle.
     * @param texName       The texture to show for the obstacle.
     * @param trigger       True if the obstacle is trigger, otherwise is contact.
     * @param damage        The damage that the obstacle does per 'hit'
     * @param hitRate       The rate at which 'hits' occur while colliding
     * @param hitLimit      The number of 'hits' required to break the obstacle
     */
    public Obstacle(String texName, boolean trigger, float damage, float hitRate, int hitLimit) {
        super(4);

        Transform t = new Transform();
        Renderable r = new Renderable(ResourceManager.getId("obstacles.txt"), texName, RenderLayer.Transparent);
        RigidBody rb = new RigidBody(PhysicsBodyType.Kinematic, r, t, trigger);
        rb.setCallback(this);
        ObstacleControl o = new ObstacleControl(damage, hitRate, hitLimit);

        addComponents(t, r, rb, o);

        doKill = false;
        colliding = false;
    }

    /**
     * Sets the obstacle to be removed on next update
     */
    public void kill() {
        doKill = true;
    }

    /**
     * Runs once per frame
     */
    @Override
    public void update() {
        super.update();

        if (doKill) {
            getComponent(Transform.class).setPosition(1000, -1000);
            doKill = false;
        }
        if (colliding) getComponent(ObstacleControl.class).TryHit(info, false);
    }

    /**
     * Takes the collision info and verifies the results of the collision
     * @param info the info linked to the collision
     */
    @Override
    public void BeginContact(CollisionInfo info) {
        colliding = true;
        this.info = info;
        getComponent(ObstacleControl.class).TryHit(info, true);
    }

    /**
     * Sets the obstacle's status as not colliding
     */
    @Override
    public void EndContact(CollisionInfo info) {
        colliding = false;
    }

    /**
     * Takes the collision info and verifies the results of the collision
     * @param info the info linked to the collision
     */
    @Override
    public void EnterTrigger(CollisionInfo info) {
        colliding = true;
        this.info = info;
        getComponent(ObstacleControl.class).TryHit(info, true);
    }

    /**
     * Sets the obstacle's status as not colliding
     */
    @Override
    public void ExitTrigger(CollisionInfo info) {
        colliding = false;
    }

    //    public Obstacle(String texName, boolean trigger, float damage, int hitLimit) {
    //        this(texName, trigger, damage, -1f, hitLimit);
    //    }

    //    public Obstacle(String texName, boolean trigger, float damage, float hitRate) {
    //        this(texName, trigger, damage, hitRate, -1);
    //    }
}
