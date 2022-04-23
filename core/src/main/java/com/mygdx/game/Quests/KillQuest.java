package com.mygdx.game.Quests;

import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Entitys.Entity;
import com.mygdx.game.Entitys.Indicator;
import com.mygdx.game.Entitys.Player;

/**
 * A Quest to kill a college is only complete once that college is dead
 */
public class KillQuest extends Quest {
    private Pirate target;

    /**
     * Creates a Kill Quest, with all the base attributes filled in with templates
     */
    public KillQuest() {
        super();
        name = "Kill the college";
        description = "KILL KILL KILL";
        plunderReward = 100;
        pointReward = 200;
        target = null;
    }

    /**
     * Creates a Kill Quest
     * @param target The Pirate which needs to be defeated for the quest
     */
    public KillQuest(Pirate target) {
        this();
        this.target = target;
        description = target.getFaction().getName();
        new Indicator(this);
    }

    /**
     * Creates a Kill Quest
     * @param target The Entity which needs to be defeated for the quest
     */
    public KillQuest(Entity target) {
        this(target.getComponent(Pirate.class));
    }

    /**
     * @return The completion status of the quest
     */
    @Override
    public boolean checkCompleted(Player p) {
        isCompleted = !target.isAlive();
        return isCompleted;
    }

    /**
     * Added for Assessment 2
     * @return the target of this quest
     */
    public Pirate getTarget() {
        return target;
    }
}
