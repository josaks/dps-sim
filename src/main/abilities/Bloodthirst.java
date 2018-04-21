package main.abilities;

import java.util.Timer;
import java.util.TimerTask;

import main.model.Player;
import main.utils.ATTACKRESULT;
import main.utils.AttackTable;
import main.utils.Constants;

public class Bloodthirst implements Ability{
    private boolean ready = true;
    private Timer timer = new Timer();
    private final int RAGECOST = 30;
    private final int COOLDOWN = 6000;
    
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
    public AttackResultContainer perform(Player character, AttackTable aTable) {
        //starting the cooldown for bloodthirst
        cooldown();
        //calculating bloodthirst damage
        int damage = (int) (character.getAp() * 0.45);
        ATTACKRESULT result = aTable.yellowResult(Constants.getRandomIntWithCeiling(1000),
                Constants.getRandomIntWithCeiling(1000));
        damage *= ATTACKRESULT.getResultDamageModifier(result, character.findMhWeaponSkill());
        damage *= character.getDamageModifier();
        String stringToDisplay = "Bloodthirst " + result + " for " + damage;
        AttackResultContainer arContainer = new AttackResultContainer(result, stringToDisplay, damage);
        return arContainer;
    }
    
    @Override
    public int getRageCost() {
        return RAGECOST;
    }
}