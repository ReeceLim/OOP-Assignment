package specialskills;

import base.Player;
import base.Combatant;
import base.Enemy;
import base.SpecialSkill;
import statuseffects.Stun;
import java.util.List;

/**
 * Warrior skill: deals BasicAttack damage to a selected enemy and stuns it for 2 turns.
 */
public class ShieldBash extends SpecialSkill {
    public ShieldBash() {
        super("Shield Bash");
    }

    @Override
    public void execute(Player caster, List<Enemy> enemies) {
        if (enemies.isEmpty()) return;
        Combatant target = enemies.get(0); // target pre-selected by GameCLI
        int damage = Math.max(0, caster.getAttack() - target.getDefense());
        target.takeDamage(damage);
        target.addStatusEffect(new Stun(2));
        System.out.printf("  Shield Bash hits %s for %d damage! %s is STUNNED for 2 turns.%n",
            target.getName(), damage, target.getName());
    }
}