package com.mygdx.game.Entitys;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Components.Renderable;
import com.mygdx.game.Components.Transform;
import com.mygdx.game.Managers.RenderLayer;
import com.mygdx.game.Managers.ResourceManager;

/**
 * Added for Assessment 2 in order to implement quest functionality
 * Simple entity shown on locate quests origin
 */
public class Chest extends Entity {
    /**
     * Constructs the Chest object
     */
    public Chest() {
        super(2);
        Transform t = new Transform();
        Renderable r = new Renderable(ResourceManager.getId("Chest.png"), RenderLayer.Transparent);
        addComponents(t, r);
    }

    /**
     * @param pos The Vector2 to set the position of the chest to
     */
    public void setPosition(Vector2 pos) {
        getComponent(Transform.class).setPosition(pos);
    }
}
