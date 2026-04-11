package playerclass;
 
import base.Player;
import base.SpecialSkill;
import base.Item;
import specialskills.ShieldBash;

import java.util.List;
 
public class Warrior extends Player {
    private static final int BASE_HP  = 260;
    private static final int BASE_ATK = 40;
    private static final int BASE_DEF = 20;
    private static final int BASE_SPD = 30;
 
    public Warrior(List<Item> items) {
        super("Warrior", BASE_HP, BASE_ATK, BASE_DEF, BASE_SPD, items);
    }

    @Override
    public SpecialSkill getSpecialSkill() { return new ShieldBash(); }
}
