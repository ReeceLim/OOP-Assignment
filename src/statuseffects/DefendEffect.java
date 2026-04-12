package statuseffects;
 
import base.StatusEffectBase;
 
public class DefendEffect implements StatusEffectBase {
    private int turnsRemaining = 2;
 
    @Override public void tick()            { if (turnsRemaining > 0) turnsRemaining--; }
    @Override public boolean isActive()     { return turnsRemaining > 0; }
    @Override public String getEffectName() { return "Defend"; }
    public int getTurnsRemaining()          { return turnsRemaining; }
}
 