package main.abilities;

import main.utils.ATTACKRESULT;

public class AttackResultContainer {
    ATTACKRESULT attackResult;
    String attackResultString;
    int damage;
    
    public int getDamage() {
        return damage;
    }

    public ATTACKRESULT getAttackResult() {
        return attackResult;
    }

    public String getAttackResultString() {
        return attackResultString;
    }

    protected AttackResultContainer(ATTACKRESULT attackResult, String attackResultString, int damage) {
        this.attackResult = attackResult;
        this.attackResultString = attackResultString;
        this.damage = damage;
    }
}
