package main.abilities;

import main.model.Player;
import main.utils.AttackTable;

public class OffhandAttack implements Swing{

    @Override
    public AttackResultContainer perform(Player character, AttackTable aTable) {
        throw new UnsupportedOperationException("offhandattack not implemented yet");
    }

    public int getRageCost() {
        return 0;
    }
}
