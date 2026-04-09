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

    /* apply effect to the target combatant
    this method is called once when the effect starts or on each turn when needed */
    public abstract void apply(Combatant target); 

    /* process the effect for each turn
    decrements remaining turns
    if the combatant havent apply the status effect, apply the effect on the target */
    public void tick(Combatant target) {
        if (!applied) {
            apply(target);  // first application
            applied = true;
        }
        remainingTurns--;   // decrease duration each turn
        if (remainingTurns <= 0) {
            onExpire(target);  // optional: handle cleanup when effect ends
        }
    }

    protected void onExpire(Combatant target) {
        // do nothing. Subclasses can override.
    }

    // whether effect has expired, return True if effect expired (i.e. remainingTurns <= 0)
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