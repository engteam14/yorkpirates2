package com.mygdx.utils;

import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssetTest {
	@Test
	void arialFontAsset() {
		assertTrue(new File("assets/arial.ttf").isFile(), "Arial.ttf font Missing");
	}

	@Test
	void beachTilesetPng() {
		assertTrue(new File("assets/Beach Tileset.png").isFile(), "Beach Tileset.png Missing");
	}

	@Test
	void beachTilesetAssetTsx() {
		assertTrue(new File("assets/Beach Tileset.tsx").isFile(), "Beach Tileset.tsx Missing");
	}
	@Test
	void boatsAssetPNG() {
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
	void obstaclesPngAsset() {
		assertTrue(new File("assets/obstacles.png").isFile(), "obstacles.png Missing");
	}

	@Test
	void obstaclesTxtAsset() {
		assertTrue(new File("assets/obstacles.txt").isFile(), "obstacles.txt Missing");
	}

	@Test
	void otherPngAsset() {
		assertTrue(new File("assets/other.png").isFile(), "other.png Missing");
	}

	@Test
	void powerupsPngAsset() {
		assertTrue(new File("assets/powerups.png").isFile(), "powerups.png Missing");
	}

	@Test
	void powerupsTxtAsset() {
		assertTrue(new File("assets/powerups.txt").isFile(), "powerups.txt Missing");
	}

	@Test
	void questArrowPngAsset() {
		assertTrue(new File("assets/questArrow.png").isFile(), "questArrow.png Missing");
	}

	@Test
	void playerShipAsset() {
		assertTrue(new File("assets/ship.png").isFile(), "Ship.png Sprite Missing");
	}

	@Test
	void skinDefaultFnt() {
		assertTrue(new File("assets/UISkin/default.fnt").isFile(), "UISkin/default.fnt Missing");
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
	void skinPngAsset() {
		assertTrue(new File("assets/UISkin/skin.png").isFile(), "UISkin/skin.png Missing");
	}
}