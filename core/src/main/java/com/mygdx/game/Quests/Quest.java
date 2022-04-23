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

    /**
     * Constructs a quest with base values filled in
     */
    public Quest() {
        name = "";
        description = "";
        plunderReward = 0;
        pointReward = 0;
        isCompleted = false;
    }

    /**
     * Checks if the given player has met the complete condition
     * @param p the player
     * @return has completed
     */
    public abstract boolean checkCompleted(Player p);

    /**
     * @return The plunder reward for this quest
     */
    public int getPlunderReward() {
        return plunderReward;
    }

    /**
     * @return The point reward for this quest
     */
    public int getPointReward() {
        return pointReward;
    }

    /**
     * @return The completion status of the current quest
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * @return The name of this quest
     */
    public String getName() {
        return name;
    }

    /**
     * @return The description of this quest
     */
    public String getDescription() {
        return description;
    }
}
