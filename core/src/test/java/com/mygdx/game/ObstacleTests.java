package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Components.PowerUpAssigned;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Entitys.Obstacle;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Entitys.PowerUpPickup;
import com.mygdx.game.Entitys.Weather;
import com.mygdx.game.Managers.GameDifficulty;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.PowerUps.PowerUp;
import com.mygdx.game.PowerUps.PowerUpOperation;
import com.mygdx.utils.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mygdx.utils.Constants.INIT_CONSTANTS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(GdxTestRunner.class)
public class ObstacleTests {

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
	 * Test Identifier: 3.0
	 * Requirements Tested: UR_OBSTACLE_ENCOUNTER
	 */
	@Test
	public void obstacleDamage() {
		// Get the player
		Player player = new Player();
		RigidBody playerRb = player.getComponent(RigidBody.class);
		float playerHealth = player.getHealth();
		assertEquals("Player health lost early", 100, playerHealth, 0);

		// Testing on both trigger types
		Obstacle contactObs = new Obstacle("barrel", false, 10);
		Obstacle triggerObs = new Obstacle("barrel", true, 10);

		// Set obstacle positions
		contactObs.getComponent(RigidBody.class).setPosition(player.getPosition().add(new Vector2(0, 200)));
		triggerObs.getComponent(RigidBody.class).setPosition(player.getPosition().add(new Vector2(0, -200)));

		// Test contact obstacle
		playerRb.setPosition(contactObs.getComponent(RigidBody.class).getPosition());
		PhysicsManager.update();
		contactObs.update();
		assertNotEquals("Player health not lost after collision with contact obstacle", playerHealth, player.getHealth(), 0);
		playerHealth = player.getHealth();

		// Test trigger obstacle
		playerRb.setPosition(triggerObs.getComponent(RigidBody.class).getPosition());
		PhysicsManager.update();
		triggerObs.update();
		PhysicsManager.update(); // Needs an extra physics tick due to trigger
		triggerObs.update();
		assertNotEquals("Player health not lost after collision with trigger obstacle", playerHealth, player.getHealth(), 0);

		// Reset player position
		playerRb.setPosition(new Vector2(0,0));
	}

	/**
	 * Test Identifier: 3.0
	 * Requirements Tested: UR_WEATHER_ENCOUNTER
	 */
	@Test
	public void weatherDamage() {
		// Get the player
		Player player = new Player();
		RigidBody playerRb = player.getComponent(RigidBody.class);
		float playerHealth = player.getHealth();
		assertEquals("Player health lost early", 100, playerHealth, 0);

		// Generate the weather
		Weather weather = new Weather(10, 10);
		weather.getComponent(RigidBody.class).setPosition(player.getPosition().add(new Vector2(0, 200)));

		// Test collision
		playerRb.setPosition(weather.getComponent(RigidBody.class).getPosition());
		PhysicsManager.update();
		weather.update();
		assertNotEquals("Player health not lost after collision with weather", playerHealth, player.getHealth(), 0);

		// Reset player position
		playerRb.setPosition(new Vector2(0,0));
	}

}
