package com.mygdx.utils;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;
import sun.security.krb5.internal.rcache.AuthList;

import static org.junit.jupiter.api.Assertions.*;

class UtilitiesTest {

    @Test
    void vectorToAngle() {
    }

    @Test
    void angleToVector() {
    }

    @Test
    void tilesToDistance() {
    }

    @Test
    void testTilesToDistance() {
    }

    @Test
    void distanceToTiles() {
    }

    @Test
    void testDistanceToTiles() {
    }

    @Test
    void checkProximity() {
    }

    @Test
    void angleBetween() {
    }

    @Test
    void scale() {
    }

    @Test
    void testScale() {
    }

    @Test
    void round() {
    }

    @Test
    void randomPos() {
    }

    @Test
    void randomChoice() {
    }

    @Test
    void floor() {
        Vector2 notFloored = new Vector2(3.1f, 2.9f);
        Vector2 v = Utilities.floor(notFloored);
        Vector2 floored = new Vector2(3,2);
        assertTrue(v.x == floored.x && v.y == floored.y );
        // already floored

    }

    @Test
    void print() {
    }

    @Test
    void testPrint() {
    }

    @Test
    void contains() {
    }
}