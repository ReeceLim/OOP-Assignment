package specialskills;

import base.Player;
import base.Combatant;
import base.Enemy;
import base.SpecialSkill;
import statuseffects.Stun;
import java.util.List;
import java.util.Scanner;

/**
 * Warrior skill: deals BasicAttack damage to a selected enemy and stuns it for 2 turns.
 */
public class ShieldBash extends SpecialSkill {
    public ShieldBash() {
        super("Shield Bash");
    }

    @Override
    public void execute(Player caster, List<Enemy> enemies) {
        // Let player pick a target
        List<Enemy> alive = enemies.stream().filter(Enemy::isAlive).toList();
        if (alive.isEmpty()) return;

        System.out.println("Choose a target for Shield Bash:");
        for (int i = 0; i < alive.size(); i++) {
            System.out.printf("  [%d] %s (HP: %d)%n", i + 1, alive.get(i).getName(), alive.get(i).getCurrentHp());
        }

        Scanner sc = new Scanner(System.in);
        int choice = -1;
        while (choice < 0 || choice >= alive.size()) {
            System.out.print("Enter choice: ");
            try { choice = Integer.parseInt(sc.nextLine().trim()) - 1; }
            catch (NumberFormatException e) { choice = -1; }
        }

        Combatant target = alive.get(choice);
        int damage = Math.max(0, caster.getAttack() - target.getDefense());
        target.takeDamage(damage);
        target.addStatusEffect(new Stun());

        System.out.printf("Shield Bash hits %s for %d damage! %s is STUNNED for 2 turns.%n",
                target.getName(), damage, target.getName());
    }
}
