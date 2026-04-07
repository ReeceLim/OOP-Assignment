package base;

import java.util.List;
 
public interface ICombatAction 
{
    void execute(Combatant actor, Combatant target,
                 List<Combatant> allies, List<Combatant> enemies);
 
    String getDisplayName();
}
 