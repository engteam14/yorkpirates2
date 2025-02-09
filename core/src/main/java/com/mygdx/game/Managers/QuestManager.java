package com.mygdx.game.Managers;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Entitys.Chest;
import com.mygdx.game.Entitys.College;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Quests.KillQuest;
import com.mygdx.game.Quests.LocateQuest;
import com.mygdx.game.Quests.Quest;
import com.mygdx.utils.Utilities;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static com.mygdx.utils.Constants.TILE_SIZE;

/**
 * Creates the quests and manages their completion and order
 */
public class QuestManager {
    private static ArrayList<Quest> allQuests;
    private static Chest chest;

    public static void Initialize() {
        //boolean initialized = true;
        allQuests = new ArrayList<>();
        chest = new Chest();

    }

    /**
     * Creates a random kill quest on a random college
     *
     * @param exclude the id of factions to not kill
     * @return the id of the faction targeted
     */
    private static int rndKillQuest(ArrayList<Integer> exclude) {
        int id;
        College enemy;
        int i = 0;
        do {
            id = new Random().nextInt(4) + 2;
            enemy = GameManager.getCollege(id);
            i++;
        }
        while (Utilities.contains(exclude, id) && i < 5);
        if (i == 5) {
            return 0;
        }
        addQuest(new KillQuest(enemy));
        return id;
    }

    /**
     * Creates locate quest for a random position sourced from game settings
     */
    private static void rndLocateQuest() {
        final ArrayList<Float> locations = new ArrayList<>();
        for (float f : GameManager.getSettings().get("quests").get("locations").asFloatArray()) {
            locations.add(f);
        }
        // in game settings the positions are stored as ints with y following x it doesn't wrap
        // eg. a, b, c, d
        // v1: (a, b) v2: (c, d)
        int choice = -1;
        float x = Utilities.randomChoice(locations);
        float y;
        if (choice == locations.size() - 1) {
            y = x;
            x = locations.get(choice - 1);
        } else {
            y = locations.get(choice + 1);
        }
        x *= TILE_SIZE;
        y *= TILE_SIZE;
        addQuest(new LocateQuest(new Vector2(x, y), 1 * TILE_SIZE));
    }

    /**
     * 50/50 chance of kill quest or locate quest
     * @param exclude list of factions to exclude from killing
     */
    private static void rndQuest(ArrayList<Integer> exclude) {
        if (new Random().nextFloat() > 0.5) {
            rndLocateQuest();
        } else {
            exclude.add(rndKillQuest(exclude));
        }
    }

    /**
     * Creates the quest line with the final quest being to kill a college
     * Changed from private to public in assessment 2 to allow the separation between initialize and creating quests for testing.
     */
    public static void createRandomQuests() { //Assessment 2 change, see javadoc
        // the last quest added is the final quest
        int primaryEnemyId = new Random().nextInt(4) + 2;
        ArrayList<Integer> exclude = new ArrayList<>();
        exclude.add(primaryEnemyId);
        for (int i = 0; i < GameManager.getSettings().get("quests").getInt("count"); i++) {
            rndQuest(exclude);
        }
        College enemy = GameManager.getCollege(primaryEnemyId);
        addQuest(new KillQuest(enemy));
    }

    public static void addQuest(Quest q) {
     //   tryInit(); removed for assessment 2
        allQuests.add(q);
    }

    /**
     * Checks quests for completion and gives rewards, teleports the chest when appropriate.
     * Stops checking the quest after the first no completed quest (prevents quests being completed in any order)
     */
    public static void checkCompleted() {
        //  tryInit(); removed for assessment 2
        Player p = GameManager.getPlayer();
        for (Quest q : allQuests) {
            if (q.isCompleted()) {
                continue;
            }
            boolean completed = q.checkCompleted(p);
            if (completed) {
                p.plunder(q.getPlunderReward());
                p.points(q.getPointReward());
            } else if (q instanceof LocateQuest) {
                chest.setPosition(((LocateQuest) q).getLocation());
                break;
            } else {
                chest.setPosition(new Vector2(-1000, -1000));
                break;
            }
        }
    }

    /**
     * Returns the next un-completed quest
     * @return the quest null if no un-completed quests found
     */
    public static Quest currentQuest() {
        //  tryInit();
        for (Quest q : allQuests) {
            if (!q.isCompleted()) {
                return q;
            }
        }
        return null;
    }

    /**
     * added for assessment 2
     * sets the current quest to the one that corresponds to a given name.
     * @param current string containing the name of the quest to be set as current
     */
    public static void setCurrentQuest(String current) {
        //  tryInit();
        for (Quest q : allQuests) {
            if (Objects.equals(q.getName(), current)) {
                allQuests.remove(q);
                allQuests.add(0, q);
            }
        }
    }

    /**
     * Are there any quests
     * @return true if any non completed quest exits
     */
    public static boolean anyQuests() {
        //  tryInit(); removed for assessment 2
        return currentQuest() != null;
    }

    //    private static void tryInit() {
    //        if (!initialized) {
    //            Initialize();
    //        }
    //    }
}
