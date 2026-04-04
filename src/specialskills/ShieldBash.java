package specialskills;

import base.EnemyBase;
import base.PlayerBase;
import base.SpecialSkillBase;

import java.util.List;
import java.util.Scanner;

/**
 * Warrior skill: deals BasicAttack damage to a selected enemy and stuns it for 2 turns.
 */
public class ShieldBash extends SpecialSkillBase {
    public ShieldBash() {
        super("Shield Bash");
    }

    @Override
    public void execute(PlayerBase caster, List<EnemyBase> enemies) {
        // Let player pick a target
        List<EnemyBase> alive = enemies.stream().filter(EnemyBase::isAlive).toList();
        if (alive.isEmpty()) return;

        System.out.println("Choose a target for Shield Bash:");
        for (int i = 0; i < alive.size(); i++) {
            System.out.printf("  [%d] %s (HP: %d)%n", i + 1, alive.get(i).getName(), alive.get(i).getHP());
        }

        Scanner sc = new Scanner(System.in);
        int choice = -1;
        while (choice < 0 || choice >= alive.size()) {
            System.out.print("Enter choice: ");
            try { choice = Integer.parseInt(sc.nextLine().trim()) - 1; }
            catch (NumberFormatException e) { choice = -1; }
        }

        EnemyBase target = alive.get(choice);
        int damage = Math.max(0, caster.getAtk() - target.getDef());
        target.takeDamage(damage);
        target.applyStun();

        System.out.printf("Shield Bash hits %s for %d damage! %s is STUNNED for 2 turns.%n",
                target.getName(), damage, target.getName());
    }
}
