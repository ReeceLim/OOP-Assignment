package actions;

import base.Combatant;
import base.ICombatAction;
import base.Player;
import base.Enemy;
import managers.BattleManager;

import java.util.List;
import java.util.stream.Collectors;

public class SpecialSkillAction implements ICombatAction {

    @Override
    public void execute(Combatant actor, List<Combatant> enemies, BattleManager manager) {
        if (!(actor instanceof Player p)) return;
        List<Enemy> living = enemies.stream()
            .filter(e -> e instanceof Enemy && e.isAlive())
            .map(e -> (Enemy) e)
            .collect(Collectors.toList());
        p.getSpecialSkill().execute(p, living);
        p.setSpecialCooldown(3);
    }

    @Override
    public String getDisplayName() { return "Special Skill"; }
}