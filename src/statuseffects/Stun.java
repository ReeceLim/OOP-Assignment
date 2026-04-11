package statuseffects;
import base.StatusEffectBase;

/** Prevents the affected entity from taking actions for a set number of turns. */
public class Stun implements StatusEffectBase {
    private int turnsRemaining;
 
    public Stun(int duration) {
        this.turnsRemaining = duration;
    }
 
    @Override
    public void tick() {
        if (turnsRemaining > 0) turnsRemaining--;
    }
 
    @Override
    public boolean isActive() {
        return turnsRemaining > 0;
    }
 
    @Override
    public String getEffectName() { return "Stun"; }
 
    public int getTurnsRemaining() { return turnsRemaining; }
}

