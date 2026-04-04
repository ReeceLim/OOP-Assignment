package base;

public abstract class PlayerBase {
    private final int maxHP;
    private int currentHP;
    private int atk;
    private final int def;
    private final int spd;
    private int tempDefBonus;
    private int tempDefTurnsLeft;

    protected SpecialSkillBase specialSkill;
    private int specialSkillCooldown;

    public PlayerBase(int hp, int atk, int def, int spd) {
        this.maxHP = hp;
        this.currentHP = hp;
        this.atk = atk;
        this.def = def;
        this.spd = spd;
        this.tempDefBonus = 0;
        this.tempDefTurnsLeft = 0;
        this.specialSkillCooldown = 0;
    }

    // --- Stats ---

    public int getHP() { return currentHP; }
    public int getMaxHP() { return maxHP; }
    public int getAtk() { return atk; }
    public int getDef() { return def + tempDefBonus; }
    public int getSpd() { return spd; }
    public boolean isAlive() { return currentHP > 0; }

    public void setAtk(int atk) { this.atk = atk; }

    public void takeDamage(int damage) {
        currentHP = Math.max(0, currentHP - damage);
    }

    public void heal(int amount) {
        currentHP = Math.min(maxHP, currentHP + amount);
    }

    // --- Defend action ---

    /** Adds a defense bonus for 2 turns (current + next). */
    public void applyDefendBonus() {
        this.tempDefBonus = 10;
        this.tempDefTurnsLeft = 2;
    }

    /** Called at the start of each turn to tick down temporary defense. */
    public void tickDefend() {
        if (tempDefTurnsLeft > 0) {
            tempDefTurnsLeft--;
            if (tempDefTurnsLeft == 0) tempDefBonus = 0;
        }
    }

    // --- Special Skill Cooldown ---

    public int getSpecialSkillCooldown() { return specialSkillCooldown; }
    public boolean canUseSpecialSkill() { return specialSkillCooldown == 0; }

    /** Sets cooldown to 3 after using the special skill normally. */
    public void triggerSpecialSkillCooldown() { specialSkillCooldown = 3; }

    /** Called after each of the player's turns to decrement cooldown. */
    public void tickCooldown() {
        if (specialSkillCooldown > 0) specialSkillCooldown--;
    }

    public SpecialSkillBase getSpecialSkill() { return specialSkill; }

    public abstract String getClassName();
}