package base;
 
public interface StatusEffect {
    void tick();
    boolean isActive();
    String getEffectName();
    int getTurnsRemaining();
}