package base;

import java.util.List;

public abstract class SpecialSkill {
    private final String skillName;

    public SpecialSkill(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillName() { return skillName; }
    public boolean requiresTarget() { return false; }

    public abstract void execute(Player caster, List<Enemy> enemies);
}