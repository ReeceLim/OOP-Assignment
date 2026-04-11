package base;

import ui.BattleUI;
import base.Combatant;
import base.ICombatAction;
import base.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Player extends Combatant {

    // ── Inventory ─────────────────────────────────────────────────────────────

    private final List<Item> inventory;

    // ── Special skill cooldown ────────────────────────────────────────────────

    /** Remaining rounds before the special skill can be used again. 0 = ready. */
    private int specialSkillCooldown;

    // ── UI delegate ───────────────────────────────────────────────────────────

    private final BattleUI ui;

    // ── Constructor ───────────────────────────────────────────────────────────

    protected Player(String name, int hp, int attack, int defense, int speed,
                     List<Item> items, BattleUI ui) {
        super(name, hp, attack, defense, speed);
        this.inventory = new ArrayList<>(items);
        this.specialSkillCooldown = 0;
        this.ui = ui;
    }

    // ── Abstract: subclasses define their special skill ───────────────────────

    /**
     * Returns a fresh instance of this player's class-specific special skill.
     * Called by the engine when the player selects the special skill action,
     * and by PowerStoneItem when triggering it for free.
     */
    public abstract ICombatAction getSpecialSkill();

    // ── Turn delegation → BattleUI ────────────────────────────────────────────

    /**
     * Delegates action selection to the injected BattleUI.
     * BattleEngine calls this uniformly on every Combatant without knowing
     * whether it is talking to a Player or an Enemy (LSP / DIP).
     */
    @Override
    public ICombatAction decideAction(List<Combatant> allies, List<Combatant> enemies) {
        return ui.decidePlayerAction(this, enemies);
    }

    // ── Cooldown management ───────────────────────────────────────────────────

    /** Returns true when the special skill is available (cooldown == 0). */
    public boolean isSpecialSkillReady() {
        return specialSkillCooldown == 0;
    }

    public int getSpecialSkillCooldown() {
        return specialSkillCooldown;
    }

    /**
     * Sets the cooldown after the special skill is used.
     * Called by BattleEngine — not by the action itself, keeping cooldown
     * management centralised in the engine (SRP).
     *
     * NOTE: must NOT be called after a Power Stone use — the spec states
     * Power Stone does not start or change the cooldown timer.
     */
    public void setSpecialSkillCooldown(int rounds) {
        this.specialSkillCooldown = rounds;
    }

    /**
     * Decrements cooldown by 1 after this player has taken a turn.
     * Called by BattleEngine once per turn this player acts.
     * Has no effect when cooldown is already 0.
     */
    public void decrementCooldown() {
        if (specialSkillCooldown > 0) specialSkillCooldown--;
    }

    // ── Inventory management ──────────────────────────────────────────────────

    /** Returns an unmodifiable view of the current inventory. */
    public List<Item> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    public boolean hasItems() {
        return !inventory.isEmpty();
    }

    /**
     * Removes one instance of the given item from inventory.
     * Called by the engine after an item action is executed.
     */
    public void consumeItem(Item item) {
        inventory.remove(item);
    }

    // ── Display ───────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format("%s [HP:%d/%d ATK:%d DEF:%d SPD:%d | CD:%d | Items:%d]",
                name, currentHp, maxHp, attack, defense, speed,
                specialSkillCooldown, inventory.size());
    }
}