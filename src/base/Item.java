package base;
 
public abstract class Item {
    protected final String name;
 
    public Item(String name) { this.name = name; }
 
    public String getName() { return name; }
 
    public abstract void use(Player user);
 
    @Override
    public String toString() { return name; }
}