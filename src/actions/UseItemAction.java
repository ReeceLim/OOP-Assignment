package actions;

import base.Combatant;
import base.ICombatAction;
import base.Item;
import base.Player;
import base.Enemy;
import items.Powerstone;
import managers.BattleManager;

import java.util.List;
import java.util.stream.Collectors;

public class UseItemAction implements ICombatAction {

    private final Item item;

    public UseItemAction(Item item) { this.item = item; }

    @Override
    public void execute(Combatant actor, List<Combatant> enemies, BattleManager manager) {
        if (!(actor instanceof Player p)) return;

        if (item instanceof Powerstone ps) {
            List<Enemy> living = enemies.stream()
                .filter(e -> e instanceof Enemy && e.isAlive())
                .map(e -> (Enemy) e)
                .collect(Collectors.toList());
            ps.useWithEnemies(p, living);
        } else {
            item.use(p);
        }
        p.getInventory().remove(item);
    }

    @Override
    public String getDisplayName() { return "Use Item: " + item.getName(); }
}