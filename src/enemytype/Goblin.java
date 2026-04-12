package enemytype;

import base.Enemy;
import actions.*;

public class Goblin extends Enemy {
    private static final int BASE_HP  = 55;
    private static final int BASE_ATK = 35;
    private static final int BASE_DEF = 15;
    private static final int BASE_SPD = 25;

    public Goblin(String name) {
        super(name, BASE_HP, BASE_ATK, BASE_DEF, BASE_SPD, new BasicAttack());
    }
}
