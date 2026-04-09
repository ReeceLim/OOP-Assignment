package statuseffects;

import base.Combatant;
import base.StatusEffect;

public class DefendEffect extends StatusEffect {

    private final int defenseBoost;

    public DefendEffect(int boost, int duration) {
        super("Defend", duration);
        this.defenseBoost = boost;
    }

    @Override
    public void apply(Combatant target) {
        System.out.println(target.getName() + " is defending! DEF +" + defenseBoost);
        target.addDefense(defenseBoost); 
    }

    @Override
    public void tick(Combatant target) {
        super.tick(target);
        if (isExpired()) {
            target.reduceDefense(defenseBoost);
            System.out.println(target.getName() + "'s defend effect wore off.");
        }
    }
}