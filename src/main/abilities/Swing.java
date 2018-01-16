package main.abilities;

import main.model.Player;
import main.utils.AttackTable;

public interface Swing {
    public AttackResultContainer perform(Player character, AttackTable aTable);
    public int getRageCost();
}
