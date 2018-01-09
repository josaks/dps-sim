package main.utils;

import org.apache.commons.lang3.NotImplementedException;

public class AttackTable {
    private final int hit;
    private final int crit;
    private final int mhWeaponSkill;
    private final int ohWeaponSkill;
    
    public AttackTable(int hit, int crit, int mhWeaponSkill) {
        this(hit,crit,mhWeaponSkill, 300);
    }
    
    public AttackTable(int hit, int crit, int mhWeaponSkill, int ohWeaponSkill) {
        this.hit = hit;
        this.crit = crit;
        this.mhWeaponSkill = mhWeaponSkill;
        this.ohWeaponSkill = ohWeaponSkill;
    }
    
    
    //determine ATTACKRESULT for a yellow attack
    public ATTACKRESULT yellowAttackRoll() {
        int roll = (int)(Math.random()*1000) + 1;
        int dodgeChance = 56;
        int weaponSkill = mhWeaponSkill;
        int missChance;
        if(Constants.bossDefSkill - weaponSkill <= 10) missChance = 50 - hit + (Constants.bossDefSkill - weaponSkill);
        else missChance = 70 - hit + (Constants.bossDefSkill - weaponSkill - 10) * 4;
        
        if(roll > 0 && roll <= missChance) return ATTACKRESULT.MISS;
        if(roll > missChance && roll <= (missChance + dodgeChance)) return ATTACKRESULT.DODGE;
        if((Math.random() * 1000 + 1) > crit) return ATTACKRESULT.CRIT;
        else return ATTACKRESULT.HIT;
    }
    
    //determine attackresult for a white mh attack
    public ATTACKRESULT mhAttackRoll() {
        int roll = (int)(Math.random()*1000) + 1;
        //assuming 5,6% dodge chance against bosses
        int dodgeChance = 56;
        //setting weaponskill correctly
        int weaponSkill = mhWeaponSkill;
        //chance of glancing
        int glancingSkillCap = 300;
        int glancingChance;
        if(weaponSkill > glancingSkillCap) glancingChance = 100 + (Constants.bossDefSkill - glancingSkillCap) * 20;
        else glancingChance = 100 + (Constants.bossDefSkill - weaponSkill) * 20;
        int missChance;
        //if the difference between boss defence skill and your weaponskill is 10 or less
        if(Constants.bossDefSkill - weaponSkill <= 10) missChance = 50 - hit + (Constants.bossDefSkill - weaponSkill);
        //if the difference between boss defence skill and your weaponskill is more than 10
        else missChance = 70 - hit + (Constants.bossDefSkill - weaponSkill - 10) * 4;
        
        if(roll > 0 && roll <= missChance) return ATTACKRESULT.MISS;
        if(roll > missChance && roll <= (missChance + dodgeChance)) return ATTACKRESULT.DODGE;
        if(roll > (missChance + dodgeChance) && roll <= (missChance + dodgeChance + glancingChance)) return ATTACKRESULT.GLANCING;
        if(roll > (missChance + dodgeChance + glancingChance) && roll <= (missChance + dodgeChance + glancingChance + crit)) return ATTACKRESULT.CRIT;
        else return ATTACKRESULT.HIT;
    
    }
    
    //determine attackresult for a white oh attack
    public ATTACKRESULT ohAttackRoll() {
        throw new NotImplementedException("OH attack roll not implemented yet");
    }
    
}
