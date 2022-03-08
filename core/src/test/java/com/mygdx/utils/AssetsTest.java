package com.mygdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AssetsTest {

    @Test
    void playerShip() {
        assertAll(() -> assertNotNull(Gdx.files.internal("core/assets/ship.png"), "Ship Sprite Missing"));
    }
}