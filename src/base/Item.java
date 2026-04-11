package base;

import java.util.List;
 
public interface Item {
    void use(Combatant user, Combatant target,
             List<Combatant> allies, List<Combatant> enemies);
 
    String getName();
}