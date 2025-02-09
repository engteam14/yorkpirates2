package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Components.*;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Entitys.PowerUpPickup;
import com.mygdx.game.Managers.GameDifficulty;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.PowerUps.PowerUp;
import com.mygdx.game.PowerUps.PowerUpOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mygdx.utils.Constants.INIT_CONSTANTS;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class PowerUpTests {

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
		GameManager.dispose();
	}

	/**
	 * Test Identifier: 5.0
	 * Requirements Tested: UR_POWER_UP, FR_POWER_UP
	 */
	@Test
	public void powerUpApply() {
		Player player = new Player();
		PowerUpAssigned playerPow = player.getComponent(PowerUpAssigned.class);

		// Try for all keys
		powerUpTest(player, playerPow, "health", PowerUpOperation.increment, 10);
		powerUpTest(player, playerPow, "ammo", PowerUpOperation.increment, 10);
		powerUpTest(player, playerPow, "defense", PowerUpOperation.increment, 10);
		powerUpTest(player, playerPow, "damage", PowerUpOperation.increment, 10);
		powerUpTest(player, playerPow, "plunderRate", PowerUpOperation.increment, 10);

		// Try for all operation types, covering multiple value cases
		powerUpTest(player, playerPow, "health", PowerUpOperation.replace, 10);

		// Increment
		powerUpTest(player, playerPow, "health", PowerUpOperation.increment, 10);
		powerUpTest(player, playerPow, "health", PowerUpOperation.increment, -10);
		powerUpTest(player, playerPow, "health", PowerUpOperation.increment, 0);
		powerUpTest(player, playerPow, "health", PowerUpOperation.increment, 0.1f);

		// Decrement
		powerUpTest(player, playerPow, "health", PowerUpOperation.decrement, 10);
		powerUpTest(player, playerPow, "health", PowerUpOperation.decrement, -10);
		powerUpTest(player, playerPow, "health", PowerUpOperation.decrement, 0);
		powerUpTest(player, playerPow, "health", PowerUpOperation.decrement, 0.1f);

		// Multiply
		powerUpTest(player, playerPow, "health", PowerUpOperation.multiply, 10);
		powerUpTest(player, playerPow, "health", PowerUpOperation.multiply, -10);
		powerUpTest(player, playerPow, "health", PowerUpOperation.multiply, 0);
		powerUpTest(player, playerPow, "health", PowerUpOperation.multiply, 0.1f);

		// Divide
		powerUpTest(player, playerPow, "health", PowerUpOperation.divide, 10);
		powerUpTest(player, playerPow, "health", PowerUpOperation.divide, -10);
		powerUpTest(player, playerPow, "health", PowerUpOperation.divide, 0);
		powerUpTest(player, playerPow, "health", PowerUpOperation.divide, 0.1f);
	}

	private void powerUpTest(Player player, PowerUpAssigned playerPow, String key, PowerUpOperation oper, float value) {
		// Initialise values and test that key exists
		float starter = 0;
		try {
			starter = player.getValue(key);
		} catch(Exception e) {
			assert false : String.format("Key %s for PowerUp does not exist", key);
		}
		PowerUp pow = new PowerUp(key, oper, value, 0,-1);

		// Test applying power-ups
		playerPow.AssignPowerUp(pow);
		switch(oper){
			case replace:
				assertEquals("PowerUp value was not set on replace operation", player.getValue(key), value, 0f);
				break;
			case increment:
				assertEquals("PowerUp value was not set on increment operation", player.getValue(key), starter + value, 0f);
				break;
			case decrement:
				assertEquals("PowerUp value was not set on decrement operation", player.getValue(key), starter - value, 0f);
				break;
			case multiply:
				assertEquals("PowerUp value was not set on multiply operation", player.getValue(key), starter * value, 0f);
				break;
			case divide:
				assertEquals("PowerUp value was not set on divide operation", player.getValue(key), starter / value, 0f);
				break;
			default:
				assert false : String.format("PowerUp operation %s is invalid", oper);
				break;
		}

		// Test removing power-up
		player.resetToDefault(key);
		assertEquals("PowerUp does not reset to correct value", player.getValue(key), starter, 0f);
	}

	/**
	 * Test Identifier: 5.1
	 * Requirements Tested: UR_POWER_UP, FR_POWER_UP
	 */
	@Test
	public void powerUpPickup() {
		// Init the player
		Player player = new Player();
		RigidBody playerRb = player.getComponent(RigidBody.class);
		float startHealth = player.getValue("health");

		// Init the power-up pickup
		PowerUp pow = new PowerUp("health", PowerUpOperation.increment, 10f, 0, -1);
		PowerUpPickup powPickup = new PowerUpPickup(pow, "health-up", new Vector2(100,100).add(player.getPosition()), 1000);

		// Make sure the PowerUp hasn't already applied
		assertEquals("PowerUp applied without player touching it", player.getValue("health"), startHealth, 0f);

		// Move the player to the PowerUp
		playerRb.setPosition(powPickup.getComponent(RigidBody.class).getPosition());
		PhysicsManager.update();
		assertNotEquals("PowerUp did not apply on collision", player.getValue("health"), startHealth);
	}

	/**
	 * Test Identifier: 5.2
	 * Requirements Tested: UR_POWER_UP, UR_SPEND_PLUNDER, FR_PLUNDER_SPEND, FR_POWER_UP
	 */
	@Test
	public void buyPowerUp(){
		GameManager.CreatePlayer();
		Player player = GameManager.getPlayer();
		int health1 = player.getHealth();
		PowerUp pow = new PowerUp("health", PowerUpOperation.increment, 10f, 0,-1);

		pow.buyPowerUp();
		int health2 = player.getHealth();
		assertTrue("Power up was not applied",(health1<health2));
	}

	/**
	 * Test Identifier: 5.3
	 * Requirements Tested: UR_POWER_UP, FR_POWER_UP
	 */
	@Test
	public void powerUpDuration(){
		GameManager.CreatePlayer();
		Player player = GameManager.getPlayer();

		PowerUp pow = new PowerUp("damage", PowerUpOperation.replace, 50f, 0,1);
		player.getComponent(PowerUpAssigned.class).AssignPowerUp(pow);

		player.getComponent(PowerUpAssigned.class).update();
		assertFalse("PowerUp is done too early", pow.CheckPowerUpDone());
		assertEquals("PowerUp did not apply", 50.0, player.getComponent(Pirate.class).getAttackDmg(), 0.0);

		long startTime = TimeUtils.millis();
		while ((TimeUtils.timeSinceMillis(startTime) < 1100)){
			TimeUtils.timeSinceMillis(startTime);
			pow.CheckPowerUpDone();
			player.getComponent(PowerUpAssigned.class).update();}
		assertTrue("PowerUp is not done", pow.CheckPowerUpDone());
		assertEquals("PowerUp did not undo effects", 10.0, player.getComponent(Pirate.class).getAttackDmg(), 0.0);

		player.getComponent(PowerUpAssigned.class).update();
	}
}
