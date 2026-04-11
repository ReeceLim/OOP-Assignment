package ui;

import java.util.List;
import base.*;

public interface BattleUI
{
    /* 
     * Player action method
    */
    ICombatAction decidePlayerAction(Combatant player, List<Combatant> enemies);
    
    /* 
     * Prompt player to select target
    */
    Combatant promptTargetSelection(Combatant actor, List<Combatant> targets);

    /*
     * Display the state of all combatants at the end of a round.
    */
    void displayRoundSummary(int roundNumber, List<Combatant> players, List<Combatant> enemies);

    void displayRoundHeader(int roundNumber);

    void displayMessage(String message);

    void displayVictory(int remainingHp, int maxHp, int totalRounds);

    void displayDefeat(int enemiesRemaining, int totalRounds);

    int promptPostGameChoice();
}