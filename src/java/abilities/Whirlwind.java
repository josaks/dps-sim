package abilities;

import java.util.Timer;
import java.util.TimerTask;

import counters.DamageCounter;
import utils.ATTACKRESULT;
import utils.AttackTable;

public class Whirlwind implements Ability{
    private boolean ready = true;
    private Timer timer = new Timer();
    private static final int RAGECOST = 25;
    private static final int COOLDOWN = 10000;
    DamageCounter dmgCounter;
    AttackTable attackTable;
    int mhWeaponSkill;
    int minWeapDmg;
    int maxWeapDmg;
    
    public Whirlwind(int minWeapDmg, int maxWeapDmg, AttackTable attackTable, DamageCounter dmgCounter, int mhWeaponSkill) {
        this.minWeapDmg = minWeapDmg;
        this.maxWeapDmg = maxWeapDmg;
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
        }, COOLDOWN);
    }
    
    @Override
    public AttackResultContainer perform() {
        //starting the cooldown for whirlwind
        cooldown();
        //calculating whirlwind damage
        int damage = (int) ((minWeapDmg + (maxWeapDmg-minWeapDmg)*Math.random()));
        ATTACKRESULT result = attackTable.yellowAttackRoll();
        damage *= ATTACKRESULT.getDamageModifier(result, mhWeaponSkill);
        dmgCounter.addDamage(damage);
        String stringToDisplay = "Whirlwind " + result + " for " + damage;
        AttackResultContainer arContainer = new AttackResultContainer(result, stringToDisplay);
        return arContainer;
    }
    
    @Override
    public int getRageCost() {
        return RAGECOST;
    }
}
