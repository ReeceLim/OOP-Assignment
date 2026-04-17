package statuseffects;

import base.Combatant;
import base.StatusEffect;

public class StrengthEffect implements StatusEffect {
    private int turnsRemaining = 2;
    private final Combatant target;

    public StrengthEffect(Combatant target) {
        this.target = target;
    }

    @Override
    public void tick() {
        if (turnsRemaining > 0) turnsRemaining--;
        if (turnsRemaining == 0) {
            target.setAttack(target.getAttack() - 20);
            System.out.printf("  %s's Strength effect expired. ATK reduced.%n", target.getName());
        }
    }

    @Override public boolean isActive()     { return turnsRemaining > 0; }
    @Override public String getEffectName() { return "Strength"; }
    @Override public int getTurnsRemaining() { return turnsRemaining; }
}