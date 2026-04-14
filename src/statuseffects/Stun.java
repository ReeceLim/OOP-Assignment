package statuseffects;
import base.Combatant;
import base.StatusEffect;

/** Prevents the affected entity from taking actions for a set number of turns. */
public class Stun extends StatusEffect {
    public Stun() {
        super("Stun", 2);
    }
 
    @Override
    public void apply(Combatant target) {
        System.out.printf("  %s is STUNNED and cannot act!%n", target.getName());
    }
 
    @Override
    protected void onExpire(Combatant target) {
        System.out.printf("  %s's stun has expired.%n", target.getName());
    }
}

