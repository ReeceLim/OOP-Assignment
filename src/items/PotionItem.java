package items;

import base.Combatant;
import base.Item;

import java.util.List;

public class PotionItem implements Item{
    private static final int HEAL_AMOUNT = 100;
 
    @Override
    public void use(Combatant user, Combatant target,
                    List<Combatant> allies, List<Combatant> enemies) {
        int hpBefore = user.getCurrentHp();
        user.heal(HEAL_AMOUNT);
        int actualHeal = user.getCurrentHp() - hpBefore;
 
        System.out.printf("  %s used Potion: HP %d → %d (+%d)%s%n",
                user.getName(),
                hpBefore,
                user.getCurrentHp(),
                actualHeal,
                actualHeal < HEAL_AMOUNT ? " (capped at max HP)" : "");
    }
 
    @Override
    public String getName()        { return "Potion"; }
}
