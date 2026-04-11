package items;
 
import base.EnemyBase;
import base.Item;
import base.Player;
 
import java.util.List;
import java.util.stream.Collectors;
 
public class PowerStone extends Item {
 
    public PowerStone() { super("Power Stone"); }
 
    @Override
    public void use(Player user) {
        System.out.printf("  %s used Power Stone! Special skill triggers for free (cooldown unchanged).%n", user.getName());
    }
 
    public void useWithEnemies(Player user, List<EnemyBase> enemies) {
        System.out.printf("  %s used Power Stone! Special skill triggers for free.%n", user.getName());
        int currentCooldown = user.getSpecialCooldown();
        user.getSpecialSkill().execute(user, enemies);
        user.setSpecialCooldown(currentCooldown);
    }
}