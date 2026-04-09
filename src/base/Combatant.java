package base;

import java.util.ArrayList;
import java.util.List;

public abstract class Combatant {

    protected final String name;
    protected final int maxHp;
    protected int currentHp;
    protected int attack;
    protected int defense;
    protected final int speed;
    protected boolean alive;
    // for stun (Status effect)
    private boolean stunned = false;
    // for smoke bomb (Status Effect)
    private boolean smokeBombActive = false;

    private final List<StatusEffect> statusEffects = new ArrayList<>();

    public Combatant(String name, int hp, int attack, int defense, int speed) {
        this.name      = name;
        this.maxHp     = hp;
        this.currentHp = hp;
        this.attack    = attack;
        this.defense   = defense;
        this.speed     = speed;
        this.alive     = true;
    }

    public void takeDamage(int rawDamage) {
        // for smoke bomb (Status Effect) so smoke bomb will come first, if use smoke bomb, 0 damage for the player
        if (isSmokeBombActive()) {
            System.out.println(name + " takes 0 damage due to Smoke Bomb!");
            return;
        }
        int effective = Math.max(0, rawDamage - defense);
        currentHp = Math.max(0, currentHp - effective);
        if (currentHp == 0) alive = false;
        
    }

    public void takeFlatDamage(int amount) {
        currentHp = Math.max(0, currentHp - amount);
        if (currentHp == 0) alive = false;
    }

    public void heal(int amount) {
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public void addStatusEffect(StatusEffect effect) {
        statusEffects.add(effect);
    }

    public void removeEffect(StatusEffect effect) {
        statusEffects.remove(effect);
    }

    public List<StatusEffect> getStatusEffects() {
        return List.copyOf(statusEffects);
    }

    // For Arcane Blast (Status Effect)
    public void addAttack(int amount) {
         attack += amount; 
    }

    public void addDefense(int amount) {
            defense += amount;
    }

    public void reduceDefense(int amount) {
        defense -= amount;
    }

    // For stun (Status Effect)
    public void setStunned(boolean value) {
         stunned = value; 
    }

    // for smoke bomb (Status Effect)
    public void setSmokeBombActive(boolean value) { 
        smokeBombActive = value; 
    }

    public void applyStatusEffects() {
        List<StatusEffect> toRemove = new ArrayList<>();
        for (StatusEffect effect : statusEffects) {
            effect.tick(this);   // decrease duration 
            if (effect.isExpired()) {
                toRemove.add(effect);
            }
        }
        statusEffects.removeAll(toRemove);
    }

    
    // Getters

    public String getName()     { return name; }
    public int    getMaxHp()    { return maxHp; }
    public int    getCurrentHp(){ return currentHp; }
    public int    getAttack()   { return attack; }
    public int    getDefense()  { return defense; }
    public int    getSpeed()    { return speed; }
    public boolean isAlive()    { return alive; }
    public boolean isStunned()  { return stunned; }
    public boolean isSmokeBombActive() { return smokeBombActive; }

    @Override
    public String toString() {
        return String.format("%s [HP: %d/%d | ATK: %d | DEF: %d | SPD: %d]",
                name, currentHp, maxHp, attack, defense, speed);
    }
}