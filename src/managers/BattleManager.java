package managers;

import base.Combatant;
import base.Enemy;
import base.ICombatAction;
import base.Player;
import base.TurnOrderStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BattleManager {

    private final Player player;
    private final List<Enemy> activeEnemies;
    private final List<Enemy> backupWave;
    private final TurnOrderStrategy turnOrderStrategy;
    private final cli.GameCLI ui;

    private int currentRound = 0;
    private boolean backupSpawned = false;

    public BattleManager(Player player, Level level, TurnOrderStrategy strategy, cli.GameCLI ui) {
        this.player = player;
        this.activeEnemies = new ArrayList<>(level.loadInitialWave());
        this.backupWave    = new ArrayList<>(level.loadBackupWave());
        this.turnOrderStrategy = strategy;
        this.ui = ui;
    }

    public BattleResult startBattle() {
        ui.displayBattleStart(player, activeEnemies);

        while (true) {
            currentRound++;
            ui.displayRoundHeader(currentRound);

            // Backup spawn checked at START of round so new enemies join
            // this round's turn order and end-of-round summary.
            tryTriggerBackupSpawn();

            List<Combatant> turnOrder = turnOrderStrategy.determineOrder(buildCombatantList());

            for (Combatant combatant : turnOrder) {
                if (!combatant.isAlive()) 
                    continue;

                combatant.tickStatusEffects();

                if (combatant.isStunned()) {
                    ui.displayStunnedSkip(combatant);
                    continue;
                }

                if (!combatant.isAlive()) 
                    continue;

                if (combatant instanceof Player p) {
                    executePlayerTurn(p);
                } else if (combatant instanceof Enemy e) {
                    executeEnemyTurn(e);
                }

                BattleResult midResult = checkEnd();
                if (midResult != null) {
                    ui.displayEndOfRound(currentRound, player, getLivingEnemies());
                    return midResult;
                }
            }

            player.tickCooldown();
            ui.displayEndOfRound(currentRound, player, getLivingEnemies());

            BattleResult endResult = checkEnd();
            if (endResult != null) return endResult;
        }
    }

    private void executePlayerTurn(Player p) {
        List<Enemy> living = getLivingEnemies();
        ICombatAction action = ui.promptPlayerAction(p, living);
        action.execute(p, new ArrayList<>(living), this);
    }

    private void executeEnemyTurn(Enemy e) {
        ICombatAction action = e.getAction();
        if (player.isInvulnerable()) {
            System.out.printf("  %s attacks but Smoke Bomb absorbs all damage!%n", e.getName());
            return;
        }
        action.execute(e, List.of(player), this);
    }

    private void tryTriggerBackupSpawn() {
        if (backupSpawned || backupWave.isEmpty()) return;
        if (activeEnemies.stream().noneMatch(Enemy::isAlive)) {
            activeEnemies.removeIf(e -> !e.isAlive());
            activeEnemies.addAll(backupWave);
            backupSpawned = true;
            ui.displayBackupSpawn(backupWave);
        }
    }

    private BattleResult checkEnd() {
        if (!player.isAlive()) {
            long remaining = activeEnemies.stream().filter(Enemy::isAlive).count();
            return BattleResult.defeat(currentRound, (int) remaining);
        }
        boolean allDead     = activeEnemies.stream().noneMatch(Enemy::isAlive);
        boolean noBackupLeft = backupSpawned || backupWave.isEmpty();
        if (allDead && noBackupLeft) {
            return BattleResult.victory(currentRound, player.getCurrentHp(), player.getMaxHp());
        }
        return null;
    }

    private List<Combatant> buildCombatantList() {
        List<Combatant> all = new ArrayList<>();
        if (player.isAlive()) all.add(player);
        activeEnemies.stream().filter(Enemy::isAlive).forEach(all::add);
        return all;
    }

    public List<Enemy> getLivingEnemies() {
        return activeEnemies.stream().filter(Enemy::isAlive).collect(Collectors.toList());
    }

    public Player getPlayer()    { return player; }
    public int getCurrentRound() { return currentRound; }
}