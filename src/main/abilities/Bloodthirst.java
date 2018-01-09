package main.abilities;

import main.counters.DamageCounter;
import java.util.Timer;
import java.util.TimerTask;
import main.utils.ATTACKRESULT;
import main.utils.AttackTable;

public class Bloodthirst implements Ability{
    private boolean ready = true;
    private Timer timer = new Timer();
    private static final int RAGECOST = 30;
    private static final int COOLDOWN = 6000;
    DamageCounter dmgCounter;
    int ap;
    AttackTable attackTable;
    int mhWeaponSkill;
    
    public Bloodthirst(int ap, AttackTable attackTable, DamageCounter dmgCounter, int mhWeaponSkill) {
        this.ap = ap;
        this.attackTable = attackTable;
        this.dmgCounter = dmgCounter;
        this.mhWeaponSkill = mhWeaponSkill;
    }
    
    @Override
    public boolean isReady() {
        return ready;
    }
    
    private void cooldown() {
        ready = false;
        timer.schedule(new TimerTask() {
            public void run() {
                ready = true;
            }
        }, Bloodthirst.COOLDOWN);
    }
    
    @Override
    public AttackResultContainer perform() {
        //starting the cooldown for bloodthirst
        cooldown();
        //calculating bloodthirst damage
        int damage = (int) (ap * 0.45);
        ATTACKRESULT result = attackTable.yellowAttackRoll();
        damage *= ATTACKRESULT.getDamageModifier(result, mhWeaponSkill);
        dmgCounter.addDamage(damage);
        String stringToDisplay = "Bloodthirst " + result + " for " + damage;
        AttackResultContainer arContainer = new AttackResultContainer(result, stringToDisplay);
        return arContainer;
    }
    
    @Override
    public int getRageCost() {
        return RAGECOST;
    }
}