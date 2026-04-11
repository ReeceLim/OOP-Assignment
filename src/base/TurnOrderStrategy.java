import java.util.List;

/**
 * Strategy interface for determining turn order among combatants.
 * Follows OCP: new ordering strategies can be added without modifying BattleEngine.
 * Follows DIP: BattleEngine depends on this abstraction, not concrete implementations.
 */

public interface TurnOrderStrategy {

    /**
     * Sorts and returns the list of combatants in the order they should act this round.
     *
     * @param combatants all living combatants participating in the current round
     * @return ordered list of combatants (first element acts first)
     */
    List<Combatant> determineOrder(List<Combatant> combatants);
}
