package statuseffects;

import base.Combatant;
import base.StatusEffect;

public class ArcaneBlastEffect extends StatusEffect {

    private final int atkBonus = 10;

    public ArcaneBlastEffect() {
        super("Arcane Blast", Integer.MAX_VALUE); //duration will be until end of the level 
    }

    // activate on kill
    @Override
    public void apply(Combatant target) {
        target.addAttack(atkBonus); // add 10 to the Wizard's attack
        System.out.println(target.getName() + " gains +10 ATK from Arcane Blast!");
    }
}