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

import static com.mygdx.utils.Constants.INIT_CONSTANTS;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class CollegeTests {

	@Before
	public void init(){
		int id_ship = ResourceManager.addTexture("ship.png");
		int id_map = ResourceManager.addTileMap("Map.tmx");
		int atlas_id = ResourceManager.addTextureAtlas("Boats.txt");
		int extras_id = ResourceManager.addTextureAtlas("UISkin/skin.atlas");
		int buildings_id = ResourceManager.addTextureAtlas("Buildings.txt");
		int powerups_id = ResourceManager.addTextureAtlas("powerups.txt");
		int obstacles_id = ResourceManager.addTextureAtlas("obstacles.txt");
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
	public void collegeExists() {
		College college = new College(1);
		assertTrue("College is spawned dead",college.isAlive());
	}

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

	@Test
	public void collegeIsCaptured() {
		College college = new College(5);
		Player player = new Player();

		Faction factionPrior = college.getFaction();
		college.killThisCollege(player.getFaction());
		Faction factionPost = college.getFaction();

		assertNotEquals("College has not changed factions", factionPrior, factionPost);
		assertEquals("College faction not changed to match conqueror", factionPost, player.getFaction());
	}
}
