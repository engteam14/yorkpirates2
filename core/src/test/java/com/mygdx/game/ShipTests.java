package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Components.*;
import com.mygdx.game.Entitys.*;
import com.mygdx.game.Managers.*;
import com.mygdx.game.Quests.KillQuest;
import com.mygdx.game.Quests.LocateQuest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mygdx.game.AI.EnemyState.*;
import static com.mygdx.utils.Constants.INIT_CONSTANTS;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class ShipTests {

	@Before
	public void init(){
		INIT_CONSTANTS();
		PirateGame.loadResources();
		PhysicsManager.Initialize(false);
		GameManager.Initialize(GameDifficulty.Regular);
	}

	@After
	public void dispose(){
		EntityManager.cleanUp();
		ResourceManager.dispose();
		GameManager.dispose();

	}

	/**
	 * Test Identifier: 8.0
	 * Requirements Tested: UR_SHIP_CONTROL,
	 */
	@Test
	public void shipMove() {
		Ship ship = new Ship();
		RigidBody shipRb = (RigidBody) ship.getComponent(ComponentType.RigidBody);

		moveTest(shipRb, new Vector2(0,0));

		moveTest(shipRb, new Vector2(1, 0));
		moveTest(shipRb, new Vector2(-1,0));

		moveTest(shipRb, new Vector2(0,1));
		moveTest(shipRb, new Vector2(0,-1));

		moveTest(shipRb, new Vector2(1,1));
		moveTest(shipRb, new Vector2(1,-1));
		moveTest(shipRb, new Vector2(-1,1));
		moveTest(shipRb, new Vector2(-1,-1));
	}

	private void moveTest(RigidBody rb, Vector2 velocity){
		Vector2 startPos = rb.getPosition().cpy();

		// Move in desired direction
		for (int i = 0; i < 10; i++) {
			rb.setVelocity(velocity);
			PhysicsManager.update();
		}

		// Calculate differences
		Vector2 diff = rb.getPosition().cpy().sub(startPos);
		boolean correctMovement =
				(velocity.scl(diff).x > 0 || velocity.scl(diff).y > 0)
				|| (velocity.x == 0 && diff.x == 0)
				|| (velocity.y == 0 && diff.y == 0);

		// Test that velocity applied
		assertTrue(String.format("Ship moved from %s to %s when velocity of %s applied.", startPos, rb.getPosition(), velocity), correctMovement);
	}

	/**
	 * Test Identifier: 8.1
	 * Requirements Tested: UR_FIRE_WEAPONS, FR_PLAYER_FIRE
	 */
	@Test
	public void shipFires() {
		Ship ship = new Ship();
		CannonBall cannonBall = GameManager.getCurrentCannon();
		Vector2 shootDirection = new Vector2(1,1);

		Transform cannonT = (Transform) cannonBall.getComponent(ComponentType.Transform);
		Vector2 cannonStartPos = cannonT.getPosition().cpy();

		ship.shoot(shootDirection);
		Vector2 cannonNewPos = cannonT.getPosition().cpy();
		assertNotEquals("Cannonball has not been fired", cannonStartPos, cannonNewPos);
	}

	/**
	 * Test Identifier: 8.2
	 * Requirements Tested: UR_HOSTILE_SHIP_ENCOUNTER, FR_HOSTILE_AI
	 */
	@Test
	public void NPCShipsChangeStates() {
		GameManager.CreatePlayer();
		Player p = GameManager.getPlayer();
		NPCShip ship = GameManager.CreateNPCShip(1);
		GameManager.CreateCollege(1);

		Pirate pirate = ship.getComponent(Pirate.class);

		assertFalse("Pirate can attack despite no target", pirate.canAttack());
		pirate.addTarget(p);
		assertTrue("Ship not in attack range",pirate.canAttack());
		ship.update();
		assertSame("Ship not in attack mode", ATTACK, ship.getCurrentState());

		p.getComponent(Pirate.class).kill();
		ship.update();
		assertSame("Ship not in wander mode after target is killed", WANDER, ship.getCurrentState());

		GameManager.CreatePlayer();
		p = GameManager.getPlayer();
		Vector2 outOfRange = new Vector2(p.getPosition().x + Ship.getAttackRange()+1,p.getPosition().y + Ship.getAttackRange()+1);
		p.getComponent(Transform.class).setPosition(outOfRange);
		ship.update();
		assertSame("Ship not in pursue mode after gaining aggro", PURSUE, ship.getCurrentState());

		ship.getComponent(Transform.class).setPosition(outOfRange);
		ship.update();
		assertSame("Ship not in attack mode after entering range", ATTACK, ship.getCurrentState());

		Vector2 outOfRangeAgain = new Vector2(p.getPosition().x + Ship.getAttackRange()+1,p.getPosition().y + Ship.getAttackRange()+1);
		p.getComponent(Transform.class).setPosition(outOfRangeAgain);
		ship.update();
		assertSame("Ship not in pursue mode after target leaves range", PURSUE, ship.getCurrentState());

		ship.getComponent(Pirate.class).removeTarget();
		ship.update();
		assertSame("Ship not in wander mode after losing aggro", WANDER, ship.getCurrentState());
	}

	/**
	 * Test Identifier: 8.3
	 * Requirements Tested: UR_HOSTILE_SHIP_ENCOUNTER
	 */
	@Test
	public void killShip(){
		NPCShip ship = new NPCShip();
		Pirate p = ship.getComponent(Pirate.class);
		assertTrue("Ship has been spawned dead",ship.isAlive());

		p.kill();
		ship.update();
		assertFalse("Ship has not been correctly killed",ship.isAlive());
	}

	/**
	 * Test Identifier: 8.4
	 * Requirements Tested: UR_HOSTILE_SHIP_ENCOUNTER
	 */
	@Test
	public void NPCShipShoots(){
		GameManager.CreatePlayer();
		Player player = GameManager.getPlayer();
		NPCShip ship = new NPCShip();

		ship.getComponent(Transform.class).setPosition(0,0);
		player.getComponent(Transform.class).setPosition(1,1);

		CannonBall cannonBall = GameManager.getCurrentCannon();
		Transform cannonT = (Transform) cannonBall.getComponent(ComponentType.Transform);
		Vector2 cannonStartPos = cannonT.getPosition().cpy();

		long initialise = TimeUtils.millis();
		while (TimeUtils.timeSinceMillis(initialise)<1010){
			//Do nothing
		}
		ship.attackShip(player);

		cannonBall.update();
		Vector2 cannonNewPos = cannonT.getPosition().cpy();
		assertNotEquals("Cannonball hasn't moved", cannonNewPos, cannonStartPos);
	}
}
