package statuseffects;

import base.Combatant;
import base.StatusEffectBase;

public class DefendEffect implements StatusEffectBase {
    private int turnsRemaining = 2;
    private final Combatant target;

    public DefendEffect(Combatant target) {
        this.target = target;
    }

    @Override
    public void tick() {
        if (turnsRemaining > 0) turnsRemaining--;
        if (turnsRemaining == 0) {
            target.setDefense(target.getDefense() - 10);
            System.out.printf("  %s's Defend effect expired. DEF restored.%n", target.getName());
        }
    }

    @Override public boolean isActive()     { return turnsRemaining > 0; }
    @Override public String getEffectName() { return "Defend"; }
}