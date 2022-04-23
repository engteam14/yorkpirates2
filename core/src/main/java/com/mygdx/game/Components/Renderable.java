package com.mygdx.game.Components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Managers.RenderLayer;
import com.mygdx.game.Managers.RenderingManager;
import com.mygdx.game.Managers.ResourceManager;

/**
 * Add the ability for the object to be shown
 */
public class Renderable extends Component {
    protected Sprite sprite;
    private boolean isVisible;

    /**
     * Called in other constructors, loads no textures by itself.
     */
    public Renderable() {
        super();
        isVisible = true;
        type = ComponentType.Renderable;
        sprite = new Sprite();
        RenderingManager.addItem(this, RenderLayer.Transparent);
    }

    /**
     * Associates Renderable with the given texture sprite and layer.
     * @param texId the id of the texture the sprite will take on
     * @param layer the rendering layer
     */
    public Renderable(int texId, RenderLayer layer) {
        this();
        sprite = new Sprite(ResourceManager.getTexture(texId));
        RenderingManager.addItem(this, layer);
    }

    /**
     * Associates Renderable with the given sprite from a texture atlas and a layer.
     * @param atlasId the id of the texture atlas containing the sprite
     * @param texName the name of the texture the sprite will take on
     * @param layer   the rendering layer
     */
    public Renderable(int atlasId, String texName, RenderLayer layer) {
        this();
        sprite = new Sprite(ResourceManager.getSprite(atlasId, texName));
        RenderingManager.addItem(this, layer);
    }

    /**
     * Locates the sprite at the position of the parent's Transform component.
     */
    @Override
    public void update() {
        super.update();
        if (sprite == null) {
            return;
        }
        Transform c = parent.getComponent(Transform.class);
        if (c == null) {
            return;
        }
        Vector2 p = c.getPosition();
        Vector2 s = c.getScale();

        sprite.setPosition(p.x, p.y);
        sprite.setRotation(MathUtils.radiansToDegrees * c.getRotation());
        sprite.setScale(s.x, s.y);
    }

    /**
     * Checks if the sprite exists and should be visible, then renders it
     */
    @Override
    public void render() {
        super.render();
        if (sprite == null || !isVisible) {
            return;
        }
        sprite.draw(RenderingManager.getBatch());
    }

    /**
     * Called once after the update loop has finished.
     */
    @Override
    public void cleanUp() {
        super.cleanUp();
    }

    /**
     * @return the Sprite for this object
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Assigns a new texture compatible with textures sourced from atlas
     * @param s the sprite contain the texture
     */
    public void setTexture(Sprite s) {
        Sprite a = getSprite();
        a.setTexture(s.getTexture());
        a.setU(s.getU());
        a.setV(s.getV());
        a.setU2(s.getU2());
        a.setV2(s.getV2());
    }

    /**
     * @return whether the object should currently be visible
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Changes the visibility to active
     */
    public void show() {
        isVisible = true;
    }

    /**
     * Changes the visibility to inactive
     */
    public void hide() {
        isVisible = false;
    }

    //public void toggleVisibility() {
    //    isVisible = !isVisible;
    //}
}
