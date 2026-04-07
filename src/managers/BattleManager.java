import java.util.ArrayList;
import java.util.List;

/**
 * BattleManager orchestrates the full lifecycle of a battle.
 *
 * Responsibilities (SRP):
 *  - Manage round progression
 *  - Delegate turn ordering to TurnOrderStrategy
 *  - Coordinate each combatant's turn (status effects → action)
 *  - Check win/loss conditions after each action and at end of round
 *  - Trigger backup spawns when the initial enemy wave is fully defeated
 *
 * Depends on abstractions (DIP):
 *  - Combatant  (not Warrior/Goblin directly)
 *  - TurnOrderStrategy (not SpeedBasedTurnOrderStrategy directly)
 *  - BattleUI   (not a concrete CLI class)
 *
 * OCP: adding a new TurnOrderStrategy or Action type requires zero changes here.
 */
public class BattleManager {

    // -----------------------------------------------------------------------
    // Dependencies (injected via constructor – DIP)
    // -----------------------------------------------------------------------
    private final Player            player;
    private final List<Enemy>       activeEnemies;      // enemies currently on the field
    private final List<Enemy>       backupWave;         // backup enemies waiting to spawn
    private final TurnOrderStrategy turnOrderStrategy;
    private final BattleUI          ui;

    // -----------------------------------------------------------------------
    // State
    // -----------------------------------------------------------------------
    private int     currentRound;
    private boolean initialWaveDefeated;
    private boolean backupSpawned;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------
    /**
     * @param player           the human-controlled player combatant
     * @param initialEnemies   enemies present at battle start
     * @param backupWave       enemies that spawn after the initial wave is wiped;
     *                         pass an empty list if the level has no backup wave
     * @param turnOrderStrategy strategy used to sort combatants each round
     * @param ui               the UI boundary responsible for all I/O
     */
    public BattleManager(Player player,
                         List<Enemy> initialEnemies,
                         List<Enemy> backupWave,
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
                } else if (combatant instanceof Enemy e) {
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
        for (Enemy e : activeEnemies) {
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
        List<Enemy> livingEnemies = getLivingEnemies();
        Action chosenAction = ui.promptPlayerAction(p, livingEnemies);
        chosenAction.execute(p, livingEnemies, this);
    }

    /**
     * Handles an enemy's turn.
     * Enemies always perform BasicAttack in this version.
     * The EnemyActionStrategy on the Enemy determines the concrete action,
     * keeping BattleManager decoupled (OCP / DIP).
     */
    private void handleEnemyTurn(Enemy e) {
        Action action = e.decideAction(player);
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
        for (Enemy e : activeEnemies) {
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
