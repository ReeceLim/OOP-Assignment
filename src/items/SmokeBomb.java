package items;
 
import base.Item;
import base.Player;
import statuseffects.SmokeBombEffect;
 
public class SmokeBomb extends Item {
 
    public SmokeBomb() { super("Smoke Bomb"); }
 
    @Override
    public void use(Player user) {
        user.addStatusEffect(new SmokeBombEffect());
        System.out.printf("  %s used Smoke Bomb! Enemy attacks deal 0 damage for 2 turns.%n", user.getName());
    }
}