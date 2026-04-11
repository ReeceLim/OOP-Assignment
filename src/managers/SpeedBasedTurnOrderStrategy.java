package managers;
 
import base.Combatant;
import base.TurnOrderStrategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Determines turn order by descending Speed stat.
 * Higher speed acts first. Ties preserve original list order (stable sort).
 *
 * Satisfies the assignment rule: "Higher speed goes first."
 */
public class SpeedBasedTurnOrderStrategy implements TurnOrderStrategy {

    @Override
    public List<Combatant> determineOrder(List<Combatant> combatants) {
        List<Combatant> ordered = new ArrayList<>(combatants);
        // Stable sort: equal-speed combatants keep their original relative order
        ordered.sort(Comparator.comparingInt(Combatant::getSpeed).reversed());
        return ordered;
    }
}
