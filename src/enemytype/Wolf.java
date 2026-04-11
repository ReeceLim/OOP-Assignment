package enemytype;

import base.Enemy;

public class Wolf extends Enemy {
    private static final int BASE_HP  = 40;
    private static final int BASE_ATK = 45;
    private static final int BASE_DEF = 5;
    private static final int BASE_SPD = 35;

    public Wolf(String instanceName) {
        super(instanceName, BASE_HP, BASE_ATK, BASE_DEF, BASE_SPD);
    }

    @Override
    public String getTypeName() { return "Wolf"; }
}
