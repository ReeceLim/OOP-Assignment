package base;

import java.util.List;

public interface IEnemyAction
{
    ICombatAction decideAction(Combatant self, List<Combatant> allies, List<Combatant> enemies);
}