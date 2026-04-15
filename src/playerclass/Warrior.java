package playerclass;

import base.Player;
import specialskills.ShieldBash;
 
public class Warrior extends Player {
    private static final int BASE_HP  = 260;
    private static final int BASE_ATK = 40;
    private static final int BASE_DEF = 20;
    private static final int BASE_SPD = 30;
 
    public Warrior() {
        super("Warrior", BASE_HP, BASE_ATK, BASE_DEF, BASE_SPD);
        this.specialSkill = new ShieldBash();
    }
 
    @Override
    public String getClassName() { return "Warrior"; }
}