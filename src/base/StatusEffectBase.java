package base;
 
public interface StatusEffectBase {
    void tick();
    boolean isActive();
    String getEffectName();
}