package ui;

import base.Combatant;
import base.Enemy;
import base.ICombatAction;
import base.Player;
import managers.BattleResult;

import java.util.List;

public interface BattleUI {

    void displayBattleStart(Player player, List<Enemy> enemies);

    void displayRoundHeader(int round);

    void displayEndOfRound(int round, Player player, List<Enemy> activeEnemies);

    void displayStunnedSkip(Combatant combatant);

    void displayEnemyDefeated(Enemy enemy);

    void displayBackupSpawn(List<Enemy> backup);

    void displayResult(BattleResult result, Player player);

    ICombatAction promptPlayerAction(Player player, List<Enemy> livingEnemies);
}