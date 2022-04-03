package com.mygdx.game.Quests;

import com.mygdx.game.Entitys.Player;

/**
 * Base class for all quests facilitates the checking of completion
 */
public abstract class Quest {
    protected String name;
    protected String description;
    protected int plunderReward;
    protected int pointReward;
    protected boolean isCompleted;

    public Quest() {
        name = "";
        description = "";
        plunderReward = 0;
        pointReward = 0;
        isCompleted = false;
    }

    /**
     * Checks if the given player has met the complete condition
     *
     * @param p the player
     * @return has completed
     */
    public abstract boolean checkCompleted(Player p);

    public int getPlunderReward() {
        return plunderReward;
    }

    public int getPointReward() {
        return pointReward;
    }

    public boolean isCompleted() {
        return isCompleted;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
