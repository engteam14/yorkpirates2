package com.mygdx.game.Entitys;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.Renderable;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Faction;
import com.mygdx.game.Managers.RenderLayer;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.Physics.CollisionCallBack;
import com.mygdx.game.Physics.CollisionInfo;
import com.mygdx.game.Physics.PhysicsBodyType;

import static com.mygdx.utils.Constants.BUILDING_SCALE;

/**
 * Buildings that you see in game.
 * Changed for Assessment 2 (made class public)
 */
public class Building extends Entity implements CollisionCallBack {
    private String buildingName;
    private static int atlas_id;
    private boolean isFlag;
    private final College college; //Added for Assessment 2

    /**
     * Flags are indestructible and mark college locations.
     * Amended for Assessment 2: Added College parameter to keep track of parent
     * @param college added to signify ownership of building
     */
    public Building(College college) {
        super();
        isFlag = false;
        Transform t = new Transform();
        t.setScale(BUILDING_SCALE, BUILDING_SCALE);
        Pirate p = new Pirate();
        atlas_id = ResourceManager.getId("Buildings.txt");
        Renderable r = new Renderable(atlas_id, "big", RenderLayer.Transparent);
        addComponents(t, p, r);
        this.college = college;
    }

    /**
     * Flags are indestructible and mark college locations.
     * Amended for Assessment 2: Added College parameter to keep track of parent
     * @param isFlag set to true to create a flag
     */
    Building(College college, boolean isFlag) {
        this(college);
        this.isFlag = isFlag;
    }

    /**
     * Creates a building with the given name at the specified location.
     *
     * @param pos  2D position vector
     * @param name name of building
     */
    public void create(Vector2 pos, String name) {
        Sprite s = ResourceManager.getSprite(atlas_id, name);
        Renderable r = getComponent(Renderable.class);
        r.setTexture(s);
        getComponent(Transform.class).setPosition(pos);
        buildingName = name;

        RigidBody rb = new RigidBody(PhysicsBodyType.Static, r, getComponent(Transform.class));
        rb.setCallback(this);
        addComponent(rb);
    }

    /**
     * Replace the building with ruins and mark as broken.
     * Changed for Assessment 2
     *  - Made public for testing purposes
     *  - Sets destroyer as parent college's most recent attacker.
     */
    public void destroy(Faction conqueror) {
        if (isFlag) {
            return;
        }
        Sprite s = ResourceManager.getSprite(atlas_id, buildingName + "-broken");
        Renderable r = getComponent(Renderable.class);
        r.setTexture(s);
        getComponent(Pirate.class).kill();
        college.setMostRecentAttacker(conqueror);
    }

    /**
     * @return the boolean value of the alive status of the Pirate Component
     */
    public boolean isAlive() {
        return getComponent(Pirate.class).isAlive();
    }

    /**
     * Destroys the building and marks cannonball for removal.
     * Amended for Assessment 2, added Faction checks and now ignores flags
     * @param info CollisionInfo container
     */
    @Override
    public void EnterTrigger(CollisionInfo info) {
        if (info.a instanceof CannonBall && isAlive() && !isFlag) {
            CannonBall a = (CannonBall) info.a;
            if(a.getFaction() != college.getFaction()){
                destroy(a.getFaction());
            }
            a.kill();
        }
    }

    /**
     * Unused
     */
    @Override
    public void BeginContact(CollisionInfo info) {}

    /**
     * Unused
     */
    @Override
    public void EndContact(CollisionInfo info) {}

    /**
     * Unused
     */
    @Override
    public void ExitTrigger(CollisionInfo info) {

    }
}
