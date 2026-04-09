package items;

import base.Combatant;
import base.ICombatAction;
import java.util.List;

public class PotionItem implements ICombatAction{
    @Override
    public void execute(Combatant actor, Combatant target, List<Combatant> allies, List<Combatant> enemies) {
        int healAmount = 100; // heal 100 HP
        actor.heal(healAmount);
        System.out.println(actor.getName() + " used a Potion! HP restored to " + actor.getCurrentHp());
    }

    @Override
    public String getDisplayName() {
        return "Potion";
    }
}
