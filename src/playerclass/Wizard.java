package playerclass;

import base.Player;
import specialskills.ArcaneBlast;
 
public class Wizard extends Player {
    private static final int BASE_HP  = 200;
    private static final int BASE_ATK = 50;
    private static final int BASE_DEF = 10;
    private static final int BASE_SPD = 20;
 
    public Wizard() {
        super(BASE_HP, BASE_ATK, BASE_DEF, BASE_SPD);
        this.specialSkill = new ArcaneBlast();
    }
 
    @Override
    public String getClassName() { return "Wizard"; }
}
