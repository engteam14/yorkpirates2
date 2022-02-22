package com.mygdx.utils;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;
import sun.security.krb5.internal.rcache.AuthList;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UtilitiesTest {

    @Test
    void vectorToAngle() {
        Vector2 testVector = new Vector2(3,4);
        float testAngle = Utilities.vectorToAngle(testVector);

        float expectedOutcome = (float) Math.atan(-3f/4);
        float wrongAngleOutcome = (float) Math.atan(-4f/3);

        assertAll(() -> assertEquals(expectedOutcome, testAngle, "Incorrect angle calculation")
                , () -> assertNotEquals(wrongAngleOutcome, testAngle, 0.0, "Calculates wrong angle, but correctly")
                , () -> assertNotEquals((float)(Math.PI/2), testAngle, 0.0, "Calculates wrong angle, but correctly"));
    }

    @Test
    void angleToVector() {
        float testAngle = (float) Math.atan(-3f/4);
        Vector2 testVector = Utilities.angleToVector(new Vector2(),testAngle);

        Vector2 expectedOutcome = new Vector2(3f/5,4f/5);
        Vector2 wrongAxisOutcome = new Vector2(4f/5,3f/5);

        assertAll(() -> assertEquals(expectedOutcome, testVector, "Incorrect vector calculation")
                , () -> assertNotEquals(wrongAxisOutcome, testVector, "Calculates vector with flipped axis, but correctly"));
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