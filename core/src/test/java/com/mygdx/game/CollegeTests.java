package com.mygdx.game;

import com.badlogic.gdx.Game;
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
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.ResourceManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static com.mygdx.utils.Constants.INIT_CONSTANTS;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class CollegeTests {

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
	 * Test Identifier: 1.0
	 * Requirements Tested: UR_COMPETING_COLLEGES
	 */
	@Test
	public void collegeExists() {
		College college = new College(1);
		GameManager.SpawnGame(-1);

		assertTrue("College is spawned dead",college.isAlive());
		assertTrue("Less than 3 colleges are spawned",GameManager.getColleges().size() > 2);
	}

	/**
	 * Test Identifier: 1.1
	 * Requirements Tested: UR_HOSTILE_BUILDING_COMBAT
	 */
	@Test
	public void collegeFires() {
		College college = new College();
		CannonBall cannonBall = GameManager.getCurrentCannon();
		Vector2 shootDirection = new Vector2(1,1);

		Transform collegeT = (Transform) college.getComponent(ComponentType.Transform);
		Transform cannonT = (Transform) cannonBall.getComponent(ComponentType.Transform);
		Vector2 cannonStartPos = cannonT.getPosition().cpy();

		college.shoot(collegeT.getPosition(),shootDirection);
		Vector2 cannonNewPos = cannonT.getPosition().cpy();

		assertNotEquals("Cannonball position has not been fired", cannonStartPos, cannonNewPos);
	}

	/**
	 * Test Identifier: 1.1
	 * Requirements Tested: UR_HOSTILE_COLLEGE_CAPTURE
	 */
	@Test
	public void collegeIsCaptured() {
		GameManager.CreatePlayer();
		College college = new College(5);
		Player player = GameManager.getPlayer();

		Faction factionPrior = college.getFaction();
		college.killThisCollege(player.getFaction());
		Faction factionPost = college.getFaction();

		assertNotEquals("College has not changed factions", factionPrior, factionPost);
		assertEquals("College faction not changed to match conqueror", factionPost, player.getFaction());
	}

	/**
	 * Test Identifier: 1.2
	 * Requirements Tested:
	 */
	@Test
	public void displacementFromShip() {
		GameManager.CreatePlayer();
		College college = new College(5);
		Player player = GameManager.getPlayer();

		college.getComponent(Transform.class).setPosition(0,0);
		player.getComponent(Transform.class).setPosition(0,0);

		Vector2 zeroVector = new Vector2(0,0);
		ArrayList<Vector2> zeroDisplacement = new ArrayList<>();
		zeroDisplacement.add(zeroVector);
		zeroDisplacement.add(zeroVector);

		assertEquals("Displacement incorrect when college and ship same location", zeroDisplacement, college.displacementFromShip(player));

		player.getComponent(Transform.class).setPosition(10,10);

		Vector2 tenVector = new Vector2(10,10);
		ArrayList<Vector2> tenDisplacement = new ArrayList<>();
		tenDisplacement.add(tenVector);
		tenDisplacement.add(tenVector);

		assertEquals("Displacement incorrect when college and ship different locations", tenDisplacement, college.displacementFromShip(player));
	}
}
