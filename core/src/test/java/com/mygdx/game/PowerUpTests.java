package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
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
		ResourceManager.addTexture("ship.png");
		ResourceManager.addTileMap("Map.tmx");
		ResourceManager.addTextureAtlas("Boats.txt");
		ResourceManager.addTextureAtlas("UISkin/skin.atlas");
		ResourceManager.addTextureAtlas("Buildings.txt");
		ResourceManager.addTextureAtlas("powerups.txt");
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
		PowerUp pow = new PowerUp(key, oper, value);

		// Test applying powerups
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

		// Test removing powerup
		player.resetToDefault(key);
		assertEquals("PowerUp does not reset to correct value", player.getValue(key), starter, 0f);
	}

	@Test
	public void powerUpPickup() {
		// Init the player
		Player player = new Player();
		RigidBody playerRb = player.getComponent(RigidBody.class);
		float startHealth = player.getValue("health");

		// Init the powerup pickup
		PowerUp pow = new PowerUp("health", PowerUpOperation.increment, 10f);
		PowerUpPickup powPickup = new PowerUpPickup(pow, "health-up", new Vector2(100,100).add(player.getPosition()), 1000);

		// Make sure the PowerUp hasn't already applied
		assertEquals("PowerUp applied without player touching it", player.getValue("health"), startHealth, 0f);

		// Move the player to the PowerUp
		playerRb.setPosition(powPickup.getComponent(RigidBody.class).getPosition());
		PhysicsManager.update();
		assertNotEquals("PowerUp did not apply on collision", player.getValue("health"), startHealth);
	}

}
