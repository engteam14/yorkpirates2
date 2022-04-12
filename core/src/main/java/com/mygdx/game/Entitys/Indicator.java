package com.mygdx.game.Entitys;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Components.*;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.RenderLayer;
import com.mygdx.game.Physics.PhysicsBodyType;
import com.mygdx.game.Quests.KillQuest;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Indicator extends Entity{
	private final Entity target;
	private final KillQuest quest;

	private Vector2 gradient;
	private boolean visible;

	public Indicator(KillQuest kq) {
		super(2);
		quest = kq;
		target = quest.getTarget().getParent();

		Transform t = new Transform();
		t.setPosition(800, 800);
		Renderable r = new Renderable(3, "white-up", RenderLayer.Transparent);

		addComponents(t, r);

		Player p = GameManager.getPlayer();
		gradient = updateGradient(target.getComponent(Transform.class),p.getComponent(Transform.class));
	}

	/**
	 * Called when updating the gradient of the Indicator.
	 */
	public Vector2 updateGradient(Transform target, Transform self){
		// Calculate the gradient to draw the indicator at.
		float changeInX = target.getPosition().x - self.getPosition().x;
		float changeInY = target.getPosition().y - self.getPosition().y;
		float scaleFactor = max(abs(changeInX),abs(changeInY));
		float dx = changeInX / scaleFactor;
		float dy = changeInY / scaleFactor;

		return (new Vector2(dx, dy));
	}
}
