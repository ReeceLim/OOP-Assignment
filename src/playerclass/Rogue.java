package playerclass;

import base.Player;
import specialskills.Backstab;

public class Rogue extends Player {
    private static final int BASE_HP  = 180;
    private static final int BASE_ATK = 55;
    private static final int BASE_DEF = 8;
    private static final int BASE_SPD = 40;

    public Rogue() {
        super("Rogue", BASE_HP, BASE_ATK, BASE_DEF, BASE_SPD);
        this.specialSkill = new Backstab();
    }

    @Override
    public String getClassName() { return "Rogue"; }
}