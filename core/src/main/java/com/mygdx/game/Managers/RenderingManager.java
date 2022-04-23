package com.mygdx.game.Managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Components.Component;

import java.util.ArrayList;

import static com.mygdx.utils.Constants.*;

/**
 * Responsible for all rending. Renders in layers render item layers can't be changed
 * holds the primary sprite batch and rendering camera
 */
public final class RenderingManager {
    private static ArrayList<Component> renderItems;
    private static ArrayList<ArrayList<Integer>> layers;
    private static OrthographicCamera camera;
    private static SpriteBatch batch;

    public static void Initialize() {
        //boolean initialized = true;
        renderItems = new ArrayList<>();

        batch = new SpriteBatch();
        // batch.enableBlending();
        camera = new OrthographicCamera();
        camera.viewportHeight = VIEWPORT_HEIGHT / ZOOM;
        camera.viewportWidth = VIEWPORT_WIDTH / ZOOM;
        camera.update();

        layers = new ArrayList<>(RenderLayer.values().length);

        for (int i = 0; i < RenderLayer.values().length; i++) {
            layers.add(new ArrayList<>());
        }
    }

    public static OrthographicCamera getCamera() {
        return camera;
    }

    /**
     * adds item to the list of renderable and adds to the correct layer
     * @param item  component that utilises render
     * @param layer the layer that it will be rendered in
     */
    public static void addItem(Component item, RenderLayer layer) {
        //tryInit();

        // start of change for assessment 2 - to initialise empty lists if initialise() hasn't already been called due to testing
        if(renderItems == null) renderItems = new ArrayList<>();
        if(layers == null) {
            layers = new ArrayList<>(RenderLayer.values().length);
            for (int i = 0; i < RenderLayer.values().length; i++) {
                layers.add(new ArrayList<>());
            }
        }
        renderItems.add(item);
        layers.get(layer.ordinal()).add(renderItems.size() - 1);
    }

    /**
     * Renders all items in accordance with their layers on one sprite batch
     */
    public static void render() {
        //tryInit();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (ArrayList<Integer> layer : layers) {
            for (Integer itemIndex : layer) {
                Component item = renderItems.get(itemIndex);
                item.render();
            }
        }

        /*for(int i = 0; i < renderItems.size(); i++){
            //renderItems.get(renderItems.size() - (1 + i)).render();
            renderItems.get(i).render();
        }*/

        batch.end();
    }

    public static void cleanUp() {
        batch.dispose();
    }

    public static SpriteBatch getBatch() {
        return batch;
    }

    //public static void setCamera(OrthographicCamera cam) {
    //        camera = cam;
    //    }

    //private static void tryInit() {
    //        if (!initialized) {
    //            //Initialize();
    //        }
    //    }
}
