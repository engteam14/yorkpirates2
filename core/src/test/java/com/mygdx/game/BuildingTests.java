package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.Components.ComponentType;
import com.mygdx.game.Components.Renderable;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Entitys.CannonBall;
import com.mygdx.game.Entitys.College;
import com.mygdx.game.Entitys.Ship;
import com.mygdx.game.Managers.GameDifficulty;
import com.mygdx.game.Managers.GameManager;
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
		int powerups_id = ResourceManager.addTextureAtlas("powerups.txt");
		ResourceManager.addTexture("menuBG.jpg");
		ResourceManager.addTexture("Chest.png");
		ResourceManager.loadAssets();

		INIT_CONSTANTS();
		PhysicsManager.Initialize(false);
		GameManager.Initialize(GameDifficulty.Regular);
	}

	@After
	public void dispose(){
		ResourceManager.dispose();
		GameManager.dispose();
	}

	@Test
	public void buildingExists() {
		College college = new College(1);
		assertTrue("College is spawned dead",college.isAlive());
	}

	@Test
	public void buildingFires() {
		JsonValue starting = GameManager.getSettings().get("starting");
		int ammo = starting.getInt("ammo");
		assertTrue("No ammunition present",ammo>0);

		College college = new College();
		CannonBall cannonBall = GameManager.getCurrentCannon();
		Vector2 shootDirection = new Vector2(1,1);

		Transform collegeT = (Transform) college.getComponent(ComponentType.Transform);
		Transform cannonT = (Transform) cannonBall.getComponent(ComponentType.Transform);
		Renderable cannonR = (Renderable) cannonBall.getComponent(ComponentType.Renderable);

		Vector2 shipPos = collegeT.getPosition().cpy();
		Vector2 cannonStartPos = cannonT.getPosition().cpy();

		assertNotEquals("Ship and Cannonball at same location before firing", shipPos, cannonStartPos);
		assertFalse("Cannonball begins visible",cannonR.isVisible());

		college.shoot(collegeT.getPosition(),shootDirection);
		Vector2 cannonNewPos = cannonT.getPosition().cpy();

		assertNotEquals("Cannonball position has not updated", cannonStartPos, cannonNewPos);
		assertEquals("Cannonball moved to incorrect location",shipPos,cannonNewPos);
		assertTrue("Cannonball remains invisible",cannonR.isVisible());
	}

	@Test
	public void buildingCapturable() {

	}
}
