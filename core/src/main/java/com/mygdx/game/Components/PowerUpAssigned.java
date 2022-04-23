package com.mygdx.game.Components;

import com.mygdx.game.PowerUps.PowerUp;

/**
 * // Assessment 2 addition //
 * For PowerUp requirement,
 * Holds the current PowerUp and manages it.
 */
public class PowerUpAssigned extends Component {

    private Pirate pirate;
    private PowerUp powerUpAssigned;

    /**
     * Assign a PowerUp, disabling the old one if present.
     * @param powerUp   The PowerUp to be assigned
     */
    public void AssignPowerUp(PowerUp powerUp) {
        if (pirate == null){
            pirate = parent.getComponent(Pirate.class);
        }
        if (!powerUp.CheckPermanent()) {
            if (powerUpAssigned != null) {
                powerUpAssigned.DisablePowerUp(pirate);
            }
            powerUp.EnablePowerUp(pirate);
            powerUpAssigned = powerUp;
        } else {
            powerUp.EnablePowerUp(pirate);
        }
    }

    /**
     * Each frame checks if the assigned PowerUp is done, and deactivates as necessary.
     */
    @Override
    public void update() {
        super.update();
        if (pirate == null){
            pirate = parent.getComponent(Pirate.class);
        }
        if (powerUpAssigned != null) {
            powerUpAssigned.CheckPowerUpDone(pirate);
        }
    }
}
