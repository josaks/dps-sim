package main.utils;

import org.apache.commons.lang3.NotImplementedException;

public class AttackTable {
    private final int hit;
    private final int crit;
    private final int mhWeaponSkill;
    private final int ohWeaponSkill;
    
    public AttackTable(int hit, int crit, int mhWeaponSkill, int ohWeaponSkill) {
        this.hit = hit;
        this.crit = crit;
        this.mhWeaponSkill = mhWeaponSkill;
        this.ohWeaponSkill = ohWeaponSkill;
    }
    
    //determine attackresult for a white mh attack
    public ATTACKRESULT mhAttack(int roll) {
        if(roll <= 0) throw new IllegalArgumentException("roll can not be 0 or less");
        if(roll > 1000) throw new IllegalArgumentException("roll can not be more than 1000");
        
        //assuming 5,6% dodge chance against bosses
        int dodgeChance = 56;
        
        //setting weaponskill correctly
        int glancingChance = calculateGlanchingChance(mhWeaponSkill, Constants.bossDefSkill);
        
        int missChance;
        //if the difference between boss defence skill and your weaponskill is 10 or less
        if(Constants.bossDefSkill - mhWeaponSkill <= 10) missChance = 50 - hit + (Constants.bossDefSkill - mhWeaponSkill);
        //if the difference between boss defence skill and your weaponskill is more than 10
        else missChance = 70 - hit + (Constants.bossDefSkill - mhWeaponSkill - 10) * 4;
        
        if(roll > 0 && roll <= missChance) return ATTACKRESULT.MISS;
        if(roll > missChance && roll <= (missChance + dodgeChance)) return ATTACKRESULT.DODGE;
        if(roll > (missChance + dodgeChance) && roll <= (missChance + dodgeChance + glancingChance)) return ATTACKRESULT.GLANCING;
        if(roll > (missChance + dodgeChance + glancingChance) && roll <= (missChance + dodgeChance + glancingChance + crit)) return ATTACKRESULT.CRIT;
        else return ATTACKRESULT.HIT;
    }
    
    private int calculateGlanchingChance(int weaponSkill, int bossDefSkill) {
        int glancingChanceSkillCap = 300;
        if(weaponSkill > glancingChanceSkillCap) return 100 + (Constants.bossDefSkill - glancingChanceSkillCap) * 20;
        else return 100 + (Constants.bossDefSkill - weaponSkill) * 20;
    }
    
    //determine attackresult for a white oh attack
    public ATTACKRESULT ohAttack() {
        throw new NotImplementedException("OH attack roll not implemented yet");
    }
    
    public ATTACKRESULT yellowAttack(int rollForHit, int rollForCrit){
        if(rollForHit <= 0 || rollForCrit <= 0) throw new IllegalArgumentException("rolls can not be 0 or less");
        if(rollForHit > 1000 || rollForCrit > 1000) throw new IllegalArgumentException("rolls can not be more than 1000");
        
        int dodgeChance = 56;
        int weaponSkill = mhWeaponSkill;
        int missChance;
        if(Constants.bossDefSkill - weaponSkill <= 10) missChance = 50 - hit + (Constants.bossDefSkill - weaponSkill);
        else missChance = 70 - hit + (Constants.bossDefSkill - weaponSkill - 10) * 4;
        
        if(rollForHit > 0 && rollForHit <= missChance) return ATTACKRESULT.MISS;
        if(rollForHit > missChance && rollForHit <= (missChance + dodgeChance)) return ATTACKRESULT.DODGE;
        if(rollForCrit > crit) return ATTACKRESULT.CRIT;
        else return ATTACKRESULT.HIT;
    }

}
