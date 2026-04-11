package enemytype;

import base.Enemy;
import base.IEnemyAction;

public class Goblin extends Enemy {
    private static final int BASE_HP  = 55;
    private static final int BASE_ATK = 35;
    private static final int BASE_DEF = 15;
    private static final int BASE_SPD = 25;

    public Goblin() {
        super("Goblin", BASE_HP, BASE_ATK, BASE_DEF, BASE_SPD, new IEnemyAction());
    }

    @Override
    public String getTypeName() { return "Goblin"; }
}
