package enemytype;

import base.Enemy;
import actions.*;

public class Troll extends Enemy {
    private static final int BASE_HP  = 120;
    private static final int BASE_ATK = 30;
    private static final int BASE_DEF = 25;
    private static final int BASE_SPD = 10;

    public Troll(String name) {
        super(name, BASE_HP, BASE_ATK, BASE_DEF, BASE_SPD, new BasicAttack());
    }
}

