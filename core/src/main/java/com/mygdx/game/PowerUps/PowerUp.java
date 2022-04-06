package com.mygdx.game.PowerUps;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Components.PowerUpAssigned;
import com.mygdx.game.Managers.GameManager;

/**
 * // Assessment 2 requirement //
 * A PowerUp that can modify the value of any Ship for a limited or permanent amount of time.
 */
public class PowerUp {

    private String name;
    private int cost;
    private String key;
    private PowerUpOperation oper;
    private float value;
    private long duration;
    private long startTime;
    private boolean done;

    /**
     * Create a PowerUp manually, used for testing.
     * @param key           The key of the value to affect
     * @param operation     The way the key is applied to the value
     * @param value         The value that is applied to the key
     * @param cost          The cost of the powerup
     */
    public PowerUp(String key, PowerUpOperation operation, float value, int cost, int duration) {
        this.key = key;
        this.oper = operation;
        this.value = value;
        this.duration = duration;
        this.done = false;
        this.cost = cost ;
    }

    /**
     * Create a PowerUp from the settings file
     * @param settings the json file containing the settings for the power up
     */
    public PowerUp(JsonValue settings) {
        this.key = settings.getString("key");
        this.oper = PowerUpOperation.values()[settings.getInt("operation")];
        this.value = settings.getInt("value");
        this.duration = settings.getInt("duration");
        this.done = false;
        this.cost = settings.getInt("cost") ;
        this.name = settings.getString("name");
    }


    /**
     * Apply PowerUp modifiers to the Pirate.
     * @param pirate        The Pirate to apply the PowerUp to
     */
    public void EnablePowerUp(Pirate pirate) {
        switch (oper){
            case replace:
                pirate.setValue(key, value);
                break;
            case increment:
                pirate.setValue(key, pirate.getValue(key) + value);
                break;
            case decrement:
                pirate.setValue(key, pirate.getValue(key) - value);
                break;
            case multiply:
                pirate.multValue(key, value);
                break;
            case divide:
                pirate.multValue(key, 1f/value);
                break;
        }
        startTime = TimeUtils.millis();
    }

    /**
     * Check whether the PowerUp is permanent or temporary.
     * @return          True if the PowerUp is permanent
     */
    public boolean CheckPermanent() {
        return (duration <= 0);
    }

    /**
     * Check whether a PowerUp is done.
     * @return          Whether the PowerUp is done or not
     */
    public boolean CheckPowerUpDone() {
        return CheckPowerUpDone(null);
    }

    /**
     * Check whether a PowerUp is done, and Disable it if so.
     * @param pirate    The Pirate class to disable the PowerUp for
     * @return          Whether the PowerUp is done or not
     */
    public boolean CheckPowerUpDone(Pirate pirate) {
        if (done) return true;
        if (duration > 0) {
            long timeSoFar = TimeUtils.timeSinceMillis(startTime) / 1000;
            if (timeSoFar >= duration) {
                if (pirate != null) DisablePowerUp(pirate);
                return true;
            }
        } return false;
    }

    /**
     * Remove PowerUp modifiers from a Pirate and reset to default.
     * @param pirate        The Pirate to return to normal
     */
    public void DisablePowerUp(Pirate pirate) {
        if (duration > 0) pirate.resetToDefault(key);
        done = true;
    }

    public Boolean buyPowerUp(){
        if (GameManager.getPlayer().getPlunder() >= this.cost) {
            GameManager.getPlayer().plunder((-this.cost));
            GameManager.getPlayer().getComponent(PowerUpAssigned.class).AssignPowerUp(this);
            return true;
        }else {
            return false;
            }
      }


    public String getName() {
        return name;
    }
}
