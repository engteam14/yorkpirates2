package com.mygdx.game;

import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.ResourceManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mygdx.utils.Constants.INIT_CONSTANTS;
import static org.junit.Assert.assertNotEquals;

@RunWith(GdxTestRunner.class)
public class GameStateTests {

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

	@Test
	public void gameStart() {

	}

	@Test
	public void winGame() {

	}

	@Test
	public void loseGame() {

	}
}
