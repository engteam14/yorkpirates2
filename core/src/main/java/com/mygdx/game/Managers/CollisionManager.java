package com.mygdx.game.Managers;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Entitys.Entity;
import com.mygdx.game.Physics.CollisionCallBack;
import com.mygdx.game.Physics.CollisionInfo;

/**
 * Handles collision callbacks for box2d
 */
public class CollisionManager implements ContactListener {
    private static boolean initialized = false;

    public CollisionManager() {
        if (initialized) {
            throw new RuntimeException("Collision manager cant be instantiated more then once");
        }
        initialized = true;
    }

    /**
     * Amended for Assessment 2 (modularised code to remove repeating code)
     * Called for every contact that box2d detects prior to collision restitution (doesn't matter if it is a trigger/sensor)
     *
     * @param contact the contact data
     */
    @Override
    public void beginContact(Contact contact) {
        CollisionInfo info = establishCollision(contact);

        if (info.a != null) {
            if (info.fA.isSensor() && info.b != null && !info.fB.isSensor()) {
                ((CollisionCallBack) info.b).EnterTrigger(info);
            } else {
                ((CollisionCallBack) info.a).BeginContact(info);
            }
        }

        if (info.b != null) {
            if (info.fB.isSensor() && info.a != null && !info.fA.isSensor()) {
                ((CollisionCallBack) info.a).EnterTrigger(info);
            } else {
                ((CollisionCallBack) info.b).BeginContact(info);
            }
        }
    }

    /**
     * Amended for Assessment 2 (modularised code to remove repeating code)
     * Called for every contact that box2d detects after collision restitution (doesn't matter if it is a trigger/sensor)
     *
     * @param contact the contact data
     */
    @Override
    public void endContact(Contact contact) {
        CollisionInfo info = establishCollision(contact);

        if (info.a != null) {
            if (info.fA.isSensor() && info.b != null && !info.fB.isSensor()) {
                ((CollisionCallBack) info.b).ExitTrigger(info);
            } else {
                ((CollisionCallBack) info.a).EndContact(info);
            }
        }

        if (info.b != null) {
            if (info.fB.isSensor() && info.a != null && !info.fA.isSensor()) {
                ((CollisionCallBack) info.a).ExitTrigger(info);
            } else {
                ((CollisionCallBack) info.b).EndContact(info);
            }
        }
    }

    /**
     * Added for Assessment 2
     * Converts Contact into CollisionInfo
     * @param contact the contact data
     */
    private CollisionInfo establishCollision(Contact contact){
        // Generally calls the correct callback on the appropriate objects (not as intuitive as id like though)
        Fixture fa = contact.getFixtureA();
        Body ba = fa.getBody();
        Object oa = ba.getUserData();
        CollisionCallBack cbA = (CollisionCallBack) oa;

        Fixture fb = contact.getFixtureB();
        Body bb = fb.getBody();
        Object ob = bb.getUserData();
        CollisionCallBack cbB = (CollisionCallBack) ob;

        CollisionInfo info = new CollisionInfo();
        info.fA = fa;
        info.fB = fb;

        info.bA = ba;
        info.bB = bb;

        info.a = (Entity) cbA;
        info.b = (Entity) cbB;
        return info;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
