package statuseffects;
 
import base.Combatant;
import base.StatusEffect;
 
public class DefendEffect extends StatusEffect {
    private static final int BONUS = 10;
 
    public DefendEffect() {
        super("Defend", 2);
    }
 
    @Override
    public void apply(Combatant target) {
        System.out.printf("  %s's defense is boosted (+%d DEF for 2 turns).%n",
                target.getName(), BONUS);
    }
 
    @Override
    protected void onExpire(Combatant target) {
        target.setDefense(target.getDefense() - BONUS);
        System.out.printf("  %s's defense bonus expired (-%d DEF).%n",
                target.getName(), BONUS);
    }
}
 