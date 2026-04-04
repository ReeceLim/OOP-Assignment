package base;

import statuseffects.Stun;

public abstract class EnemyBase {
    private final String name;
    private final int maxHP;
    private int currentHP;
    private final int atk;
    private final int def;
    private final int spd;

    private Stun stun;

    public EnemyBase(String name, int hp, int atk, int def, int spd) {
        this.name = name;
        this.maxHP = hp;
        this.currentHP = hp;
        this.atk = atk;
        this.def = def;
        this.spd = spd;
        this.stun = null;
    }

    // --- Stats ---

    public String getName() { return name; }
    public int getHP() { return currentHP; }
    public int getMaxHP() { return maxHP; }
    public int getAtk() { return atk; }
    public int getDef() { return def; }
    public int getSpd() { return spd; }
    public boolean isAlive() { return currentHP > 0; }

    public void takeDamage(int damage) {
        currentHP = Math.max(0, currentHP - damage);
    }

    // --- Stun ---

    public boolean isStunned() { return stun != null && stun.isActive(); }

    public void applyStun() {
        this.stun = new Stun(2);
    }

    /** Called at the start of the enemy's turn; returns true if the turn is skipped. */
    public boolean tickStunAndCheckSkip() {
        if (stun != null && stun.isActive()) {
            stun.tick();
            return true; // skip turn
        }
        return false;
    }

    public abstract String getTypeName();
}