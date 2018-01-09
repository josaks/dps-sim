package main.abilities;

import main.utils.ATTACKRESULT;

public class AttackResultContainer {
    ATTACKRESULT attackResult;
    String attackResultString;
    
    public ATTACKRESULT getAttackResult() {
        return attackResult;
    }

    public String getAttackResultString() {
        return attackResultString;
    }

    protected AttackResultContainer(ATTACKRESULT attackResult, String attackResultString) {
        this.attackResult = attackResult;
        this.attackResultString = attackResultString;
    }
}
