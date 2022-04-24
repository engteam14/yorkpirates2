package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.AI.Node;
import com.mygdx.game.AI.Path;
import com.mygdx.game.Components.AINavigation;
import com.mygdx.game.Components.ComponentType;
import com.mygdx.game.Components.Renderable;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Entitys.*;
import com.mygdx.game.Managers.GameDifficulty;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.Physics.CollisionInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mygdx.utils.Constants.INIT_CONSTANTS;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class AITests {

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
	 * Test Identifier: 9.0
	 * Requirements Tested: FR_FRIENDLY_AI, FR_HOSTILE_AI
	 */
	@Test
	public void nodePosition() {
		// Test initialising a Node
		Vector2 startPos = new Vector2(10, 15);
		Node testNode = new Node(startPos.x, startPos.y);
		assertEquals("Node position is different to value initialised with", startPos, testNode.getPosition());

		// Test changing a Node
		Vector2 newPos = new Vector2(0, -5);
		testNode.set(newPos.x, newPos.y);
		assertNotEquals("Node position did not set at all", startPos, testNode.getPosition());
		assertEquals("Node position was set but incorrectly", newPos, testNode.getPosition());
	}

	/**
	 * Test Identifier: 9.1
	 * Requirements Tested: FR_FRIENDLY_AI, FR_HOSTILE_AI
	 */
	@Test
	public void pathNodes() {
		// Initialize path
		Node from = new Node(1,2);
		Node to = new Node(5,12);
		Path testPath = new Path(from, to);

		// Test nodes are correct
		assertEquals("from Node is different than value initialised with", from, testPath.getFromNode());
		assertEquals("to Node is different than value initialised with", to, testPath.getToNode());
	}

	/**
	 * Test Identifier: 9.2
	 * Requirements Tested: FR_FRIENDLY_AI, FR_HOSTILE_AI
	 */
	@Test
	public void pathCost() {
		// Initialize path
		Node from = new Node(1,2);
		Node to = new Node(5,12);
		Path testPath = new Path(from, to);

		// Test path cost is correct
		assertEquals("Path cost is non expected value", -1, testPath.getCost(), 0);
	}
}
