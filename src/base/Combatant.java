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
    private final List<StatusEffectBase> statusEffects = new ArrayList<>();

    public Combatant(String name, int hp, int attack, int defense, int speed) {
        this.name = name;
        this.maxHp = hp;
        this.currentHp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.alive = true;
    }

    public void takeDamage(int damage) {
        currentHp = Math.max(0, currentHp - damage);
        if (currentHp == 0) alive = false;
    }

    public void heal(int amount) {
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public void addStatusEffect(StatusEffectBase effect) {
        statusEffects.add(effect);
    }

    public void tickStatusEffects() {
        for (StatusEffectBase e : List.copyOf(statusEffects)) {
            e.tick();
        }
        statusEffects.removeIf(e -> !e.isActive());
    }

    public boolean isStunned() {
        return statusEffects.stream().anyMatch(e -> e.getEffectName().equals("Stun") && e.isActive());
    }

    public boolean isInvulnerable() {
        return statusEffects.stream().anyMatch(e -> e.getEffectName().equals("SmokeBomb") && e.isActive());
    }

    public boolean isDefending() {
        return statusEffects.stream().anyMatch(e -> e.getEffectName().equals("Defend") && e.isActive());
    }

    public List<StatusEffectBase> getStatusEffects() { return List.copyOf(statusEffects); }

    public String getName()       { return name; }
    public int getMaxHp()         { return maxHp; }
    public int getCurrentHp()     { return currentHp; }
    public int getAttack()        { return attack; }
    public int getDefense()       { return defense; }
    public int getSpeed()         { return speed; }
    public boolean isAlive()      { return alive; }
    public void setAttack(int v)  { attack = v; }
    public void setDefense(int v) { defense = v; }
}



/*package base;

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


    // Getters

    public String getName()     { return name; }
    public int    getMaxHp()    { return maxHp; }
    public int    getCurrentHp(){ return currentHp; }
    public int    getAttack()   { return attack; }
    public int    getDefense()  { return defense; }
    public int    getSpeed()    { return speed; }
    public boolean isAlive()    { return alive; }

    @Override
    public String toString() {
        return String.format("%s [HP: %d/%d | ATK: %d | DEF: %d | SPD: %d]",
                name, currentHp, maxHp, attack, defense, speed);
    }
}*/