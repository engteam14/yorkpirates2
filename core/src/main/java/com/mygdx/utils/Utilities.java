package com.mygdx.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

import static com.mygdx.utils.Constants.TILE_SIZE;

/**
 * Helper functions
 */
public final class Utilities {
    public static float vectorToAngle(Vector2 v) {
        return (float) Math.atan2(-v.x, v.y);
    }

    public static Vector2 angleToVector(Vector2 out, float angle) {
        out.x = -(float) Math.sin(angle);
        out.y = (float) Math.cos(angle);
        return out;
    }

    public static float tilesToDistance(float tiles) {
        return TILE_SIZE * tiles;
    }

    public static Vector2 tilesToDistance(Vector2 tiles) {
        return tiles.cpy().scl(TILE_SIZE);
    }

    public static int distanceToTiles(float dist) {
        return (int) (dist / TILE_SIZE);
    }

    public static Vector2 distanceToTiles(Vector2 dist) {
        return dist.cpy().scl(1.0f / TILE_SIZE);
    }

    /**
     * checks the proximity of point a to point b
     *
     * @param a      first point
     * @param b      second point
     * @param radius min dist to be considered close
     * @return |dist(a, b)| < radius
     */
    public static boolean checkProximity(Vector2 a, Vector2 b, float radius) {
        final float d2 = radius * radius;
        final float d = Math.abs(a.dst2(b));
        return d <= d2;
    }

    public static double angleBetween(Vector2 a, Vector2 b) {
        if(a.equals(new Vector2(0,0)) || b.equals(new Vector2(0,0))){
            return 0;
        }
        float dot = Vector2.dot(a.x,a.y,b.x,b.y);
        double absA = Math.sqrt( Math.pow(a.x,2) + Math.pow(a.y,2) );
        double absB = Math.sqrt( Math.pow(b.x,2) + Math.pow(b.y,2) );

        return Math.acos(dot/(absA*absB));
    }

    public static float scale(float min0, float max0, float min1, float max1) {
        return (max1 - min1) * ((1 - min0) / (max0 - min0)) + min1;
    }

    public static float scale(Vector2 a, Vector2 b) {
        return (b.y - b.x) * ((1 - a.x) / (a.y - a.x)) + b.x;
    }

    /**
     * @param x the vector to round
     * @return x modified for chaining
     */
    public static Vector2 round(Vector2 x) {
        x.x = Math.round(x.x);
        x.y = Math.round(x.y);
        return x;
    }

    /**
     * Random Vec2 in range
     *
     * @param min inclusive
     * @param max exclusive
     * @return rand Vector2
     */
    public static Vector2 randomPos(float min, float max) {
        Random r = new Random();
        return new Vector2(min + r.nextFloat() * (max - min), min + r.nextFloat() * (max - min));
    }

    /**
     * Chooses a random element
     *
     * @param list   source
     * @param <T>    type of element to return
     * @return the random element
     */
    public static <T> T randomChoice(ArrayList<T> list) {
        int choice = new Random().nextInt(list.size());
        return list.get(choice);
    }

    /**
     * floors the vector
     *
     * @param a given vector
     * @return new vector floored
     */
    public static Vector2 floor(final Vector2 a) {
        return new Vector2(MathUtils.floor(a.x), MathUtils.floor(a.y));
    }

    /**
     * helper for System.out.print
     *
     * @param v   string
     * @param eol eol msg
     */
    public static void print(String v, String eol) {
        System.out.print(v + eol);
    }


    /**
     * helper for System.out.println
     *
     * @param v string
     */
    public static void print(String v) {
        System.out.println(v);
    }

    /**
     * does array contain a
     *
     * @param array source
     * @param a     desired
     * @param <T>   type of element looking for
     * @return true if contained otherwise false
     */
    public static <T> boolean contains(ArrayList<T> array, T a) {
        for (T b : array) {
            if (b == a) {
                return true;
            }
        }
        return false;
    }
}
