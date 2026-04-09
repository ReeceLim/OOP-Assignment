package items;

import base.Combatant;
import base.ICombatAction;
import java.util.List;
import statuseffects.SmokeBombEffect;

public class SmokeBombItem implements ICombatAction {

    @Override
    public void execute(Combatant actor, Combatant target, List<Combatant> allies, List<Combatant> enemies) {
        actor.addStatusEffect(new SmokeBombEffect());
        System.out.println(actor.getName() + " used Smoke Bomb! Damage will be 0 this and next turn.");
    }

    @Override
    public String getDisplayName() {
        return "Smoke Bomb";
    }
}