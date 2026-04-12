package managers;

import base.Combatant;
import base.Enemy;
import base.ICombatAction;
import base.Player;
import base.TurnOrderStrategy;
import statuseffects.DefendEffect;

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
        this.backupWave = new ArrayList<>(level.loadBackupWave());
        this.turnOrderStrategy = strategy;
        this.ui = ui;
    }

    public BattleResult startBattle() {
        ui.displayBattleStart(player, activeEnemies);

        while (true) {
            currentRound++;
            ui.displayRoundHeader(currentRound);

            List<Combatant> turnOrder = turnOrderStrategy.determineOrder(buildCombatantList());

            for (Combatant combatant : turnOrder) {
                if (!combatant.isAlive()) continue;

                combatant.tickStatusEffects();
                expireDefendIfNeeded(combatant);

                if (combatant.isStunned()) {
                    ui.displayStunnedSkip(combatant);
                    continue;
                }

                if (!combatant.isAlive()) continue;

                if (combatant instanceof Player p) {
                    ICombatAction action = ui.promptPlayerAction(p, getLivingEnemies());
                    List<Combatant> targets = new ArrayList<>(getLivingEnemiesAsCombatants());
                    action.execute(p, targets, this);
                } else if (combatant instanceof Enemy e) {
                    ICombatAction action = e.setAction();
                    if (player.isInvulnerable()) {
                        System.out.printf("  %s attacks but Smoke Bomb absorbs all damage!%n", e.getName());
                    } else {
                        action.execute(e, List.of(player), this);
                    }
                }

                removeDefeatedEnemies();

                BattleResult result = checkEnd();
                if (result != null) {
                    ui.displayEndOfRound(currentRound, player, activeEnemies);
                    return result;
                }
            }

            player.tickCooldown();
            tryTriggerBackupSpawn();
            ui.displayEndOfRound(currentRound, player, activeEnemies);

            BattleResult result = checkEnd();
            if (result != null) return result;
        }
    }

    private void expireDefendIfNeeded(Combatant c) {
        c.getStatusEffects().stream()
            .filter(e -> e.getEffectName().equals("Defend") && !e.isActive())
            .forEach(e -> c.setDefense(c.getDefense() - 10));
    }

    private void tryTriggerBackupSpawn() {
        if (backupSpawned || backupWave.isEmpty()) return;
        if (activeEnemies.stream().noneMatch(Enemy::isAlive)) {
            activeEnemies.clear();
            activeEnemies.addAll(backupWave);
            backupSpawned = true;
            ui.displayBackupSpawn(backupWave);
        }
    }

    private void removeDefeatedEnemies() {
        List<Enemy> defeated = activeEnemies.stream()
            .filter(e -> !e.isAlive()).collect(Collectors.toList());
        for (Enemy e : defeated) {
            ui.displayEnemyDefeated(e);
        }
        activeEnemies.removeIf(e -> !e.isAlive());
    }

    private BattleResult checkEnd() {
        if (!player.isAlive()) {
            return BattleResult.defeat(currentRound, activeEnemies.size());
        }
        if (activeEnemies.isEmpty() && (backupSpawned || backupWave.isEmpty())) {
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

    private List<Combatant> getLivingEnemiesAsCombatants() {
        return new ArrayList<>(getLivingEnemies());
    }

    public Player getPlayer()       { return player; }
    public int getCurrentRound()    { return currentRound; }
}