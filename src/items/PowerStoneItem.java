package items;

import base.Combatant;
import base.ICombatAction;
import java.util.List;

public class PowerStoneItem implements ICombatAction {

    private final ICombatAction skill;

    public PowerStoneItem(ICombatAction skill) {
        this.skill = skill;
    }

    @Override
    public void execute(Combatant actor, Combatant target, List<Combatant> allies, List<Combatant> enemies) {
        System.out.println(actor.getName() + " used Power Stone to trigger skill for free!");
        skill.execute(actor, target, allies, enemies);
    }

    @Override
    public String getDisplayName() {
        return "Power Stone";
    }
}