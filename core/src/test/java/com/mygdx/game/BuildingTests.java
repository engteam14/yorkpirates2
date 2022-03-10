package com.mygdx.game;

import com.mygdx.game.Entitys.College;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.ResourceManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mygdx.utils.Constants.INIT_CONSTANTS;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class BuildingTests {

	@Before
	public void init(){
		int id_ship = ResourceManager.addTexture("ship.png");
		int id_map = ResourceManager.addTileMap("Map.tmx");
		int atlas_id = ResourceManager.addTextureAtlas("Boats.txt");
		int extras_id = ResourceManager.addTextureAtlas("UISkin/skin.atlas");
		int buildings_id = ResourceManager.addTextureAtlas("Buildings.txt");
		ResourceManager.addTexture("menuBG.jpg");
		ResourceManager.addTexture("Chest.png");
		ResourceManager.loadAssets();

		INIT_CONSTANTS();
		PhysicsManager.Initialize(false);
	}

	@After
	public void dispose(){
		ResourceManager.dispose();
	}

	@Test
	public void buildingExists() {
		College college = new College(1);
		assertTrue("College is spawned dead",college.isAlive());
	}

	@Test
	public void buildingFires() {

	}

	@Test
	public void buildingCapturable() {

	}
}
