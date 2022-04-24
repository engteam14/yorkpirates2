package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.Components.*;
import com.mygdx.game.Entitys.*;
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
public class ProjectileTests {

	@Before
	public void init(){
		INIT_CONSTANTS();
		PirateGame.loadResources();
		PhysicsManager.Initialize(false);
		GameManager.Initialize(GameDifficulty.Regular);
	}

	@After
	public void dispose(){
		ResourceManager.dispose();
	}

	@Test
	public void getComponent() {
		Player player = new Player();
		assertNull(player.getComponent(AINavigation.class));

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

	@Test
	public void projectileHitsShips() {
		Ship allyShipOne = new Ship();
		Ship allyShipTwo = new Ship();
		Ship enemyShip = new Ship();

		allyShipOne.setFaction(1);
		allyShipTwo.setFaction(1);
		enemyShip.setFaction(2);

		//Fire cannonball from ally to ally
		CannonBall cannonBallA = GameManager.getCurrentCannon();

		allyShipOne.shoot(new Vector2(-10,-10));
		int healthPrior = allyShipTwo.getHealth();
		allyShipTwo.EnterTrigger(new CollisionInfo(null,null,null,null,cannonBallA,null));
		int healthPost = allyShipTwo.getHealth();

		assertSame("Ally ship damages allies",healthPrior,healthPost);

		//Fire cannonball from enemy to ally
		CannonBall cannonBallB = GameManager.getCurrentCannon();

		enemyShip.shoot(new Vector2(10,10));
		allyShipTwo.EnterTrigger(new CollisionInfo(null,null,null,null,cannonBallB,null));
		int healthPostPost = allyShipTwo.getHealth();

		assertNotSame("Enemy ship fails to damage enemies",healthPost,healthPostPost);
	}

	@Test
	public void removeOnCollision(){
		Ship ship = new Ship();

		CannonBall cannon = GameManager.getCurrentCannon();
		Vector2 homePosition = cannon.getComponent(Transform.class).getPosition().cpy();
		ship.shoot(new Vector2(1, 1));
		Vector2 shotPosition = cannon.getComponent(Transform.class).getPosition().cpy();
		cannon.kill();
		cannon.update();
		Vector2 deadPosition = cannon.getComponent(Transform.class).getPosition().cpy();
		assertNotEquals(homePosition, shotPosition);
		assertEquals("hasnt gone off screen", new Vector2(10000, 10000), deadPosition);
		System.out.println(homePosition);
		System.out.println(shotPosition);
		System.out.println(deadPosition);

	}
}
