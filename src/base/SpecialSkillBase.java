package base;
 
import java.util.List;
 
public abstract class SpecialSkillBase {
    private final String skillName;
 
    public SpecialSkillBase(String skillName) { this.skillName = skillName; }
 
    public String getSkillName() { return skillName; }
 
    public abstract void execute(Player caster, List<EnemyBase> enemies);
}