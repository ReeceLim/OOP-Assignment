package base;

public interface StatusEffectBase {
    /** Decrements the effect's duration by one turn. */
    void tick();

    /** Returns true while the effect still has turns remaining. */
    boolean isActive();

    /** Human-readable name for display purposes. */
    String getEffectName();
}