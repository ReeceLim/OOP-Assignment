package actions;

import base.Combatant;
import base.ICombatAction;
import managers.BattleManager;
import java.util.List;

public class PlayerBasicAttack implements ICombatAction {
    private final Combatant target;

    public PlayerBasicAttack(Combatant target) { this.target = target; }

    @Override
    public void execute(Combatant actor, List<Combatant> enemies, BattleManager manager) {
        if (target.isInvulnerable()) {
            System.out.printf("  %s attacks %s but Smoke Bomb absorbs all damage!%n",
                actor.getName(), target.getName());
            return;
        }
        int dmg = Math.max(0, actor.getAttack() - target.getDefense());
        target.takeDamage(dmg);
        System.out.printf("  %s attacks %s for %d damage! (dmg: %d-%d=%d, HP: %d/%d)%n",
            actor.getName(), target.getName(), dmg,
            actor.getAttack(), target.getDefense(), dmg,
            target.getCurrentHp(), target.getMaxHp());
    }

    @Override
    public String getDisplayName() { return "Basic Attack"; }
}