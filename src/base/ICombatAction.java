package base;
 
import java.util.List;
 
public interface ICombatAction {
    void execute(Combatant actor, List<Combatant> enemies, managers.BattleManager manager);
    String getDisplayName();
}
 