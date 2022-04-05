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
}
