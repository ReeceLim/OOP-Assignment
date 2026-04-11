package base;

import java.util.List;

public abstract class SpecialSkillBase {
    private final String skillName;

    public SpecialSkillBase(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillName() { return skillName; }

    /**
     * Executes the special skill effect.
     * @param caster  the player using the skill
     * @param enemies all living enemies currently in combat
     */
    public abstract void execute(PlayerBase caster, List<EnemyBase> enemies);
}