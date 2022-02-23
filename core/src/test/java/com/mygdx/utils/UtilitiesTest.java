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
        Vector2 origin = new Vector2(0,0);
        Vector2 horizontal = new Vector2(10,0);
        Vector2 other = new Vector2(7.5f,3.3f);

        boolean sameCheck = Utilities.checkProximity(origin, origin, 0f);
        boolean horizontalCheck = Utilities.checkProximity(origin, horizontal, 15);
        boolean boundsCheck = Utilities.checkProximity(origin, horizontal, 10);
        boolean outsideCheck = Utilities.checkProximity(origin, horizontal, 5);
        boolean angleCheck = Utilities.checkProximity(origin, other, 9);

        assertAll(() -> assertTrue(sameCheck, "Fails to confirm proximity for two identical vectors.")
                , () -> assertTrue(horizontalCheck, "Fails to confirm proximity on the horizontal axis.")
                , () -> assertTrue(boundsCheck, "Fails to confirm proximity when other vector lies on the boundary.")
                , () -> assertFalse(outsideCheck, "Fails to deny proximity when other vector lies outside the boundary.")
                , () -> assertTrue(angleCheck, "Fails to confirm proximity between two vectors."));
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
        Vector2 testEdges =     Utilities.round(new Vector2(2.9f, 2.1f));
        Vector2 testRounded =   Utilities.round(new Vector2(3, 2));
        Vector2 testHalf =      Utilities.round(new Vector2(2.5f, 1.5f));
        Vector2 expected =                      new Vector2(3, 2);

        assertAll(
                    () -> assertAll(
                            () -> assertEquals(expected.x, testEdges.x, "Fails on lower edge.")
                        ,   () -> assertEquals(expected.y, testEdges.y, "Fails on upper edge."))
                ,   () -> assertAll(
                            () -> assertEquals(expected.x, testRounded.x, "Fails on rounded value.")
                        ,   () -> assertEquals(expected.y, testRounded.y, "Fails on rounded value."))
                ,   () -> assertAll(
                            () -> assertEquals(expected.x, testHalf.x, "Fails on half value.")
                        ,   () -> assertEquals(expected.y, testHalf.y, "Fails on half value."))
        );
    }

    @Test
    void floor() {
        Vector2 testEdges =     Utilities.floor(new Vector2(3.1f, 2.9f));
        Vector2 testRounded =   Utilities.floor(new Vector2(3, 2));
        Vector2 testHalf =      Utilities.floor(new Vector2(3.5f, 2.5f));
        Vector2 expected =                      new Vector2(3, 2);

        assertAll(
                    () -> assertAll(
                                () -> assertEquals(expected.x, testEdges.x, "Fails on lower edge.")
                            ,   () -> assertEquals(expected.y, testEdges.y, "Fails on upper edge."))
                ,   () -> assertAll(
                                () -> assertEquals(expected.x, testRounded.x, "Fails on rounded value.")
                            ,   () -> assertEquals(expected.y, testRounded.y, "Fails on rounded value."))
                ,   () -> assertAll(
                                () -> assertEquals(expected.x, testHalf.x, "Fails on half value.")
                            ,   () -> assertEquals(expected.y, testHalf.y, "Fails on half value."))
        );
    }

    @Test
    void contains() {
        ArrayList<Integer> list = new ArrayList<>();
        boolean checkEmpty = Utilities.contains(list, 10);
        list.add(10);
        boolean checkOne = Utilities.contains(list, 10);
        list.add(20);
        list.add(0, 5);
        boolean checkMultiple = Utilities.contains(list, 10);
        boolean checkWrong = Utilities.contains(list, 30);

        assertAll(() -> assertFalse(checkEmpty, "Found value despite empty array.")
                , () -> assertTrue(checkOne, "Cannot find value in single sized array.")
                , () -> assertTrue(checkMultiple, "Cannot find value in multiple sized array.")
                , () -> assertFalse(checkWrong, "Found value when not in array."));
    }
}