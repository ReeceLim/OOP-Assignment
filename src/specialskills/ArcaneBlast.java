package specialskills;

import base.EnemyBase;
import base.PlayerBase;
import base.SpecialSkillBase;

import java.util.List;

/**
 * Wizard skill: deals BasicAttack damage to ALL enemies.
 * Each enemy killed grants +10 ATK to the Wizard for the rest of the level.
 */
public class ArcaneBlast extends SpecialSkillBase {
    public ArcaneBlast() {
        super("Arcane Blast");
    }

    @Override
    public void execute(PlayerBase caster, List<EnemyBase> enemies) {
        List<EnemyBase> alive = enemies.stream().filter(EnemyBase::isAlive).toList();
        if (alive.isEmpty()) return;

        System.out.println("Arcane Blast hits all enemies!");
        int kills = 0;

        for (EnemyBase enemy : alive) {
            int damage = Math.max(0, caster.getAtk() - enemy.getDef());
            enemy.takeDamage(damage);
            System.out.printf("  %s takes %d damage (HP: %d).%n", enemy.getName(), damage, enemy.getHP());

            if (!enemy.isAlive()) {
                kills++;
                // Grant +10 ATK per kill before applying to next target
                caster.setAtk(caster.getAtk() + 10);
                System.out.printf("  %s eliminated! Wizard ATK +10 → %d.%n", enemy.getName(), caster.getAtk());
            }
        }

        if (kills == 0) System.out.println("No enemies defeated.");
    }
}
