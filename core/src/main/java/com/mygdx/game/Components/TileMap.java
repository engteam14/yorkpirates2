package com.mygdx.game.Components;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Managers.RenderLayer;
import com.mygdx.game.Managers.RenderingManager;
import com.mygdx.game.Managers.ResourceManager;

import static com.mygdx.utils.Constants.TILE_SIZE;

/**
 * Component that allows the rendering of tile-maps (has its own sprite batch)
 */
public class TileMap extends Component {
    TiledMap map;
    TiledMapRenderer renderer;

    /**
     * Sets up the base values of the TileMap component
     */
    private TileMap() {
        super();
        type = ComponentType.TileMap;
        // CollisionManager.addTileMap(this);
    }

    /**
     * @param id    resource id of the tilemap
     * @param layer rendering layer
     */
    public TileMap(int id, RenderLayer layer) {
        this();
        map = ResourceManager.getTileMap(id);
        renderer = new OrthogonalTiledMapRenderer(map);
        RenderingManager.addItem(this, layer);

        TILE_SIZE = getTileDim().x;
    }

    /**
     * @return the dimensions of the tilemap
     */
    public Vector2 getTileDim() {
        return new Vector2(
                ((TiledMapTileLayer) map.getLayers().get(0)).getTileWidth(),
                ((TiledMapTileLayer) map.getLayers().get(0)).getTileHeight());
    }

    /**
     * @return the map attached to this object
     */
    public TiledMap getTileMap() {
        return map;
    }

    /**
     * Updates the renderer's view with the rendering camera
     */
    @Override
    public void update() {
        super.update();
        renderer.setView(RenderingManager.getCamera());
    }

    /**
     * draws the first 2 layers
     */
    @Override
    public void render() {
        super.render();
        renderer.render(new int[]{0, 1});
    }

    /**
     * Calls the Clean-Up function of the Component class
     */
    @Override
    public void cleanUp() {
        super.cleanUp();
    }

    //public TiledMapTileLayer.Cell getCell(Vector2 pos) {
    //        Vector2 p = pos.cpy();
    //        TiledMapTileLayer l = (TiledMapTileLayer) map.getLayers().get(1);
    //        p.x /= l.getTileWidth();
    //        p.y /= l.getTileHeight();
    //
    //        return l.getCell((int) p.x, (int) p.y);
    //    }
}
