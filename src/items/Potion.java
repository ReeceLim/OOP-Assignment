package items;
 
import base.Item;
import base.Player;
 
public class Potion extends Item {
 
    public Potion() { super("Potion"); }
 
    @Override
    public void use(Player user) {
        int before = user.getCurrentHp();
        user.heal(100);
        System.out.printf("  %s restored %d HP! (HP: %d/%d)%n",
            user.getName(), user.getCurrentHp() - before,
            user.getCurrentHp(), user.getMaxHp());
    }
}
