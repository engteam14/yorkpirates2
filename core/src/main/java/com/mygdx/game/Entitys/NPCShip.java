package com.mygdx.game.Entitys;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.AI.EnemyState;
import com.mygdx.game.Components.*;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Physics.CollisionCallBack;
import com.mygdx.game.Physics.CollisionInfo;
import com.mygdx.utils.QueueFIFO;
import com.mygdx.utils.Utilities;

import java.util.Objects;

/**
 * NPC ship entity class.
 */
public class NPCShip extends Ship implements CollisionCallBack {
    public StateMachine<NPCShip, EnemyState> stateMachine;
    private static JsonValue AISettings;
    private long lastShootTime; //Added for Assessment 2, stores the time when the ship last attacked

    /**
     * Creates an initial state machine
     * Changed for Assessment 2:
     *  - Added lastShootTime to store the time when the ship last attacked
     */
    public NPCShip() {
        super();
        //QueueFIFO<Vector2> path = new QueueFIFO<>();

        if (AISettings == null) {
            AISettings = GameManager.getSettings().get("AI");
        }

        stateMachine = new DefaultStateMachine<>(this, EnemyState.WANDER);

        setName("NPC");
        AINavigation nav = new AINavigation();

        addComponent(nav);


        RigidBody rb = getComponent(RigidBody.class);
        // rb.setCallback(this);

        JsonValue starting = GameManager.getSettings().get("starting");

        // agro trigger
        rb.addTrigger(Utilities.tilesToDistance(starting.getFloat("argoRange_tiles")), "agro");
        getComponent(Pirate.class).setInfiniteAmmo(true);

        lastShootTime = TimeUtils.millis() / 1000;
    }

    /**
     * gets the top of targets from pirate component
     *
     * @return the top target
     */
    private Ship getTarget() {
        return getComponent(Pirate.class).getTarget();
    }

    /**
     * Amended for Assessment 2: Added check for life and kills stateMachine and ship in case of death
     * updates the state machine
     */
    @Override
    public void update() {
        if(!isAlive()) {
           kill();
           stateMachine = null;
        }else{
            super.update();
            stateMachine.update();
        }
    }

    /**
     * Added for Assessment 2
     * Removes the ship from play
     */
    private void kill() {
        getComponent(Renderable.class).hide();
        Transform t = getComponent(Transform.class);
        t.setPosition(20000, 20000);

        RigidBody rb = getComponent(RigidBody.class);
        rb.setPosition(t.getPosition());
        rb.setVelocity(0, 0);

        dispose();
    }

    /**
     * creates a new steering behaviour that will make the NPC beeline for the target (doesn't factor in obstacles)
     */
    public void followTarget() {
        if (getTarget() == null) {
            stopMovement();
            return;
        }
        AINavigation nav = getComponent(AINavigation.class);

        Arrive<Vector2> arrives = new Arrive<>(nav,
                getTarget().getComponent(Transform.class))
                .setTimeToTarget(AISettings.getFloat("accelerationTime"))
                .setArrivalTolerance(AISettings.getFloat("arrivalTolerance"))
                .setDecelerationRadius(AISettings.getFloat("slowRadius"));

        nav.setBehavior(arrives);
    }

    /**
     * Added for Assessment 2
     * Creates a new steering behaviour that will make the NPC circle the college it is attached to (doesn't factor in obstacles)
     */
    public void circleOrigin() {
        College origin = GameManager.getCollege(getFaction().id);
        Vector2 dirToOrigin = new Vector2( (origin.getPosition().x - getPosition().x),(origin.getPosition().y - getPosition().y) );
        double currentAngle = Utilities.vectorToAngle(dirToOrigin);

        double theta;
        if (dirToOrigin.x > 0){
            theta = currentAngle - (Math.PI)/12;
        }else{
            theta = currentAngle + (Math.PI)/12;
        }

        Transform target = new Transform();
        float radius = Utilities.tilesToDistance(30);
        float xPos = (float) (origin.getPosition().x + (radius * Math.cos(theta)));
        float yPos = (float) (origin.getPosition().y + (radius * Math.sin(theta)));
        target.setPosition(xPos,yPos);

        AINavigation nav = getComponent(AINavigation.class);

        Arrive<Vector2> arrives = new Arrive<>(nav,target)
                .setTimeToTarget(AISettings.getFloat("accelerationTime"))
                .setArrivalTolerance(AISettings.getFloat("arrivalTolerance"))
                .setDecelerationRadius(AISettings.getFloat("slowRadius"));

        nav.setBehavior(arrives);
    }

    /**
     * stops all movement and sets the behaviour to null
     */
    public void stopMovement() {
        AINavigation nav = getComponent(AINavigation.class);
        nav.setBehavior(null);
        nav.stop();
    }

    /**
     * Added for Assessment 2, calculates the direction an Enemy Ship is in
     */
    public Vector2 directionToShip(Ship ship) {
        Vector2 shipLocale = ship.getPosition();
        Vector2 thisPosition = getPosition();

        float xDiff = shipLocale.x-thisPosition.x;
        float yDiff = shipLocale.y-thisPosition.y;

        return new Vector2(xDiff,yDiff);
    }

    /**
     * Added for Assessment 2, shoots a cannonball towards the player ship
     */
    public void attackShip(Ship ship) {
        long current = TimeUtils.millis() / 1000;
        if (current > lastShootTime) {
            Vector2 direction = directionToShip(ship);
            shoot(direction);
        }
        lastShootTime = current;
    }

    /**
     * @return the current state the NPCShip is in
     */
    public EnemyState getCurrentState() {
        return stateMachine.getCurrentState();
    }

    /**
     * if the agro fixture hit a ship set it as the target
     * @param info the collision info
     */
    @Override
    public void EnterTrigger(CollisionInfo info) {
        super.EnterTrigger(info);
        if (info.a instanceof Ship) {
            Ship other = (Ship) info.a;
            if (Objects.equals(other.getComponent(Pirate.class).getFaction(), getComponent(Pirate.class).getFaction())) {
                // is the same faction
                return;
            }
            // add the new collision as a new target
            Pirate pirate = getComponent(Pirate.class);
            pirate.addTarget(other);
        }
    }

    /**
     * if a target has left remove it from the potential targets Queue
     * @param info collision info
     */
    @Override
    public void ExitTrigger(CollisionInfo info) {
        if (!(info.a instanceof Ship)) {
            return;
        }
        Pirate pirate = getComponent(Pirate.class);
        Ship o = (Ship) info.a;
        // remove the object from the targets list
        for (Ship targ : pirate.getTargets()) {
            if (targ == o) {
                pirate.getTargets().remove(targ);
                break;
            }
        }
    }
}
