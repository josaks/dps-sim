package main.abilities;

import main.model.Player;
import main.utils.AttackTable;

public interface Ability {
    public AttackResultContainer perform(Player character, AttackTable aTable);
    public boolean isReady();
    public int getRageCost();
    //public void reset();
}