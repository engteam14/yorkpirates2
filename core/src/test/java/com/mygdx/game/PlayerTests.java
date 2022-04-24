package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Components.ComponentType;
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
public class PlayerTests {

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

	/**
	 * Test Identifier: 4.0
	 * Requirements Tested: UR_QUEST_PROGRESS, FR_QUEST_OBJECTIVE, FR_QUEST_TRACKING
	 */
	@Test
	public void playerProgressTasks() {
		GameManager.CreatePlayer();
		Player player = GameManager.getPlayer();

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

	/**
	 * Test Identifier: 4.1
	 * Requirements Tested: FR_PLUNDER_TRACKING, FR_PLUNDER_UPDATE, UR_EARN_PLUNDER
	 */
	@Test
	public void playerPlunder() {
		// player
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
	}

	/**
	 * Test Identifier: 4.2
	 * Requirements Tested: FR_XP_TRACKING, FR_XP_UPDATE, FR_XP_UPDATE, UR_EARN_XP
	 */
	@Test
	public void playerPoints() {
		// Generate Player
		GameManager.CreatePlayer();
		Player player = GameManager.getPlayer();
		int initial = player.getPoints();
		Vector2 playerLoc = new Vector2(player.getPosition());

		// Check points earned on quests
		QuestManager.Initialize();
		assertEquals("Player started with more than 0 points", 0, initial);
		College collegeToKill = new College(1);
		KillQuest killCollege = new KillQuest(collegeToKill);

		QuestManager.addQuest(killCollege);

		collegeToKill.killThisCollege(null);
		QuestManager.checkCompleted();

		int after = player.getPoints();
		assertTrue("Player hasn't gained points after completing killCollege task", (after > initial));

		// Check points earned on time passing
		initial = player.getPoints();
		for (int i = 0; i < 100000000; i++){
			player.update();
		}
		after = player.getPoints();
		assertTrue("Player hasn't gained points after allowing time to pass", (after > initial));
	}
}
