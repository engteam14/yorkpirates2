package com.mygdx.game.AI;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Entitys.NPCShip;
import com.mygdx.game.Entitys.Ship;

/**
 * State machine used for NPC ships' behaviour
 */
public enum EnemyState implements State<NPCShip> {
    /**
     * Picks random pos and travels to it
     */
    WANDER() {
        @Override
        public void enter(NPCShip e) {
            e.stopMovement();
        }

        @Override
        public void update(NPCShip e) {
            super.update(e);
            e.circleOrigin(); //Added in Assessment 2 so idle ships would patrol around colleges
        }
    },
    /**
     * Tries to get into attack range of the player
     */
    PURSUE() {
        @Override
        public void enter(NPCShip e) {
            e.stopMovement();
            e.followTarget();
        }

        @Override
        public void update(NPCShip e) {
            super.update(e);
        }
    },
    /**
     * Attempts to kill the enemy
     */
    ATTACK() {
        @Override
        public void enter(NPCShip e) {
            e.stopMovement();
        }

        @Override
        public void update(NPCShip e) {
            super.update(e);
            Ship ship = e.getComponent(Pirate.class).getTarget();
            e.attackShip(ship);
        }
    };

    /**
     * Called every from for every NPC ship (there or there abouts)
     * @param e the sender
     */
    @Override
    public void update(NPCShip e) {
        StateMachine<NPCShip, EnemyState> m = e.stateMachine;
        Pirate p = e.getComponent(Pirate.class);
        switch (m.getCurrentState()) {
            case WANDER:
                if (p.isAggro()) {
                    m.changeState(PURSUE);
                } else if (p.canAttack()) {
                    m.changeState(ATTACK);
                }
                break;
            case PURSUE:
                // if enter attack range attack
                if (p.canAttack()) {
                    m.changeState(ATTACK);
                }
                // if leave detection range wander
                if (!p.canAttack() && !p.isAggro()) {
                    m.changeState(WANDER);
                }
                break;
            case ATTACK:
                // if leave attack range pursue
                if (p.isAggro() && !p.canAttack()) {
                    m.changeState(PURSUE);
                }
                // if target dead
                else if (!p.getTarget().isAlive()) {
                    m.changeState(WANDER);
                }
                break;
        }
    }

    /**
     * Called when a state is left
     * @param e the sender
     */
    @Override
    public void exit(NPCShip e) {

    }

    /**
     * Called when a state is entered
     * @param entity the sender
     */
    @Override
    public void enter(NPCShip entity) {

    }

    /**
     * not used
     */
    @Override
    public boolean onMessage(NPCShip e, Telegram telegram) {
        return false;
    }
}
