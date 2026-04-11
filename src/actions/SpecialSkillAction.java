package actions;

import base.Combatant;
import base.EnemyBase;
import base.ICombatAction;
import base.Player;
import managers.BattleManager;

import java.util.List;
import java.util.stream.Collectors;

public class SpecialSkillAction implements ICombatAction {

    @Override
    public void execute(Combatant actor, List<Combatant> enemies, BattleManager manager) {
        if (!(actor instanceof Player p)) return;
        List<EnemyBase> living = enemies.stream()
            .filter(e -> e instanceof EnemyBase && e.isAlive())
            .map(e -> (EnemyBase) e)
            .collect(Collectors.toList());
        p.getSpecialSkill().execute(p, living);
        p.setSpecialCooldown(3);
    }

    @Override
    public String getDisplayName() { return "Special Skill"; }
}