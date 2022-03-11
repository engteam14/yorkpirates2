package com.mygdx.utils;

import com.badlogic.gdx.Gdx;

import com.mygdx.game.GdxTestRunner;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@RunWith(GdxTestRunner.class)

public class AssetsTest {
    @Test
    void ArialFontAsset() {
        assertTrue(new File("assets/arial.ttf").isFile(), "Arial.ttf font Missing");
    }

    @Test
    void BeachTilesetPng() {
        assertTrue(new File("assets/Beach Tileset.png").isFile(), "Beach Tileset.png Missing");
    }

    @Test
    void BeachTilesetAssetTsx() {
        assertTrue(new File("assets/Beach Tileset.tsx").isFile(), "Beach Tileset.tsx Missing");
    }
    @Test
    void BoatsAssetPNG() {
        assertTrue(new File("assets/boats.png").isFile(), "boats.png Sprite Missing");
    }
    @Test
    void BoatsAssetTxt() {
        assertTrue(new File("assets/Boats.txt").isFile(), "boats.txt file Missing");
    }

    @Test
    void BuildingsAssetTxt() {
        assertTrue(new File("assets/Buildings.txt").isFile(), "buildings.txt Missing");
    }
    @Test
    void ChestAssetPng(){
        assertTrue(new File("assets/Chest.png").isFile(), "Chest Sprite Missing");
    }

    @Test
    void GSettingsAssetJson() {
        assertTrue(new File("assets/GameSettings.json").isFile(), "GameSettings.json Missing");
    }
    @Test
    void mapTmxAsset() {
        assertTrue(new File("assets/Map.tmx").isFile(), "Map.tmx Missing");
    }
    @Test
    void menuBGAsset() {
        assertTrue(new File("assets/menuBG.jpg").isFile(), "MenuBG.jpg Missing");
    }
    @Test
    void OtherPngAsset() {
        assertTrue(new File("assets/other.png").isFile(), "other.png Missing");
    }
    @Test
    void playerShipAsset() {
        assertTrue(new File("assets/ship.png").isFile(), "Ship.png Sprite Missing");
    }
    @Test
    void SkinDefaultFnt() {
        assertTrue(new File("assets/UISkin/default.fnt").isFile(), "UISkin/default.fnt Missing");
    }
    @Test
    void SkinDefaultPng() {
        assertTrue(new File("assets/UISkin/default.png").isFile(), "UISkin/default.png Missing");
    }
    @Test
    void skinAtlasAsset() {
        assertTrue(new File("assets/UISkin/skin.atlas").isFile(), "UISkin/skin.atlas Missing");
    }
    @Test
    void skinJsonAsset() {
        assertTrue(new File("assets/UISkin/skin.json").isFile(), "UISkin/skin.json Missing");
    }
    @Test
    void UiSkinJsonAsset() {
        assertTrue(new File("assets/UISkin/uiskin.png").isFile(), "UISkin/uiskin.png Missing");
    }
}