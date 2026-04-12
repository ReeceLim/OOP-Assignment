package managers;

import base.Enemy;
import enemytype.Goblin;
import enemytype.Wolf;

import java.util.ArrayList;
import java.util.List;

public class Level {

    public enum Difficulty { EASY, MEDIUM, HARD }

    private final Difficulty difficulty;

    public Level(Difficulty difficulty) { this.difficulty = difficulty; }

    public Difficulty getDifficulty() { return difficulty; }

    public String getDifficultyName() { return difficulty.name(); }

    public List<Enemy> loadInitialWave() {
        return switch (difficulty) {
            case EASY   -> List.of(new Goblin("Goblin A"), new Goblin("Goblin B"), new Goblin("Goblin C"));
            case MEDIUM -> List.of(new Goblin("Goblin"), new Wolf("Wolf"));
            case HARD   -> List.of(new Goblin("Goblin A"), new Goblin("Goblin B"));
        };
    }

    public List<Enemy> loadBackupWave() {
        return switch (difficulty) {
            case EASY   -> new ArrayList<>();
            case MEDIUM -> List.of(new Wolf("Wolf A"), new Wolf("Wolf B"));
            case HARD   -> List.of(new Goblin("Goblin C"), new Wolf("Wolf A"), new Wolf("Wolf B"));
        };
    }

    public boolean hasBackupWave() {
        return difficulty != Difficulty.EASY;
    }
}