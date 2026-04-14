package base;

public abstract class Enemy extends Combatant {

    private ICombatAction action;

    protected Enemy(String name, int hp, int attack, int defense, int speed,
                    ICombatAction action) {
        super(name, hp, attack, defense, speed);
        this.action = action;
    }

    public void setAction(ICombatAction action) {
        this.action = action;
    }

    public ICombatAction getAction() { return action; }

}