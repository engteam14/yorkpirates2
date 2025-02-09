package com.mygdx.game.Entitys;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.Renderable;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Faction;
import com.mygdx.game.Managers.EntityManager;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.RenderLayer;
import com.mygdx.game.Physics.CollisionCallBack;
import com.mygdx.game.Physics.CollisionInfo;
import com.mygdx.game.Physics.PhysicsBodyType;

import static com.mygdx.utils.Constants.TILE_SIZE;

/**
 * Cannonball entity and the methods to get it flying.
 */
public class CannonBall extends Entity implements CollisionCallBack {
    private static float speed;
    private boolean toggleLife;
    private static final int MAX_AGE = 5;
    private Entity shooter; // Changed for Assessment 2, type switched from Ship to Entity

    /**
     * Constructs the cannonball object by generating components
     */
    public CannonBall() {
        super(3);
        setName("ball");
        toggleLife = false;
        Transform t = new Transform();
        t.setPosition(-100, 100);
        t.setScale(0.5f, 0.5f);
        Renderable r = new Renderable(4, "ball", RenderLayer.Transparent);
        RigidBody rb = new RigidBody(PhysicsBodyType.Dynamic, r, t, true);
        rb.setCallback(this);

        addComponents(t, r, rb);

        speed = GameManager.getSettings().get("starting").getFloat("cannonSpeed");
        r.hide();
    }

    /**
     * Called once per frame
     */
    @Override
    public void update() {
        super.update();
        removeOnCollision();
    }

    /**
     * Removes the cannonball offscreen once it hits a target.
     */
    private void removeOnCollision() {
        if (toggleLife) {
            getComponent(Renderable.class).hide();
            Transform t = getComponent(Transform.class);
            t.setPosition(10000, 10000);

            RigidBody rb = getComponent(RigidBody.class);
            rb.setPosition(t.getPosition());
            rb.setVelocity(0, 0);
            toggleLife = false;
        }
        /*else{
            age += EntityManager.getDeltaTime();
        }
        if(age > MAX_AGE) {
            age = 0;
            kill();
        }*/
    }

    /**
     * Teleport the cannonball in from offscreen and send in flying away from the ship.
     *
     * @param pos    2D vector location from where it sets off
     * @param dir    2D vector direction for its movement
     * @param sender ship entity firing it
     */
    public void fire(Entity sender, Vector2 pos, Vector2 dir) {
        Transform t = getComponent(Transform.class);
        t.setPosition(pos);

        RigidBody rb = getComponent(RigidBody.class);
        Vector2 ta = dir.cpy().scl(speed * EntityManager.getDeltaTime());
        Vector2 o = new Vector2(TILE_SIZE * t.getScale().x, TILE_SIZE * t.getScale().y);
        Vector2 v = ta.cpy().sub(o);

        rb.setVelocity(v);

        getComponent(Renderable.class).show();
        shooter = sender;
    }

    /**
     * Marks cannonball for removal on next update.
     */
    public void kill() {
        toggleLife = true;
    }

    /**
     * Added for Assessment 2
     * @return The Faction of the Pirate Component attached to this entity
     */
    public Faction getFaction() {
        return shooter.getComponent(Pirate.class).getFaction();
    }

    /**
     * Added for Assessment 2
     * @return the damage dealt by the unit shooting this cannonball
     */
    public Float getAttackDmg() {
        return shooter.getComponent(Pirate.class).getAttackDmg();
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
    public void EnterTrigger(CollisionInfo info) {

    }

    /**
     * `unused`
     */
    @Override
    public void ExitTrigger(CollisionInfo info) {

    }

    //    public Entity getShooter() {
    //        return shooter;
    //    }
}
