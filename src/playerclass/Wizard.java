package playerclass;

import base.Player;
import base.Item;
import base.SpecialSkill;
import specialskills.ArcaneBlast;
 
import java.util.List;

public class Wizard extends Player {
    private static final int BASE_HP  = 200;
    private static final int BASE_ATK = 50;
    private static final int BASE_DEF = 10;
    private static final int BASE_SPD = 20;
 
    public Wizard(List<Item> items) {
        super("Wizard", BASE_HP, BASE_ATK, BASE_DEF, BASE_SPD, items);
    }
 
    @Override
    public SpecialSkill getSpecialSkill() { return new ArcaneBlast(); }
}
