package specialskills;

import base.Combatant;
import base.SpecialSkill;

import java.util.List;

public class ArcaneBlast extends SpecialSkill {
    public ArcaneBlast() {
        super("Arcane Blast");
    }

    @Override
    public void execute(Combatant caster, List<Combatant> enemies) {
        List<Combatant> alive = enemies.stream().filter(Combatant::isAlive).toList();
        if (alive.isEmpty()) return;

        System.out.println("Arcane Blast hits all enemies!");
        int kills = 0;

        for (Combatant enemy : alive) {
            int damage = Math.max(0, caster.getAttack() - enemy.getDefense());
            enemy.takeDamage(damage);
            System.out.printf("  %s takes %d damage (HP: %d).%n", enemy.getName(), damage, enemy.getCurrentHp());

            if (!enemy.isAlive()) {
                kills++;
                // Grant +10 ATK per kill before applying to next target
                caster.addAttack(10);
                System.out.printf("  %s eliminated! Wizard ATK +10 → %d.%n", enemy.getName(), caster.getAttack());
            }
        }

        if (kills == 0) System.out.println("No enemies defeated.");
    }
}
