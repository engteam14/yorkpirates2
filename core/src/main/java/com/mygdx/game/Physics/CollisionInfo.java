package com.mygdx.game.Physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.Entitys.Entity;

/**
 * Contains collision info consisting of entities, box2d bodies, and fixtures involved.
 */
public class CollisionInfo {
    public Fixture fA, fB;
    public Body bA, bB;
    public Entity a, b;

    public CollisionInfo(Fixture f1, Fixture f2, Body b1, Body b2, Entity e1, Entity e2) {
        fA = f1;
        fB = f2;

        bA = b1;
        bB = b2;

        a = e1;
        b = e2;
    }
}
