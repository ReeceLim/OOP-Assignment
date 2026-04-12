package statuseffects;

import base.StatusEffect;
import base.Combatant;

public class StunEffect extends StatusEffect{
    public StunEffect() {
        super("Stun", 2);  // stun for 2 turns, current + next turn
    }

    @Override
    public void apply(Combatant target){
        System.out.println(target.getName() + " is stunned and cannot act!");
        target.setStunned(true);
    }

    @Override
    public void tick(Combatant target) {
        super.tick(target);
        if (isExpired()) {
            target.setStunned(false);
            System.out.println(target.getName() + "'s stun has expired.");
        }
    }
}
