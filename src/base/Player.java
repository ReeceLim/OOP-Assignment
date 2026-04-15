package base;

import java.util.*;

public abstract class Player extends Combatant {
 
    private final List<Item> inventory;
    private int specialSkillCooldown = 0;
 
    public Player(String name, int hp, int attack, int defense, int speed, List<Item> items) 
    {
        super(name, hp, attack, defense, speed);
        this.inventory = new ArrayList<>(items);
    }
 
    // Cooldown
    public boolean isSpecialSkillReady()        { return specialSkillCooldown == 0; }
    public int     getSpecialSkillCooldown()    { return specialSkillCooldown; }
    public void    setSpecialSkillCooldown(int rounds) { this.specialSkillCooldown = rounds; }
 
    public void decrementCooldown() {
        if (specialSkillCooldown > 0) specialSkillCooldown--;
    }

    public List<Item> getInventory()     { return Collections.unmodifiableList(inventory); }
    public boolean    hasItems()         { return !inventory.isEmpty(); }
 
    public void consumeItem(Item item)   { inventory.remove(item); }
 
    public abstract ICombatAction getSpecialSkill();
 
    @Override
    public String toString() {
        return String.format(
            "%s [HP: %d/%d | ATK: %d | DEF: %d | SPD: %d | Cooldown: %d | Items: %d]",
            name, currentHp, maxHp, attack, defense, speed, specialSkillCooldown, inventory.size());
    }
}