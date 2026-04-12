package actions;

import base.Combatant;
import base.ICombatAction;
import managers.BattleManager;
import statuseffects.DefendEffect;

import java.util.List;

public class DefendAction implements ICombatAction {

    @Override
    public void execute(Combatant actor, List<Combatant> enemies, BattleManager manager) {
        actor.addStatusEffect(new DefendEffect());
        actor.setDefense(actor.getDefense() + 10);
        System.out.printf("  %s defends! DEF +10 for this turn and next. (DEF: %d)%n",
            actor.getName(), actor.getDefense());
    }

    @Override
    public String getDisplayName() { return "Defend"; }
}