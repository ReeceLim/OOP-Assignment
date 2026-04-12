package specialskills;

import base.Player;
import base.Enemy;
import base.SpecialSkill;
import java.util.List;

/**
 * Wizard skill: deals BasicAttack damage to ALL enemies.
 * Each enemy killed grants +10 ATK to the Wizard for the rest of the level.
 */
public class ArcaneBlast extends SpecialSkill {
    public ArcaneBlast() {
        super("Arcane Blast");
    }

    @Override
    public void execute(Player caster, List<Enemy> enemies) {
        List<Enemy> alive = enemies.stream().filter(Enemy::isAlive).toList();
        if (alive.isEmpty()) return;

        System.out.println("Arcane Blast hits all enemies!");
        int kills = 0;

        for (Enemy enemy : alive) {
            int damage = Math.max(0, caster.getAttack() - enemy.getDefense());
            enemy.takeDamage(damage);
            System.out.printf("  %s takes %d damage (HP: %d).%n", enemy.getName(), damage, enemy.getCurrentHp());

            if (!enemy.isAlive()) {
                kills++;
                // Grant +10 ATK per kill before applying to next target
                caster.setAttack(caster.getAttack() + 10);
                System.out.printf("  %s eliminated! Wizard ATK +10 → %d.%n", enemy.getName(), caster.getAttack());
            }
        }

        if (kills == 0) System.out.println("No enemies defeated.");
    }
}
