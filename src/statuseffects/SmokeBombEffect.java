package statuseffects;
 
import base.Combatant;
import base.StatusEffect;
 
public class SmokeBombEffect extends StatusEffect {
    public SmokeBombEffect() {
        super("SmokeBomb", 2);
    }
 
    @Override
    public void apply(Combatant target) {
        System.out.printf("  %s is shrouded in smoke! Enemy attacks deal 0 damage.%n",
                target.getName());
    }
 
    @Override
    protected void onExpire(Combatant target) {
        System.out.printf("  %s's Smoke Bomb has worn off.%n", target.getName());
    }
}
 