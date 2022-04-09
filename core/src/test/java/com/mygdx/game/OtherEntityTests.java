package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.Components.ComponentType;
import com.mygdx.game.Components.Renderable;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Entitys.Building;
import com.mygdx.game.Entitys.CannonBall;
import com.mygdx.game.Entitys.College;
import com.mygdx.game.Entitys.Ship;
import com.mygdx.game.Managers.GameDifficulty;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.Physics.CollisionInfo;
import com.mygdx.utils.Utilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static com.mygdx.utils.Constants.INIT_CONSTANTS;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class OtherEntityTests {

	@Before
	public void init(){
		int id_ship = ResourceManager.addTexture("ship.png");
		int id_map = ResourceManager.addTileMap("Map.tmx");
		int atlas_id = ResourceManager.addTextureAtlas("Boats.txt");
		int extras_id = ResourceManager.addTextureAtlas("UISkin/skin.atlas");
		int buildings_id = ResourceManager.addTextureAtlas("Buildings.txt");
		int obstacles_id = ResourceManager.addTextureAtlas("obstacles.txt");
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
	}

	@Test
	public void projectileAmmoExists() {
		JsonValue starting = GameManager.getSettings().get("starting");
		int ammo = starting.getInt("ammo");
		assertTrue("No ammunition present",ammo>0);
	}

	@Test
	public void projectileFires() {
		Ship ship = new Ship();
		CannonBall cannonBall = GameManager.getCurrentCannon();
		Vector2 shootDirection = new Vector2(1,1);

		Transform shipT = (Transform) ship.getComponent(ComponentType.Transform);
		Transform cannonT = (Transform) cannonBall.getComponent(ComponentType.Transform);
		Renderable cannonR = (Renderable) cannonBall.getComponent(ComponentType.Renderable);

		Vector2 shipPos = shipT.getPosition().cpy();
		Vector2 cannonStartPos = cannonT.getPosition().cpy();

		assertNotEquals("Ship and Cannonball at same location before firing", shipPos, cannonStartPos);
		assertFalse("Cannonball begins visible",cannonR.isVisible());

		ship.shoot(shootDirection);
		Vector2 cannonNewPos = cannonT.getPosition().cpy();

		assertNotEquals("Cannonball position has not updated", cannonStartPos, cannonNewPos);
		assertEquals("Cannonball moved to incorrect location",shipPos,cannonNewPos);
		assertTrue("Cannonball remains invisible",cannonR.isVisible());
	}

	@Test
	public void projectileHitsBuildings() {
		College allyCollegeOne = new College(1);
		College allyCollegeTwo = new College(1);
		College enemyCollege = new College(2);

		Building building = new Building(allyCollegeTwo);

		Vector2 allyPositionOne = new Vector2(10,10);
		Vector2 allyPositionTwo = new Vector2(0,0);
		Vector2 enemyPosition = new Vector2(-10,-10);

		building.create(allyPositionTwo, "big");

		//Fire cannonball from ally to ally
		CannonBall cannonBallA = GameManager.getCurrentCannon();

		allyCollegeOne.shoot(allyPositionOne,enemyPosition);
		building.EnterTrigger(new CollisionInfo(null,null,null,null,cannonBallA,null));

		assertTrue("Ally college damages allies",building.isAlive());

		//Fire cannonball from enemy to ally
		CannonBall cannonBallB = GameManager.getCurrentCannon();

		enemyCollege.shoot(enemyPosition,allyPositionOne);
		building.EnterTrigger(new CollisionInfo(null,null,null,null,cannonBallB,null));

		assertFalse("Enemy college fails to damage enemies",building.isAlive());
	}
}
