package items;
 
import base.Enemy;
import base.Item;
import base.Player;
 
import java.util.List;
 
public class Powerstone extends Item {
 
    public Powerstone() { super("Power Stone"); }
 
    @Override
    public void use(Player user) {
        throw new UnsupportedOperationException("PowerStone requires targets. Use useWithEnemies().");
    }
 
    public void useWithEnemies(Player user, List<Enemy> enemies) {
        System.out.printf("  %s used Power Stone! Special skill triggers for free.%n", user.getName());
        int currentCooldown = user.getSpecialCooldown();
        user.getSpecialSkill().execute(user, enemies);
        user.setSpecialCooldown(currentCooldown);
    }
}