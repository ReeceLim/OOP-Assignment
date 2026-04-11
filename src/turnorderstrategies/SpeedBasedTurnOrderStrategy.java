package turnorderstrategies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import base.TurnOrderStrategy;
import base.Combatant;

public class SpeedBasedTurnOrderStrategy implements TurnOrderStrategy {

    @Override
    public List<Combatant> determineOrder(List<Combatant> combatants) {
        List<Combatant> ordered = new ArrayList<>(combatants);
        // Stable sort: equal-speed combatants keep their original relative order
        ordered.sort(Comparator.comparingInt(Combatant::getSpeed).reversed());
        return ordered;
    }
}
