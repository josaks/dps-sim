package main.abilities;

import java.util.Timer;
import java.util.TimerTask;
import main.utils.ATTACKRESULT;
import main.utils.YellowAttackTable;
import main.utils.Constants;

public class Bloodthirst implements Ability{
    private boolean ready = true;
    private Timer timer = new Timer();
    private final int RAGECOST = 30;
    private final int COOLDOWN = 6000;
    int ap;
    YellowAttackTable attackTable;
    int mhWeaponSkill;
    
    public Bloodthirst(int ap, YellowAttackTable attackTable, int mhWeaponSkill) {
        this.ap = ap;
        this.attackTable = attackTable;
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
        }, COOLDOWN);
    }
    
    @Override
    public AttackResultContainer perform() {
        //starting the cooldown for bloodthirst
        cooldown();
        //calculating bloodthirst damage
        int damage = (int) (ap * 0.45);
        ATTACKRESULT result = attackTable.attack(Constants.getRandomIntWithCeiling(1000),
                Constants.getRandomIntWithCeiling(1000));
        damage *= ATTACKRESULT.getDamageModifier(result, mhWeaponSkill);
        String stringToDisplay = "Bloodthirst " + result + " for " + damage;
        AttackResultContainer arContainer = new AttackResultContainer(result, stringToDisplay, damage);
        return arContainer;
    }
    
    @Override
    public int getRageCost() {
        return RAGECOST;
    }
}