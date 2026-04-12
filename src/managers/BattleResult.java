package managers;

public class BattleResult {

    public enum Outcome { VICTORY, DEFEAT }

    private final Outcome outcome;
    private final int totalRounds;
    private final int remainingHp;
    private final int maxHp;
    private final int enemiesRemaining;

    private BattleResult(Outcome outcome, int totalRounds, int remainingHp, int maxHp, int enemiesRemaining) {
        this.outcome = outcome;
        this.totalRounds = totalRounds;
        this.remainingHp = remainingHp;
        this.maxHp = maxHp;
        this.enemiesRemaining = enemiesRemaining;
    }

    public static BattleResult victory(int rounds, int hp, int maxHp) {
        return new BattleResult(Outcome.VICTORY, rounds, hp, maxHp, 0);
    }

    public static BattleResult defeat(int rounds, int enemiesLeft) {
        return new BattleResult(Outcome.DEFEAT, rounds, 0, 0, enemiesLeft);
    }

    public Outcome getOutcome()         { return outcome; }
    public int getTotalRounds()         { return totalRounds; }
    public int getRemainingHp()         { return remainingHp; }
    public int getMaxHp()               { return maxHp; }
    public int getEnemiesRemaining()    { return enemiesRemaining; }
    public boolean isVictory()          { return outcome == Outcome.VICTORY; }
}