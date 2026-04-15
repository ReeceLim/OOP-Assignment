package base;

import java.util.ArrayList;
import java.util.List;

public abstract class Player extends Combatant {

    protected SpecialSkill specialSkill;
    private int specialCooldown = 0;
    private final List<Item> inventory = new ArrayList<>();
    private final int baseDefense;

    public Player(String name, int hp, int atk, int def, int spd) {
        super(name, hp, atk, def, spd);
        this.baseDefense = def;
    }

    public abstract String getClassName();

    public List<Item> getInventory()           { return inventory; }
    public void addItem(Item item)             { inventory.add(item); }
    public boolean hasItems()                  { return !inventory.isEmpty(); }
    public SpecialSkill getSpecialSkill()  { return specialSkill; }
    public boolean canUseSpecial()             { return specialCooldown == 0; }
    public int getSpecialCooldown()            { return specialCooldown; }
    public void setSpecialCooldown(int cd)     { specialCooldown = cd; }
    public void tickCooldown()                 { if (specialCooldown > 0) specialCooldown--; }
    public int getBaseDefense()                { return baseDefense; }

    @Override
    public String toString() {
        return String.format("%s [HP: %d/%d | ATK: %d | DEF: %d | SPD: %d | Cooldown: %s]",
            getClassName(), currentHp, maxHp, attack, defense, speed,
            specialCooldown == 0 ? "Ready" : specialCooldown + " turns");
    }
}