package base;

public abstract class StatusEffect
{
    protected final String name; // name of effect
    protected int remainingTurns; // how many turns the effect lasts for
    protected boolean applied; // whether the combatant got the effect

    public StatusEffect(String name, int duration){
        this.name = name;
        this.remainingTurns = duration;
        this.applied = false;
    }

    public abstract void apply(Combatant target); 

    public void tick(Combatant target) {
        if (!applied) {
            apply(target);
            applied = true;
        }
        remainingTurns--;
        if (remainingTurns <= 0) {
            onExpire(target);  
        }
    }

    protected void onExpire(Combatant target) {}

    public boolean isExpired(){
        return remainingTurns <= 0;
    }

    // getter method
    public String getName(){ return name; }
    public int getRemainingTurns(){ return remainingTurns; }

    @Override
    public String toString(){
        return String.format("%s (%d turns left", name, remainingTurns);
    }

}