package base;
 
public abstract class EnemyBase extends Combatant {
 
    public EnemyBase(String name, int hp, int atk, int def, int spd) {
        super(name, hp, atk, def, spd);
    }
 
    public abstract String getTypeName();
 
    public ICombatAction decideAction() {
        return new actions.BasicAttack();
    }
}
 