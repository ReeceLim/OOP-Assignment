package items;

import base.Item;
import base.Player;
import statuseffects.StrengthEffect;
 
public class StrengthElixir extends Item {
 
    public StrengthElixir() { super("Strength Elixir"); }
 
    @Override
    public void use(Player user) {
        user.setAttack(user.getAttack() + 20);
        user.addStatusEffect(new StrengthEffect(user));
        System.out.printf("  %s used a strength elixir! ATK +20 for this turn and next. (ATK: %d)%n",
            user.getName(), user.getAttack());
    }
}