package cli;

import actions.*;
import base.*;
import items.*;
import managers.*;
import playerclass.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameCLI {

    private final Scanner sc = new Scanner(System.in);

    public void start() {
        showWelcome();
        boolean running = true;
        while (running) {
            Player player = choosePlayer();
            chooseItems(player);
            Level level = chooseLevel();
            BattleManager manager = new BattleManager(
                player, level, new SpeedBasedTurnOrderStrategy(), this);
            BattleResult result = manager.startBattle();
            displayResult(result, player);
            running = askReplay(result);
        }
        System.out.println("\nThanks for playing. Goodbye!");
    }

    private void showWelcome() {
        System.out.println("===========================================");
        System.out.println("       TURN-BASED COMBAT ARENA");
        System.out.println("===========================================");
        System.out.println("\nPLAYER CLASSES:");
        System.out.println("  [1] Warrior  - HP: 260 | ATK: 40 | DEF: 20 | SPD: 30");
        System.out.println("      Special: Shield Bash - Deals damage and stuns target for 2 turns.");
        System.out.println("  [2] Wizard   - HP: 200 | ATK: 50 | DEF: 10 | SPD: 20");
        System.out.println("      Special: Arcane Blast - Hits all enemies. +10 ATK per kill.");
        System.out.println("\nENEMIES:");
        System.out.println("  Goblin - HP: 55 | ATK: 35 | DEF: 15 | SPD: 25");
        System.out.println("  Wolf   - HP: 40 | ATK: 45 | DEF: 5  | SPD: 35");
        System.out.println("\nITEMS:");
        System.out.println("  Potion     - Heal 100 HP");
        System.out.println("  Smoke Bomb - Block all damage for 2 turns");
        System.out.println("  Power Stone- Trigger special skill once, no cooldown change");
    }

    public Player choosePlayer() {
        System.out.println("\n--- Choose Your Class ---");
        System.out.println("  [1] Warrior");
        System.out.println("  [2] Wizard");
        int choice = readInt(1, 2);
        Player p = choice == 1 ? new Warrior() : new Wizard();
        System.out.printf("You chose: %s%n", p.getClassName());
        return p;
    }

    public void chooseItems(Player player) {
        System.out.println("\n--- Choose 2 Items (duplicates allowed) ---");
        System.out.println("  [1] Potion");
        System.out.println("  [2] Smoke Bomb");
        System.out.println("  [3] Power Stone");
        for (int i = 1; i <= 2; i++) {
            System.out.printf("Pick item %d: ", i);
            int choice = readInt(1, 3);
            player.addItem(switch (choice) {
                case 1 -> new Potion();
                case 2 -> new SmokeBomb();
                default -> new PowerStone();
            });
        }
        System.out.print("Items selected: ");
        player.getInventory().forEach(item -> System.out.print(item.getName() + "  "));
        System.out.println();
    }

    public Level chooseLevel() {
        System.out.println("\n--- Choose Difficulty ---");
        System.out.println("  [1] Easy   - 3 Goblins");
        System.out.println("  [2] Medium - 1 Goblin + 1 Wolf | Backup: 2 Wolves");
        System.out.println("  [3] Hard   - 2 Goblins | Backup: 1 Goblin + 2 Wolves");
        int choice = readInt(1, 3);
        Level.Difficulty diff = switch (choice) {
            case 1 -> Level.Difficulty.EASY;
            case 2 -> Level.Difficulty.MEDIUM;
            default -> Level.Difficulty.HARD;
        };
        System.out.printf("Difficulty: %s%n", diff.name());
        return new Level(diff);
    }

    public ICombatAction promptPlayerAction(Player player, List<EnemyBase> livingEnemies) {
        System.out.println("\n  Your turn! Choose an action:");
        System.out.println("    [1] Basic Attack");
        System.out.println("    [2] Defend");

        boolean hasItems = player.hasItems();
        boolean canSpecial = player.canUseSpecial();
        String specialLabel = canSpecial
            ? "[3] Special Skill (" + player.getSpecialSkill().getSkillName() + ")"
            : "[3] Special Skill (Cooldown: " + player.getSpecialCooldown() + " turns)";

        if (hasItems) {
            System.out.println("    [3] " + (canSpecial ? "Special Skill (" + player.getSpecialSkill().getSkillName() + ")" : "Special Skill (Cooldown: " + player.getSpecialCooldown() + " turns)"));
            System.out.println("    [4] Use Item");
        } else {
            System.out.println("    [3] " + specialLabel);
        }

        int maxOption = hasItems ? 4 : 3;
        int choice = readInt(1, maxOption);

        return switch (choice) {
            case 1 -> {
                Combatant target = pickTarget(livingEnemies);
                yield new BasicAttack() {
                    @Override
                    public void execute(Combatant actor, List<Combatant> enemies, managers.BattleManager manager) {
                        if (target.isInvulnerable()) {
                            System.out.printf("  %s attacks %s but Smoke Bomb absorbs all damage!%n",
                                actor.getName(), target.getName());
                            return;
                        }
                        int dmg = Math.max(0, actor.getAttack() - target.getDefense());
                        target.takeDamage(dmg);
                        System.out.printf("  %s attacks %s for %d damage! (HP: %d/%d)%n",
                            actor.getName(), target.getName(), dmg,
                            target.getCurrentHp(), target.getMaxHp());
                    }
                };
            }
            case 2 -> new DefendAction();
            case 3 -> {
                if (!canSpecial) {
                    System.out.println("  Skill on cooldown! Defaulting to Basic Attack.");
                    Combatant target = pickTarget(livingEnemies);
                    yield new BasicAttack();
                }
                yield new SpecialSkillAction();
            }
            default -> {
                Item chosen = pickItem(player);
                yield new UseItemAction(chosen);
            }
        };
    }

    private Combatant pickTarget(List<EnemyBase> enemies) {
        if (enemies.size() == 1) return enemies.get(0);
        System.out.println("  Pick a target:");
        for (int i = 0; i < enemies.size(); i++) {
            EnemyBase e = enemies.get(i);
            System.out.printf("    [%d] %s (HP: %d/%d)%n", i + 1,
                e.getName(), e.getCurrentHp(), e.getMaxHp());
        }
        int idx = readInt(1, enemies.size()) - 1;
        return enemies.get(idx);
    }

    private Item pickItem(Player player) {
        List<Item> inv = player.getInventory();
        System.out.println("  Pick an item:");
        for (int i = 0; i < inv.size(); i++) {
            System.out.printf("    [%d] %s%n", i + 1, inv.get(i).getName());
        }
        return inv.get(readInt(1, inv.size()) - 1);
    }

    public void displayBattleStart(Player player, List<EnemyBase> enemies) {
        System.out.println("\n===========================================");
        System.out.println("              BATTLE START");
        System.out.println("===========================================");
        System.out.println(player);
        System.out.print("Items: ");
        player.getInventory().forEach(i -> System.out.print(i.getName() + "  "));
        System.out.println("\nEnemies:");
        enemies.forEach(e -> System.out.printf("  %s (HP: %d | ATK: %d | DEF: %d | SPD: %d)%n",
            e.getName(), e.getMaxHp(), e.getAttack(), e.getDefense(), e.getSpeed()));
        System.out.println("===========================================");
    }

    public void displayRoundHeader(int round) {
        System.out.printf("%n--- ROUND %d ---%n", round);
    }

    public void displayEndOfRound(int round, Player player, List<EnemyBase> activeEnemies) {
        System.out.printf("%nEnd of Round %d:%n", round);
        System.out.printf("  %s HP: %d/%d", player.getClassName(), player.getCurrentHp(), player.getMaxHp());
        if (!player.getInventory().isEmpty()) {
            System.out.print(" | Items: ");
            player.getInventory().forEach(i -> System.out.print(i.getName() + " "));
        }
        System.out.printf(" | Cooldown: %s%n",
            player.canUseSpecial() ? "Ready" : player.getSpecialCooldown() + " turns");
        activeEnemies.forEach(e -> System.out.printf("  %s HP: %d/%d%s%n",
            e.getName(), e.getCurrentHp(), e.getMaxHp(),
            e.isStunned() ? " [STUNNED]" : ""));
    }

    public void displayStunnedSkip(Combatant c) {
        System.out.printf("  %s is STUNNED and skips their turn!%n", c.getName());
    }

    public void displayEnemyDefeated(EnemyBase e) {
        System.out.printf("  %s has been ELIMINATED!%n", e.getName());
    }

    public void displayBackupSpawn(List<EnemyBase> backup) {
        System.out.println("\n  *** BACKUP ENEMIES ARRIVE! ***");
        backup.forEach(e -> System.out.printf("  %s enters the battle! (HP: %d | ATK: %d | DEF: %d | SPD: %d)%n",
            e.getName(), e.getMaxHp(), e.getAttack(), e.getDefense(), e.getSpeed()));
    }

    public void displayResult(BattleResult result, Player player) {
        System.out.println("\n===========================================");
        if (result.isVictory()) {
            System.out.println("  VICTORY! You defeated all enemies!");
            System.out.printf("  Remaining HP: %d/%d | Total Rounds: %d%n",
                result.getRemainingHp(), result.getMaxHp(), result.getTotalRounds());
        } else {
            System.out.println("  DEFEATED. Don't give up, try again!");
            System.out.printf("  Enemies Remaining: %d | Rounds Survived: %d%n",
                result.getEnemiesRemaining(), result.getTotalRounds());
        }
        System.out.println("===========================================");
    }

    private boolean askReplay(BattleResult result) {
        System.out.println("\nWhat would you like to do?");
        System.out.println("  [1] Play Again");
        System.out.println("  [2] Exit");
        return readInt(1, 2) == 1;
    }

    private int readInt(int min, int max) {
        while (true) {
            System.out.print("  > ");
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val >= min && val <= max) return val;
            } catch (NumberFormatException ignored) {}
            System.out.printf("  Please enter a number between %d and %d.%n", min, max);
        }
    }
}