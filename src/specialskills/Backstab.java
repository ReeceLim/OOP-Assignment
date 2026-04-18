package specialskills;

import base.Enemy;
import base.Player;
import base.SpecialSkill;

import java.util.List;

/*
Rogue skill: deals double attack to one target if it is stunned,
otherwise deals normal damage.
Cooldown: 3 turns.
 */
public class Backstab extends SpecialSkill {

    public Backstab() {
        super("Backstab");
    }

    @Override
    public boolean requiresTarget() { return true; }

    @Override
    public void execute(Player caster, List<Enemy> enemies) {
        if (enemies.isEmpty()) return;
        Enemy target = enemies.get(0);

        int baseDamage = Math.max(0, caster.getAttack() - target.getDefense());

        if (target.getCurrentHp() > target.getMaxHp() / 2) {
            int damage = baseDamage * 2;
            target.takeDamage(damage);
            System.out.printf(
                "  Backstab! %s is above half HP! %s strikes for DOUBLE damage: %d! (HP: %d/%d)%n",
                target.getName(), caster.getName(), damage, target.getCurrentHp(), target.getMaxHp());
        } else {
            target.takeDamage(baseDamage);
            System.out.printf(
            "  Backstab! %s is already wounded! %s deals normal damage: %d. (HP: %d/%d)%n",
            target.getName(), caster.getName(), baseDamage, target.getCurrentHp(), target.getMaxHp());
        }
    }
}