package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Components.ComponentType;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.ResourceManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;


import static com.mygdx.utils.Constants.INIT_CONSTANTS;

@RunWith(GdxTestRunner.class)
public class ShipTests {

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
	public void shipMove() {
		Player player = new Player();
		RigidBody playerRb = (RigidBody) player.getComponent(ComponentType.RigidBody);
		Vector2 startPos = playerRb.getPosition().cpy();

		for (int i = 0; i < 500; i++) {
			playerRb.setVelocity(new Vector2(1f,1f));
			PhysicsManager.update();
		}
		Vector2 endPos = playerRb.getPosition().cpy();

		assertNotEquals("Ship did not move (x axis)", endPos.x, startPos.x);
		assertNotEquals("Ship did not move (y axis)", endPos.y, startPos.y);
	}

	@Test
	public void shipFires() {

	}

	@Test
	public void gainMoney() {

	}

	@Test
	public void gainPoints() {

	}

	@Test
	public void progressTasks() {

	}
}
