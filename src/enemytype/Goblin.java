package enemytype;

import base.EnemyBase;

public class Goblin extends EnemyBase {
    private static final int BASE_HP  = 55;
    private static final int BASE_ATK = 35;
    private static final int BASE_DEF = 15;
    private static final int BASE_SPD = 25;

    public Goblin(String instanceName) {
        super(instanceName, BASE_HP, BASE_ATK, BASE_DEF, BASE_SPD);
    }

    @Override
    public String getTypeName() { return "Goblin"; }
}
