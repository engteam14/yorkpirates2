package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.Components.ComponentType;
import com.mygdx.game.Components.Renderable;
import com.mygdx.game.Components.RigidBody;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Entitys.CannonBall;
import com.mygdx.game.Entitys.College;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Entitys.Ship;
import com.mygdx.game.Managers.*;
import com.mygdx.game.Quests.KillQuest;
import com.mygdx.game.Quests.LocateQuest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mygdx.utils.Constants.INIT_CONSTANTS;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class ShipTests {

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
	}

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
		assertTrue(String.format("Ship moved from %s to %s when velocity of %s applied.", startPos, rb.getPosition(), velocity), correctMovement);
	}

	@Test
	public void playerProgressTasks() {
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

		collegeToKill.killThisCollege(null);
		boolean questStatusD = killCollege.checkCompleted(player);

		assertFalse("Kill Quest shown as complete when isn't",questStatusC);
		assertTrue("Kill Quest shown as incomplete when isn't",questStatusD);
	}

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

	@Test
	public void playerMoney() {
		// player
		GameManager.Initialize();
		GameManager.CreatePlayer();
		Player player = GameManager.getPlayer();
		int initial = player.getPlunder();
		Vector2 playerLoc = new Vector2(player.getPosition());
		//quests
		QuestManager.Initialize();
		assertEquals("Player started with more than 0 loot", 0, initial);
		College collegeToKill = new College(1);
		KillQuest killCollege = new KillQuest(collegeToKill);
		LocateQuest locateQuest = new LocateQuest(playerLoc, 1);

		QuestManager.addQuest(killCollege);

		collegeToKill.killThisCollege(null);
		QuestManager.checkCompleted();

		int after = player.getPlunder();
		assertTrue("Player hasn't gained loot after completing killCollege task", (after > initial));


		initial = after; //new initial after 1 complete quest

		QuestManager.addQuest(locateQuest);
		QuestManager.checkCompleted();

		after = player.getPlunder();
		assertTrue("Player hasn't gained loot after picking up loot chest ", (after > initial));
		//TODO: Check loot on enemy kill

	}

	@Test
	public void playerPoints() {

	}
}
