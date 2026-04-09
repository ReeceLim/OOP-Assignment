package statuseffects;

import base.Combatant;
import base.StatusEffect;

public class SmokeBombEffect extends StatusEffect {

    public SmokeBombEffect() {
        super("Smoke Bomb", 2); // duration will be 2 turn, current and next 
    }

    @Override
    public void apply(Combatant target) {
        System.out.println(target.getName() + " is protected by Smoke Bomb! Damage is 0 this turn."); // Enemy attack do 0 damage
        target.setSmokeBombActive(true);
    }

    @Override
    public void tick(Combatant target) {
        super.tick(target);
        if (isExpired()) {
            target.setSmokeBombActive(false);
            System.out.println(target.getName() + "'s Smoke Bomb effect has expired.");
        }
    }
}