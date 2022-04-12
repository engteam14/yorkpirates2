package com.mygdx.game.Entitys;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Components.*;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.QuestManager;
import com.mygdx.game.Managers.RenderLayer;
import com.mygdx.game.Quests.KillQuest;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Indicator extends Entity {
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
		Renderable r = new Renderable(10, RenderLayer.Transparent);
		r.hide();

		addComponents(t, r);
		gradient = updateGradient();
	}

	@Override
	public void update(){
		if(QuestManager.currentQuest() == quest){
			getComponent(Renderable.class).show();
		}else{
			getComponent(Renderable.class).hide();
		}
		followPlayer();
	}

	/**
	 * Called when updating the gradient of the Indicator.
	 */
	public Vector2 updateGradient(){
		Player p = GameManager.getPlayer();
		Transform self = p.getComponent(Transform.class);
		Transform goal = target.getComponent(Transform.class);

		// Calculate the gradient to draw the indicator at.
		float changeInX = goal.getPosition().x - self.getPosition().x;
		float changeInY = goal.getPosition().y - self.getPosition().y;
		float scaleFactor = max(abs(changeInX),abs(changeInY));
		float dx = changeInX / scaleFactor;
		float dy = changeInY / scaleFactor;

		return (new Vector2(dx, dy));
	}

	/**
	 * Called when updating the position of the Indicator.
	 */
	public void followPlayer() {
		gradient = updateGradient();
		rotateSprite();
		Player p = GameManager.getPlayer();
		Vector2 position = new Vector2( p.getPosition().x + (50 * gradient.x), p.getPosition().y + (50 * gradient.y) );
		getComponent(Transform.class).setPosition(position);
	}

	public void rotateSprite() {
		getComponent(Transform.class).setRotation((float) Math.atan2(gradient.y,gradient.x));
	}
}
