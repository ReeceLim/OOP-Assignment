package base;

import java.util.List;

public abstract class Enemy extends Combatant {

    private IEnemyAction action;

    protected Enemy(String name, int hp, int attack, int defense, int speed,
                    IEnemyAction action) {
        super(name, hp, attack, defense, speed);
        this.action = action;
    }

    public void setAction(IEnemyAction action) {
        this.action = action;
    }

    @Override
    public ICombatAction decideAction(List<Combatant> allies, List<Combatant> enemies) {
        return action.decideAction(this, allies, enemies);
    }
}