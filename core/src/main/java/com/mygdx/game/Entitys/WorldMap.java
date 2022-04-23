package com.mygdx.game.Entitys;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.game.Components.TileMap;
import com.mygdx.game.Managers.PhysicsManager;
import com.mygdx.game.Managers.RenderLayer;

/**
 * The world map
 */
public class WorldMap extends Entity {
    /**
     * Creates the World Map
     * @param mapId The ID of the map to create
     */
    public WorldMap(int mapId) {
        super(1);
        setName("WorldMap");
        TileMap map = new TileMap(mapId, RenderLayer.Five);
        addComponent(map);
        PhysicsManager.createMapCollision(map);
    }

    /**
     * @return the Tile Map attached to this object
     */
    public TiledMap getTileMap() {
        return getComponent(TileMap.class).getTileMap();
    }
}
