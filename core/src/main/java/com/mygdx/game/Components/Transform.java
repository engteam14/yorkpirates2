package com.mygdx.game.Components;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.utils.Utilities;

/**
 * Position, Scale, Rotation (in radians clockwise)
 */
public class Transform extends Component implements Location<Vector2> {
    private final Vector2 position;
    private final Vector2 scale;
    private float rotation;

    /**
     * position = (0, 0)
     * scale = (0, 0)
     * rotation = 0
     * rot not used but easy to add functionality for
     */
    public Transform() {
        position = new Vector2();
        scale = new Vector2(1, 1);
        rotation = 0;
        type = ComponentType.Transform;
    }

    /**
     * Set position associated with the Transform component.
     *
     * @param pos 2D vector specifying the position
     * @param rb  true to pass on the position to the parent's RigidBody
     */
    public void setPosition(Vector2 pos, boolean rb) {
        setPosition(pos.x, pos.y, rb);
    }

    /**
     * Set position associated with the Transform component.
     *
     * @param x   coordinate
     * @param y   coordinate
     * @param rb_ true to pass on the position to the parent's RigidBody
     */
    public void setPosition(float x, float y, boolean rb_) {
        position.set(x, y);
        if (parent != null && rb_) {
            RigidBody rb = parent.getComponent(RigidBody.class);
            if (rb != null) {
                rb.setPosition(position);
            }
        }
    }

    /**
     * Set position associated with the Transform component.
     *
     * @param pos 2D vector specifying the position
     */
    public void setPosition(Vector2 pos) {
        setPosition(pos.x, pos.y);
    }

    /**
     * Set position associated with the Transform component.
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void setPosition(float x, float y) {
        position.set(x, y);
        if (parent != null) {
            RigidBody rb = parent.getComponent(RigidBody.class);
            if (rb != null) {
                rb.setPosition(position);
            }
        }
    }

    /**
     * Sets the scale of the component
     * @param x    the x dimension
     * @param y    the y dimension
     */
    public void setScale(float x, float y) {
        scale.set(x, y);
    }

    /**
     * @param rot in Radians
     */
    public void setRotation(float rot) {
        rotation = rot;
    }

    /**
     * @return the position of the component on the grid
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * @return the orientation of the component `unused`
     */
    @Override
    public float getOrientation() {
        return 0;
    }

    /**
     * @param orientation the float value to set the orientation of the component to `unused`
     */
    @Override
    public void setOrientation(float orientation) {}

    /**
     * Return new angle in radians from input vector.
     * @param vector 2D vector
     * @return the vector as an angle
     */
    @Override
    public float vectorToAngle(Vector2 vector) {
        return Utilities.vectorToAngle(vector);
    }

    /**
     * Return new vector combining input vector with input angle in radians.
     * @param outVector 2D vector
     * @param angle     in radians
     * @return the angle as a vector
     */
    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Utilities.angleToVector(outVector, angle);
    }

    /**
     * @return a Transform object at a new location
     */
    @Override
    public Location<Vector2> newLocation() {
        return new Transform();
    }

    /**
     * @return the scale of this object
     */
    public Vector2 getScale() {
        return scale;
    }

    /**
     * @return the angle of this object in radians
     */
    public float getRotation() {
        return rotation;
    }

    //public void setScale(Vector2 pos) {
    //        scale.set(pos);
    //    }

    //    public Vector2 getCenter() {
    //        RigidBody rb = parent.getComponent(RigidBody.class);
    //        if (rb == null) {
    //            return getPosition();
    //        }
    //        return rb.getBody().getWorldCenter();
    //    }
}
