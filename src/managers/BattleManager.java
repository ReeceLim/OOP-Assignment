package managers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import base.*;

public class BattleManager {

    private final Player            player;
    private final List<Combatant>       activeEnemies;      // enemies currently on the field
    private final List<Combatant>       backupWave;         // backup enemies waiting to spawn
    private final TurnOrderStrategy turnOrderStrategy;
    private final BattleUI          ui;

    private int     currentRound;
    private boolean initialWaveDefeated;
    private boolean backupSpawned;

    public BattleManager(Player player,
                         List<Combatant> initialEnemies,
                         List<Combatant> backupWave,
                         TurnOrderStrategy turnOrderStrategy,
                         BattleUI ui) {
        this.player            = player;
        this.activeEnemies     = new ArrayList<>(initialEnemies);
        this.backupWave        = new ArrayList<>(backupWave);
        this.turnOrderStrategy = turnOrderStrategy;
        this.ui                = ui;
        this.currentRound      = 0;
        this.initialWaveDefeated = false;
        this.backupSpawned       = false;
    }

    // -----------------------------------------------------------------------
    // Public entry point
    // -----------------------------------------------------------------------

    /**
     * Runs the battle to completion and returns the outcome.
     *
     * @return BattleResult indicating victory or defeat with statistics
     */
    public BattleResult startBattle() {
        ui.displayBattleStart(player, activeEnemies);

        while (true) {
            currentRound++;
            ui.displayRoundHeader(currentRound);

            // --- Backup spawn check (beginning of round, before turns) ---
            tryTriggerBackupSpawn();

            // --- Build the ordered turn list for this round ---------------
            List<Combatant> allCombatants = buildCombatantList();
            List<Combatant> turnOrder     = turnOrderStrategy.determineOrder(allCombatants);

            // --- Process each combatant's turn ----------------------------
            for (Combatant combatant : turnOrder) {

                // Skip dead combatants (may have been killed mid-round)
                if (!combatant.isAlive()) {
                    continue;
                }

                // 1. Apply existing status effects (damage-over-time, stun check, etc.)
                combatant.applyStatusEffects();
                ui.displayStatusEffectResults(combatant);

                // 2. Check if stunned – stunned combatants skip their action
                if (combatant.isStunned()) {
                    ui.displayStunnedSkip(combatant);
                    combatant.decrementStun();
                    continue;
                }

                // 3. Combatant takes its action
                if (combatant instanceof Player p) {
                    handlePlayerTurn(p);
                } else if (combatant instanceof Combatant e) {
                    handleEnemyTurn(e);
                }

                // 4. Update HP, status effects, and inventory in UI
                ui.displayTurnResult(combatant, activeEnemies, player);

                // 5. Prune newly-defeated enemies
                removeDefeatedEnemies();

                // 6. Check game-ending condition after every action
                BattleResult result = checkBattleEnd();
                if (result != null) {
                    ui.displayEndOfRound(currentRound, player, activeEnemies);
                    return result;
                }
            }

            // --- Tick per-round durations (Defend bonus expires, etc.) ----
            tickRoundEffects();

            // --- End-of-round display -------------------------------------
            ui.displayEndOfRound(currentRound, player, activeEnemies);

            // Final win/loss check at round boundary
            BattleResult result = checkBattleEnd();
            if (result != null) {
                return result;
            }
        }
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Builds a flat list of all living combatants (player + active enemies).
     * TurnOrderStrategy operates on Combatant abstractions (DIP).
     */
    private List<Combatant> buildCombatantList() {
        List<Combatant> all = new ArrayList<>();
        if (player.isAlive()) {
            all.add(player);
        }
        for (Combatant e : activeEnemies) {
            if (e.isAlive()) {
                all.add(e);
            }
        }
        return all;
    }

    /**
     * Handles the player's turn: prompt for action, execute it.
     * Actions are self-contained objects (OCP) – BattleManager does not
     * need to know which concrete Action is selected.
     */
    private void handlePlayerTurn(Player p) {
        List<Combatant> livingEnemies = getLivingEnemies();
        Action chosenAction = ui.decidePlayerAction(p, livingEnemies);
        chosenAction.execute(p, livingEnemies, this);
    }

    /**
     * Handles an enemy's turn.
     * Enemies always perform BasicAttack in this version.
     * The EnemyActionStrategy on the Combatant determines the concrete action,
     * keeping BattleManager decoupled (OCP / DIP).
     */
    private void handleEnemyTurn(Combatant e) {
        ICombatAction action = e.decideAction(player);
        action.execute(e, List.of(player), this);
    }

    /**
     * Triggers backup spawn if:
     *  - there is a backup wave defined,
     *  - it has not already been spawned, and
     *  - all initial enemies are defeated.
     *
     * "All entities of a Backup Spawn enter simultaneously" (spec §3.6).
     */
    private void tryTriggerBackupSpawn() {
        if (backupSpawned || backupWave.isEmpty()) {
            return;
        }

        boolean allInitialDefeated = activeEnemies.stream().noneMatch(Enemy::isAlive);
        if (allInitialDefeated) {
            activeEnemies.addAll(backupWave);
            backupSpawned = true;
            ui.displayBackupSpawn(backupWave);
        }
    }

    /**
     * Removes enemies that have been defeated (HP == 0) from the active list.
     * Called after each action so checks are always up-to-date.
     */
    private void removeDefeatedEnemies() {
        activeEnemies.removeIf(e -> {
            if (!e.isAlive()) {
                ui.displayEnemyDefeated(e);
                return true;
            }
            return false;
        });
    }

    /**
     * Ticks end-of-round countdowns:
     *  - Defend bonus duration
     *  - Smoke Bomb invulnerability duration
     *  - Special skill cooldowns
     *
     * Each Combatant manages its own state; BattleManager just triggers the tick.
     */
    private void tickRoundEffects() {
        player.tickRoundEffects();
        for (Combatant e : activeEnemies) {
            e.tickRoundEffects();
        }
    }

    /**
     * Returns a BattleResult if the battle is over, or null if it continues.
     *
     * Win  – all active enemies defeated AND no pending backup wave.
     * Loss – player HP == 0.
     */
    private BattleResult checkBattleEnd() {
        if (!player.isAlive()) {
            return BattleResult.defeat(currentRound, getLivingEnemies().size());
        }

        boolean noEnemiesLeft = activeEnemies.isEmpty();
        boolean noMoreBackup  = backupSpawned || backupWave.isEmpty();

        if (noEnemiesLeft && noMoreBackup) {
            return BattleResult.victory(currentRound, player.getCurrentHp(), player.getMaxHp());
        }

        return null; // battle continues
    }

    // -----------------------------------------------------------------------
    // Accessors used by Action implementations
    // -----------------------------------------------------------------------

    /** Returns only the living enemies currently on the field. */
    public List<Enemy> getLivingEnemies() {
        return activeEnemies.stream()
                .filter(Enemy::isAlive)
                .collect(java.util.stream.Collectors.toList());
    }

    public Player getPlayer()       { return player; }
    public int    getCurrentRound() { return currentRound; }
}
