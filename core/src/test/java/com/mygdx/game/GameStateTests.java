package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.Components.ComponentType;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Entitys.College;
import com.mygdx.game.Entitys.NPCShip;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.utils.Utilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mygdx.utils.Constants.INIT_CONSTANTS;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class GameStateTests {

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
	}

	@After
	public void dispose(){
		ResourceManager.dispose();
	}

	@Test
	public void gameStart() {
		GameManager.SpawnGame(-1);
		Player player = GameManager.getPlayer();
		RigidBody playerRb = (RigidBody) player.getComponent(ComponentType.RigidBody);

		// Check player values
		assertNotNull("Player is null", player);
		assertNotNull("Player RigidBody is null", playerRb);
		assertEquals(String.format("Player starts at %s instead of expected (800, 800)", playerRb.getPosition()), playerRb.getPosition(), new Vector2(800, 800));
		assertEquals(String.format("Player starts with %s health instead of 100", player.getHealth()), player.getHealth(), 100);
		assertEquals(String.format("Player starts with %s ammo instead of 50", player.getAmmo()), player.getAmmo(), 50);
		assertEquals(String.format("Player starts with %s plunder instead of 0", player.getPlunder()), player.getPlunder(), 0);
		assertTrue("Player is not alive on start", player.isAlive());
		// TODO: Check player points when they are added

		// Get expected pirate values
		JsonValue starting = GameManager.getSettings().get("starting");
		int expectedHealth = starting.getInt("health");
		int expectedAmmo = starting.getInt("ammo");
		int expectedPlunder = starting.getInt("plunder");

		// Iterate over factions
		JsonValue factionsSettings = GameManager.getSettings().get("factions");
		for (int i = 1; i <= factionsSettings.size; i++) {
			Faction faction = GameManager.getFaction(i);
			JsonValue factionExpected = factionsSettings.get(i-1);
			College factionCollege = GameManager.getCollege(i);

			// Get expected faction values
			String nameExpected = factionExpected.getString("name");
			String colourExpected = factionExpected.getString("colour");
			Vector2 positionExpected = new Vector2(factionExpected.get("position").getFloat("x"), factionExpected.get("position").getFloat("y"));
			positionExpected = Utilities.tilesToDistance(positionExpected);
			Vector2 spawnExpected = new Vector2(factionExpected.get("shipSpawn").getFloat("x"), factionExpected.get("shipSpawn").getFloat("y"));
			spawnExpected = Utilities.tilesToDistance(spawnExpected);

			// Check faction values
			assertNotNull("Faction is null", faction);
			assertEquals(String.format("Faction #%d named incorrectly", i), nameExpected, faction.getName());
			assertEquals(String.format("Faction #%d coloured incorrectly", i), colourExpected, faction.getColour());
			assertEquals(String.format("Faction #%d located incorrectly", i), positionExpected, faction.getPosition());
			assertEquals(String.format("Faction #%d has wrong boat spawn", i), spawnExpected, faction.getSpawnPos());

			// Check college values
			Pirate collegePirate = (Pirate) factionCollege.getComponent(ComponentType.Pirate);

			assertNotNull(String.format("College %d is null", i), factionCollege);
			assertNotNull(String.format("College %d's Pirate is null", i), collegePirate);
			assertEquals(String.format("College %d's faction is unexpected", i), collegePirate.getFaction(), faction);

			assertEquals(String.format("College #%d starts with wrong health", i), collegePirate.getHealth(), expectedHealth);
			assertEquals(String.format("College #%d starts with wrong ammo", i), collegePirate.getAmmo(), expectedAmmo);
			assertEquals(String.format("College #%d starts with wrong plunder", i), collegePirate.getPlunder(), expectedPlunder);
			assertTrue(String.format("College #%d is not alive on start", i), factionCollege.isAlive());
		}

		// Iterate over all ships apart from player
		int shipNum = GameManager.getSettings().get("factionDefaults").getInt("shipCount");
		for (int i = 1; i < shipNum * factionsSettings.size; i++){
			// Get and check ship
			NPCShip ship = GameManager.getNPCShip(i);
			assertNotNull(String.format("NPCShip %s is null", i), ship);

			// Get and check ship's pirate
			Pirate shipPirate = (Pirate) ship.getComponent(ComponentType.Pirate);
			assertNotNull(String.format("NPCShip %s's Pirate is null", i), shipPirate);
			String facName = shipPirate.getFaction().getName();

			// Check ship values
			assertEquals(String.format("NPCShip %s#%d starts with wrong health", facName, i), shipPirate.getHealth(), expectedHealth);
			assertEquals(String.format("NPCShip %s#%d starts with wrong ammo", facName, i), shipPirate.getAmmo(), expectedAmmo);
			assertEquals(String.format("NPCShip %s#%d starts with wrong plunder", facName, i), shipPirate.getPlunder(), expectedPlunder);
			assertTrue(String.format("NPCShip %s#%d is not alive on start", facName, i), ship.isAlive());
		}
	}

	@Test
	public void winGame() {

	}

	@Test
	public void loseGame() {

	}
}
