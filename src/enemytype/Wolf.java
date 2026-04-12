package enemytype;

import base.Enemy;
import actions.*;

public class Wolf extends Enemy {
    private static final int BASE_HP  = 40;
    private static final int BASE_ATK = 45;
    private static final int BASE_DEF = 5;
    private static final int BASE_SPD = 35;

    public Wolf(String name) {
        super(name, BASE_HP, BASE_ATK, BASE_DEF, BASE_SPD, new BasicAttack());
    }

}
