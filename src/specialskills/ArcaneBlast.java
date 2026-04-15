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
        List<Enemy> alive = enemies.stream()
            .filter(Enemy::isAlive)
            .toList();
        if (alive.isEmpty()) return;

        System.out.println("  Arcane Blast hits all enemies!");
        for (Enemy enemy : alive) {
            int damage = Math.max(0, caster.getAttack() - enemy.getDefense());
            enemy.takeDamage(damage);
            System.out.printf("  %s takes %d damage (HP: %d/%d).%n",
                enemy.getName(), damage, enemy.getCurrentHp(), enemy.getMaxHp());
            if (!enemy.isAlive()) {
                caster.setAttack(caster.getAttack() + 10);
                System.out.printf("  %s eliminated! Wizard ATK +10 -> %d.%n",
                    enemy.getName(), caster.getAttack());
            }
        }
    }
}
