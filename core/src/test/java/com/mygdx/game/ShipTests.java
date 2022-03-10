package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Components.ComponentType;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Entitys.Building;
import com.mygdx.game.Entitys.College;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.Quests.KillQuest;
import com.mygdx.game.Quests.LocateQuest;
import com.mygdx.utils.Utilities;
import org.junit.After;
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

	@After
	public void dispose(){
		ResourceManager.dispose();
	}

	@Test
	public void shipMove() {
		Player player = new Player();
		RigidBody playerRb = (RigidBody) player.getComponent(ComponentType.RigidBody);

		moveTest(playerRb, new Vector2(0,0));

		moveTest(playerRb, new Vector2(1, 0));
		moveTest(playerRb, new Vector2(-1,0));

		moveTest(playerRb, new Vector2(0,1));
		moveTest(playerRb, new Vector2(0,-1));

		moveTest(playerRb, new Vector2(1,1));
		moveTest(playerRb, new Vector2(1,-1));
		moveTest(playerRb, new Vector2(-1,1));
		moveTest(playerRb, new Vector2(-1,-1));
	}

	private void moveTest(RigidBody rb, Vector2 velocity){
		Vector2 startPos = rb.getPosition().cpy();

		// Move in desired direction
		for (int i = 0; i < 500; i++) {
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
		assertTrue(String.format("Ship moved from %s to %s when velocity of %s applied.", startPos.toString(), rb.getPosition().toString(), velocity.toString()), correctMovement);
	}

	@Test
	public void progressTasks() {
		Player player = new Player();

		Vector2 questLocA = new Vector2(100,100);
		Vector2 questLocB = new Vector2(player.getPosition());

		LocateQuest questA = new LocateQuest(questLocA,1);
		LocateQuest questB = new LocateQuest(questLocB,1);

		boolean questStatusA = questA.checkCompleted(player);
		boolean questStatusB = questB.checkCompleted(player);

		assertFalse("Locate Quest shown as complete when isn't",questStatusA);
		assertTrue("Locate Quest shown as incomplete when isn't",questStatusB);

		College collegeToKill = new College(1);
		KillQuest killCollege = new KillQuest(collegeToKill);
		boolean questStatusC = killCollege.checkCompleted(player);

		collegeToKill.killThisCollege();
		boolean questStatusD = killCollege.checkCompleted(player);

		assertFalse("Kill Quest shown as complete when isn't",questStatusC);
		assertTrue("Kill Quest shown as incomplete when isn't",questStatusD);
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
}
