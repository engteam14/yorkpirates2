package com.mygdx.game.Components;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Entitys.Ship;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.utils.Utilities;

/**
 * Used to control NPCs with steerable for movement and state machines for behaviour
 */
public class AINavigation extends Component implements Steerable<Vector2> {
    /**
     * NPC settings class to be used for steerable units
     */
    private static class Attributes {
        public float boundingRadius = 128;
        public float maxSpd = GameManager.getSettings().get("AI").getFloat("maxSpeed");
        public float maxAcc = 50000;
        public float maxAngSpd = 0;
        public float maxAngAcc = 0;
        public boolean isTagged = false;
    }

    RigidBody rb;
    Transform t;
    Attributes attributes;
    SteeringBehavior<Vector2> behavior;
    SteeringAcceleration<Vector2> steeringOutput;

    /**
     * Creates a simple navigation component
     */
    public AINavigation() {
        super();
        attributes = new Attributes();
        setRequirements(ComponentType.RigidBody);
        type = ComponentType.AINavigation;
        steeringOutput = new SteeringAcceleration<>(new Vector2());
    }

    /**
     * @param behavior the behavior the AI will be set to
     */
    public void setBehavior(SteeringBehavior<Vector2> behavior) {
        this.behavior = behavior;
    }

    /**
     * Gets the components if != null
     */
    private void getComps() {
        if (rb == null) {
            rb = parent.getComponent(RigidBody.class);
            t = parent.getComponent(Transform.class);
        }
    }

    /**
     * Called once per frame. Apply the steering behaviour and sets the ship direction, so it faces the right way
     */
    @Override
    public void update() {
        super.update();
        getComps();
        if (behavior != null) {
            behavior.calculateSteering(steeringOutput);
            applySteering();
        } else {
            stop();
        }

        Vector2 vel = rb.getVelocity().cpy();
        if (vel.x == 0 && vel.y == 0) {
            ((Ship) parent).setShipDirection("-up");
            return;
        }
        vel.nor();
        Utilities.round(vel);

        if (Ship.shipDirections.containsKey(vel)) {
            ((Ship) parent).setShipDirection(vel);
        }
    }

    /**
     * Calculates the forces required by the steering behaviour (no rotation)
     */
    private void applySteering() {
        boolean anyAcc = false;
        if (!steeringOutput.linear.isZero()) {
            Vector2 f = steeringOutput.linear;
            rb.applyForce(f);
            anyAcc = true;
        }

        if (anyAcc) {
            Vector2 vel = rb.getVelocity();
            float speed = vel.len2();
            if (speed > attributes.maxSpd * attributes.maxSpd) {
                rb.setVelocity(vel.scl(attributes.maxSpd / (float) Math.sqrt(speed)));
            }
        }
    }

    /**
     * Stops all motion
     */
    public void stop() {
        getComps();
        rb.setVelocity(new Vector2(0, 0));
    }

    /**
     * @return the linear velocity of the rigid body this AI is attached to
     */
    @Override
    public Vector2 getLinearVelocity() {
        getComps();
        return rb.getVelocity();
    }

    /**
     * @return the angular velocity of the rigid body this AI is attached to
     */
    @Override
    public float getAngularVelocity() {
        getComps();
        return rb.getAngularVelocity();
    }

    /**
     * @return the bounding radius of the AI's area
     */
    @Override
    public float getBoundingRadius() {
        return attributes.boundingRadius;
    }

    /**
     * @return whether the AI has been tagged
     */
    @Override
    public boolean isTagged() {
        return attributes.isTagged;
    }

    /**
     * @param tagged the boolean to set the tagged status to
     */
    @Override
    public void setTagged(boolean tagged) {
        attributes.isTagged = tagged;
    }

    /**
     * @return the Zero Linear Speed Threshold `unused`
     */
    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.01f;
    }

    /**
     * @param value the float to set the Zero Linear Speed Threshold to `unused`
     */
    @Override
    public void setZeroLinearSpeedThreshold(float value) {}

    /**
     * @return the maximum Linear Speed
     */
    @Override
    public float getMaxLinearSpeed() {
        return attributes.maxSpd;
    }

    /**
     * @param maxLinearSpeed the float to set the maximum Linear Speed to
     */
    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        attributes.maxSpd = maxLinearSpeed;
    }

    /**
     * @return the maximum Linear Acceleration
     */
    @Override
    public float getMaxLinearAcceleration() {
        return attributes.maxAcc;
    }

    /**
     * @param maxLinearAcceleration the float to set the maximum Linear Acceleration to
     */
    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        attributes.maxAcc = maxLinearAcceleration;
    }

    /**
     * @return the maximum Angular Speed
     */
    @Override
    public float getMaxAngularSpeed() {
        return attributes.maxAngSpd;
    }

    /**
     * @param maxAngularSpeed the float to set the maximum Angular Speed to
     */
    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        attributes.maxAngSpd = maxAngularSpeed;
    }

    /**
     * @return the maximum Angular Acceleration
     */
    @Override
    public float getMaxAngularAcceleration() {
        return attributes.maxAngAcc;
    }

    /**
     * @param maxAngularAcceleration the float to set the maximum Angular Acceleration to
     */
    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        attributes.maxAngAcc = maxAngularAcceleration;
    }

    /**
     * @return the position of the AI
     */
    @Override
    public Vector2 getPosition() {
        getComps();
        return t.getPosition();
    }

    /**
     * @return the orientation of the AI
     */
    @Override
    public float getOrientation() {
        getComps();
        return t.getRotation();
    }

    /**
     * @param orientation the float to set the orientation to `unused`
     */
    @Override
    public void setOrientation(float orientation) {}

    /**
     * @param vector the vector to be converted
     * @return the parsed vector as an angle
     */
    @Override
    public float vectorToAngle(Vector2 vector) {
        return Utilities.vectorToAngle(vector);
    }

    /**
     * @param angle the angle to be converted
     * @return the parsed angle as a vector
     */
    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Utilities.angleToVector(outVector, angle);
    }

    /**
     * @return an empty location
     */
    @Override
    public Location<Vector2> newLocation() {
        getComps();
        return t;
    }
}
